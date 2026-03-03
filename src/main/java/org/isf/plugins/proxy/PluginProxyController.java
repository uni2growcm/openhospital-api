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

import java.io.IOException;

import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.isf.plugins.exception.PluginNotFoundException;
import org.isf.plugins.registry.PluginRegistry;
import org.isf.plugins.security.PluginAuthorizationChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Gateway controller that handles all {@code /plugins/{pluginId}/**} routes.
 *
 * <h3>Request lifecycle</h3>
 * <ol>
 *   <li><strong>Resolve plugin</strong> — looks up {@code pluginId} in the
 *       {@link PluginRegistry}; returns {@code 404} if not found or unhealthy at startup.</li>
 *   <li><strong>Authorize</strong> — delegates to {@link PluginAuthorizationChecker} to verify
 *       the authenticated user holds at least one required privilege; returns {@code 403}
 *       on failure.</li>
 *   <li><strong>Extract sub-path</strong> — strips the {@code /plugins/{pluginId}} prefix from
 *       the request URI to obtain the upstream path segment.</li>
 *   <li><strong>Forward</strong> — proxies the full request (method, headers, body, query
 *       string) to the plugin via {@link PluginRequestForwarder}, adding {@code X-User} and
 *       {@code X-Permissions} identity headers.</li>
 *   <li><strong>Return</strong> — the upstream response (status, headers, body) is returned
 *       to the client unmodified.</li>
 * </ol>
 *
 * <h3>Route example</h3>
 * <pre>
 *   GET /plugins/smart-doc/document-types
 *       → GET http://localhost:8042/api/document-types
 *
 *   POST /plugins/smart-doc/documents?personId=123&type=RX
 *       → POST http://localhost:8042/api/documents?personId=123&type=RX
 * </pre>
 *
 * <h3>Security</h3>
 * All plugin routes fall under the existing {@code .anyRequest().authenticated()} rule in
 * {@link org.isf.config.SecurityConfig} — the {@code JWTFilter} validates the bearer token
 * before this controller is reached. Plugin-specific permission checks are performed inside
 * this controller via {@link PluginAuthorizationChecker}.
 */
@RestController
@RequestMapping("/plugins")
@Tag(name = "Plugins", description = "Dynamic gateway to registered external plugin services")
@SecurityRequirement(name = "bearerAuth")
public class PluginProxyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginProxyController.class);

	/** The URL prefix that will be stripped before forwarding to the upstream plugin. */
	private static final String PLUGINS_PREFIX = "/plugins/";

	private final PluginRegistry pluginRegistry;
	private final PluginAuthorizationChecker authorizationChecker;
	private final PluginRequestForwarder requestForwarder;

	public PluginProxyController(
			PluginRegistry pluginRegistry,
			PluginAuthorizationChecker authorizationChecker,
			PluginRequestForwarder requestForwarder) {
		this.pluginRegistry = pluginRegistry;
		this.authorizationChecker = authorizationChecker;
		this.requestForwarder = requestForwarder;
	}

	/**
	 * Catch-all handler for every HTTP method under {@code /plugins/{pluginId}/**}.
	 *
	 * @param request the original {@link HttpServletRequest}
	 * @return the upstream plugin's response, forwarded transparently
	 * @throws IOException if reading the request body fails
	 */
	@RequestMapping(value = "/{id}/**")
	@Operation(
			summary = "Proxy a request to an external plugin",
			description = "Resolves the plugin by ID, checks authorization, then forwards the request " +
					"to the plugin's upstream URL. The sub-path after /plugins/{pluginId} is appended to the " +
					"plugin base URL. All HTTP methods are supported.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Upstream response forwarded successfully"),
			@ApiResponse(responseCode = "403", description = "Authenticated user lacks required privileges for this plugin"),
			@ApiResponse(responseCode = "404", description = "Plugin not found or unavailable"),
			@ApiResponse(responseCode = "502", description = "Upstream plugin returned an unexpected error")
	})
	public ResponseEntity<byte[]> proxy(
			@Parameter(hidden = true) HttpServletRequest request) throws IOException {

		// 1. Extract plugin ID from the URI
		String pluginId = extractPluginId(request.getRequestURI());

		// 2. Resolve the plugin from the registry
		PluginDefinition plugin = pluginRegistry.find(pluginId)
				.orElseThrow(() -> new PluginNotFoundException(pluginId));

		// 3. Authorization check
		authorizationChecker.assertAccess(plugin);

		// 4. Extract sub-path (everything after /plugins/{pluginId})
		String subPath = extractSubPath(request.getRequestURI(), pluginId);

		// 5. Read request body (may be empty for GET/DELETE/HEAD)
		byte[] body = request.getInputStream().readAllBytes();

		// 6. Collect authentication for identity headers
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		LOGGER.debug("Routing [{}] /plugins/{}{} → {}{}",
				request.getMethod(), pluginId, subPath, plugin.url(), subPath);

		// 7. Forward and return the upstream response
		return requestForwarder.forward(
				plugin,
				subPath,
				request.getQueryString(),
				HttpMethod.valueOf(request.getMethod()),
				buildRequestHeaders(request),
				body,
				authentication.getName(),
				authentication.getAuthorities());
	}

	// -------------------------------------------------------------------------
	// Exception handlers (controller-scoped, highest precedence for plugin errors)
	// -------------------------------------------------------------------------

	@ExceptionHandler(PluginNotFoundException.class)
	public ResponseEntity<PluginErrorResponse> handlePluginNotFound(PluginNotFoundException ex) {
		LOGGER.warn("Plugin not found: {}", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new PluginErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(PluginAccessDeniedException.class)
	public ResponseEntity<PluginErrorResponse> handleAccessDenied(PluginAccessDeniedException ex) {
		LOGGER.warn("Plugin access denied: {}", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new PluginErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<byte[]> handleUpstreamError(RestClientResponseException ex) {
		LOGGER.warn("Upstream plugin error: HTTP {} — {}", ex.getStatusCode(), ex.getMessage());
		return ResponseEntity
				.status(ex.getStatusCode())
				.headers(ex.getResponseHeaders())
				.body(ex.getResponseBodyAsByteArray());
	}

	// -------------------------------------------------------------------------
	// Private helpers
	// -------------------------------------------------------------------------

	/**
	 * Extracts the plugin ID from a URI of the form {@code /plugins/{pluginId}/...}.
	 *
	 * @param requestUri the full request URI (e.g. {@code /plugins/smart-doc/documents})
	 * @return the plugin ID segment (e.g. {@code "smart-doc"})
	 */
	private String extractPluginId(String requestUri) {
		// Strip context-path prefix if present, then take the segment after /plugins/
		String afterPrefix = requestUri.substring(requestUri.indexOf(PLUGINS_PREFIX) + PLUGINS_PREFIX.length());
		int slashIndex = afterPrefix.indexOf('/');
		return slashIndex == -1 ? afterPrefix : afterPrefix.substring(0, slashIndex);
	}

	/**
	 * Extracts the sub-path after {@code /plugins/{pluginId}}.
	 * Returns {@code "/"} when there is no sub-path (request is to the plugin root).
	 *
	 * @param requestUri the full request URI
	 * @param pluginId   the resolved plugin ID
	 * @return the sub-path string, always starting with {@code /}
	 */
	private String extractSubPath(String requestUri, String pluginId) {
		String prefix = PLUGINS_PREFIX + pluginId;
		int prefixEnd = requestUri.indexOf(prefix) + prefix.length();
		String subPath = prefixEnd < requestUri.length() ? requestUri.substring(prefixEnd) : "";
		return subPath.isEmpty() ? "/" : subPath;
	}

	/**
	 * Copies all headers from the {@link HttpServletRequest} into an {@link HttpHeaders} map.
	 *
	 * @param request the incoming servlet request
	 * @return assembled {@link HttpHeaders}
	 */
	private HttpHeaders buildRequestHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		java.util.Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				java.util.Enumeration<String> values = request.getHeaders(name);
				while (values.hasMoreElements()) {
					headers.add(name, values.nextElement());
				}
			}
		}
		return headers;
	}

	// -------------------------------------------------------------------------
	// Nested error response DTO
	// -------------------------------------------------------------------------

	/**
	 * Minimal error payload returned by the plugin gateway for {@code 404} and {@code 403}
	 * responses. Kept as a nested class since it is exclusive to this controller.
	 */
	public record PluginErrorResponse(int status, String message) {
	}
}
