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

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import org.isf.plugins.config.PluginDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Constructs and dispatches HTTP requests to upstream plugin services, then returns
 * the raw response back to the caller unchanged.
 *
 * <h3>Request forwarding rules</h3>
 * <ul>
 *   <li>Target URL: {@code plugin.url + subPath [+ ?queryString]}</li>
 *   <li>Method, body, and all original headers are forwarded as-is, except {@code Host}
 *       (removed to avoid conflicts with the upstream server).</li>
 *   <li>The original {@code Authorization: Bearer <token>} header is forwarded so that
 *       the plugin can optionally re-validate it.</li>
 *   <li>Two identity headers are added:
 *     <ul>
 *       <li>{@value #HEADER_X_USER} — the authenticated username</li>
 *       <li>{@value #HEADER_X_PERMISSIONS} — comma-separated granted authorities from the JWT</li>
 *     </ul>
 *   </li>
 *   <li>The full upstream response (status, headers, body) is returned unmodified.</li>
 * </ul>
 *
 * <h3>Error handling</h3>
 * If the upstream service returns an error HTTP status, that status and body are passed
 * through to the client without modification — the gateway does not swallow upstream errors.
 * {@link RestClientResponseException} is re-thrown as-is and handled by
 * {@link PluginProxyController}.
 */
@Component
public class PluginRequestForwarder {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginRequestForwarder.class);

	/** Identity header carrying the authenticated username. */
	public static final String HEADER_X_USER = "X-User";

	/** Identity header carrying the comma-separated permission list from the JWT. */
	public static final String HEADER_X_PERMISSIONS = "X-Permissions";

	/** Headers that must not be forwarded to the upstream plugin. */
	private static final Set<String> EXCLUDED_REQUEST_HEADERS = Set.of(
			HttpHeaders.HOST.toLowerCase()
	);

	private final RestClient restClient;

	public PluginRequestForwarder() {
		this.restClient = RestClient.builder().build();
	}

	/** Package-private constructor for testing — allows injecting a mock {@link RestClient}. */
	PluginRequestForwarder(RestClient restClient) {
		this.restClient = restClient;
	}

	/**
	 * Forwards an incoming request to the appropriate upstream plugin endpoint.
	 *
	 * @param plugin           the target plugin definition
	 * @param subPath          the path segment after {@code /plugins/{id}} (e.g. {@code "/documents/123"})
	 * @param queryString      the raw query string from the original request, may be {@code null}
	 * @param method           the HTTP method of the original request
	 * @param incomingHeaders  headers from the original request
	 * @param body             the raw request body, may be {@code null} for bodiless methods
	 * @param username         the authenticated username (added as {@value #HEADER_X_USER})
	 * @param authorities      the user's granted authorities (added as {@value #HEADER_X_PERMISSIONS})
	 * @return the upstream response with its original status, headers, and body
	 */
	public ResponseEntity<byte[]> forward(
			PluginDefinition plugin,
			String subPath,
			String queryString,
			HttpMethod method,
			HttpHeaders incomingHeaders,
			byte[] body,
			String username,
			Collection<? extends GrantedAuthority> authorities) {

		URI targetUri = buildTargetUri(plugin, subPath, queryString);
		HttpHeaders forwardHeaders = buildForwardHeaders(incomingHeaders, username, authorities);

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

	/**
	 * Builds the full target URI by joining the plugin base URL, sub-path, and query string.
	 * Ensures no double slashes at the join point.
	 *
	 * @param plugin      plugin definition (provides base URL)
	 * @param subPath     path after the plugin prefix, must start with {@code /} or be empty
	 * @param queryString raw query string, may be {@code null} or empty
	 * @return the resolved {@link URI}
	 */
	private URI buildTargetUri(PluginDefinition plugin, String subPath, String queryString) {
		String base = plugin.url().endsWith("/")
				? plugin.url().substring(0, plugin.url().length() - 1)
				: plugin.url();

		String path = StringUtils.hasText(subPath)
				? (subPath.startsWith("/") ? subPath : "/" + subPath)
				: "";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base + path);

		if (StringUtils.hasText(queryString)) {
			builder.query(queryString);
		}

		return builder.build(true).toUri();
	}

	/**
	 * Builds the headers to send to the upstream plugin.
	 * Copies all incoming headers except those in {@link #EXCLUDED_REQUEST_HEADERS},
	 * then appends the identity headers.
	 *
	 * @param incomingHeaders  original request headers
	 * @param username         authenticated username
	 * @param authorities      the user's granted authorities
	 * @return the assembled {@link HttpHeaders} for the upstream request
	 */
	private HttpHeaders buildForwardHeaders(
			HttpHeaders incomingHeaders,
			String username,
			Collection<? extends GrantedAuthority> authorities) {

		HttpHeaders headers = new HttpHeaders();

		incomingHeaders.forEach((name, values) -> {
			if (!EXCLUDED_REQUEST_HEADERS.contains(name.toLowerCase())) {
				headers.addAll(name, values);
			}
		});

		// Identity headers — always set, overwriting any client-supplied values.
		headers.set(HEADER_X_USER, username);
		headers.set(HEADER_X_PERMISSIONS, buildPermissionsHeader(authorities));

		return headers;
	}

	/**
	 * Converts the authority collection to a comma-separated string.
	 *
	 * @param authorities granted authorities from the JWT
	 * @return comma-separated authority strings, or empty string if none
	 */
	private String buildPermissionsHeader(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.reduce((a, b) -> a + "," + b)
				.orElse("");
	}
}
