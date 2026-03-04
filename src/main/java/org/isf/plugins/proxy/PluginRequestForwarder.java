/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2026 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.plugins.proxy;

import org.isf.plugins.config.PluginDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.Collection;

/**
 * @author Steve Tsala
 */
@Component
public class PluginRequestForwarder implements IPluginRequestForwarder {

	/**
	 * Identity header carrying the authenticated username.
	 */
	public static final String HEADER_X_USER = "X-User";
	/**
	 * Identity header carrying the comma-separated permission list from the JWT.
	 */
	public static final String HEADER_X_PERMISSIONS = "X-Permissions";
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginRequestForwarder.class);
	private final RestClient restClient;

	public PluginRequestForwarder() {
		this(RestClient.builder().build());
	}

	public PluginRequestForwarder(RestClient restClient) {
		this.restClient = restClient;
	}

	@Override
	public ResponseEntity<byte[]> forward(
		PluginDefinition plugin,
		String subPath,
		String queryString,
		HttpMethod method,
		HttpHeaders incomingHeaders,
		byte[] body,
		String username,
		Collection<? extends GrantedAuthority> authorities) {

		URI targetUri = PluginUriBuilder.build(plugin, subPath, queryString);
		HttpHeaders forwardHeaders = PluginHeadersBuilder.build(incomingHeaders, username, authorities);

		LOGGER.debug("Proxying {} {} → {}", method, subPath, targetUri);

		try {
			RestClient.RequestBodySpec requestSpec = restClient.method(method)
				.uri(targetUri)
				.headers(h -> h.addAll(forwardHeaders));

			if (body != null && body.length > 0) {
				requestSpec.body(body);
			}

			return requestSpec
				.retrieve()
				.onStatus(status -> true, (req, res) -> {
					// Pass all statuses through — do not throw on 4xx/5xx from upstream.
				})
				.toEntity(byte[].class);

		} catch (RestClientResponseException ex) {
			LOGGER.warn("Upstream plugin '{}' returned error {}: {}", plugin.id(), ex.getStatusCode(), ex.getMessage());
			throw ex;
		}
	}
}
