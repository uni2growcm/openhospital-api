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
package org.isf.plugins.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.isf.plugins.exception.PluginNotFoundException;
import org.isf.plugins.proxy.IPluginRequestForwarder;
import org.isf.plugins.registry.IPluginRegistry;
import org.isf.plugins.security.IAuthenticationSupplier;
import org.isf.plugins.security.IPluginAuthorizationChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.util.Collection;

/**
 * Controller for all {@code /plugins} routes.
 *
 * <h3>Endpoints</h3>
 * <ul>
 *   <li>{@code GET /plugins} — lists every plugin registered and healthy at startup.</li>
 *   <li>{@code ANY /plugins/{id}/**} — gateway that proxies a request to the upstream
 *       plugin identified by {@code id}.</li>
 * </ul>
 *
 * <h3>Proxy request lifecycle</h3>
 * <ol>
 *   <li><strong>Resolve plugin</strong> — looks up {@code pluginId} in the
 *       {@link IPluginRegistry}; returns {@code 404} if not found or unhealthy at startup.</li>
 *   <li><strong>Authorize</strong> — delegates to {@link IPluginAuthorizationChecker} to verify
 *       the authenticated user holds at least one required privilege; returns {@code 403}
 *       on failure.</li>
 *   <li><strong>Extract sub-path</strong> — strips the {@code /plugins/{id}} prefix from
 *       the request URI to obtain the upstream path segment.</li>
 *   <li><strong>Forward</strong> — proxies the full request (method, headers, body, query
 *       string) to the plugin via {@link IPluginRequestForwarder}, adding {@code X-User} and
 *       {@code X-Permissions} identity headers.</li>
 *   <li><strong>Return</strong> — the upstream response (status, headers, body) is returned
 *       to the client unmodified.</li>
 * </ol>
 *
 * <h3>Route examples</h3>
 * <pre>
 *   GET /plugins
 *       → [ { "id": "smart-doc", "url": "...", ... }, ... ]
 *
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
 * this controller via {@link IPluginAuthorizationChecker}.
 *
 * @author Steve Tsala
 */
@RestController
@RequestMapping("/plugins")
@Tag(name = "Plugins", description = "Dynamic gateway to registered external plugin services")
@SecurityRequirement(name = "bearerAuth")
public class PluginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);

	private final IPluginRegistry pluginRegistry;
	private final IPluginAuthorizationChecker authorizationChecker;
	private final IPluginRequestForwarder requestForwarder;
	private final IAuthenticationSupplier authenticationSupplier;

	public PluginController(IPluginRegistry pluginRegistry, IPluginAuthorizationChecker authorizationChecker, IPluginRequestForwarder requestForwarder, IAuthenticationSupplier authenticationSupplier) {
		this.pluginRegistry = pluginRegistry;
		this.authorizationChecker = authorizationChecker;
		this.requestForwarder = requestForwarder;
		this.authenticationSupplier = authenticationSupplier;
	}

	// -------------------------------------------------------------------------
	// GET /plugins — list all registered plugins
	// -------------------------------------------------------------------------

	/**
	 * Returns the full list of plugins that are registered and healthy.
	 * Any authenticated user may call this endpoint.
	 *
	 * @return {@code 200 OK} with a JSON array of {@link PluginDefinition}
	 */
	@GetMapping
	@Operation(summary = "List all registered plugins",
		description = "Returns all plugins that passed the startup health check and are currently available through the gateway.")
	@ApiResponse(responseCode = "200", description = "Plugin list returned successfully")
	public Collection<PluginDefinition> listPlugins() {
		return pluginRegistry.all();
	}

	// -------------------------------------------------------------------------
	// ANY /plugins/{id}/** — proxy handler
	// -------------------------------------------------------------------------

	/**
	 * Catch-all handler for every HTTP method under {@code /plugins/{id}/**}.
	 *
	 * @param request the original {@link HttpServletRequest}
	 * @return the upstream plugin's response, forwarded transparently
	 * @throws IOException if reading the request body fails
	 */
	@RequestMapping(value = "/{id}/{*path}")
	@Operation(summary = "Proxy a request to an external plugin",
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
		@Parameter(hidden = true) HttpServletRequest request,
		@PathVariable(required = false) String id,
		@PathVariable(required = false) String path) throws IOException {

		PluginDefinition plugin = pluginRegistry.find(id).orElseThrow(() -> new PluginNotFoundException(id));

		authorizationChecker.assertAccess(plugin);

		// Extract sub-path (everything after /plugins/{id})
		String subPath = String.format("/%s", path != null ? path : "");

		// Read request body (may be empty for GET/DELETE/HEAD)
		byte[] body = request.getInputStream().readAllBytes();

		Authentication authentication = authenticationSupplier.get();

		LOGGER.debug("Routing [{}] /plugins/{}{} → {}{}", request.getMethod(), id, subPath, plugin.url(), subPath);

		return requestForwarder.forward(plugin, subPath, request.getQueryString(), HttpMethod.valueOf(request.getMethod()), buildRequestHeaders(request), body, authentication.getName(), authentication.getAuthorities());
	}

	// -------------------------------------------------------------------------
	// Exception handlers (controller-scoped, highest precedence for plugin errors)
	// -------------------------------------------------------------------------

	@ExceptionHandler(PluginNotFoundException.class)
	public ResponseEntity<PluginErrorResponse> handlePluginNotFound(PluginNotFoundException ex) {
		LOGGER.warn("Plugin not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new PluginErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(PluginAccessDeniedException.class)
	public ResponseEntity<PluginErrorResponse> handleAccessDenied(PluginAccessDeniedException ex) {
		LOGGER.warn("Plugin access denied: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(new PluginErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<byte[]> handleUpstreamError(RestClientResponseException ex) {
		LOGGER.warn("Upstream plugin error: HTTP {} — {}", ex.getStatusCode(), ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsByteArray());
	}

	// -------------------------------------------------------------------------
	// Private helpers
	// -------------------------------------------------------------------------

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
