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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.config.PluginPermission;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.isf.plugins.exception.PluginNotFoundException;
import org.isf.plugins.registry.PluginRegistry;
import org.isf.plugins.security.PluginAuthorizationChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientResponseException;

class PluginProxyControllerTest {

	@Mock
	private PluginRegistry pluginRegistry;

	@Mock
	private PluginAuthorizationChecker authorizationChecker;

	@Mock
	private PluginRequestForwarder requestForwarder;

	private MockMvc mockMvc;
	private AutoCloseable closeable;

	private static final PluginDefinition SMART_DOC = new PluginDefinition(
			"smart-doc",
			"http://localhost:4000/api",
			"/health",
			List.of(new PluginPermission("admin", List.of("smart-doc.read"))));

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(new PluginProxyController(pluginRegistry, authorizationChecker, requestForwarder))
				.build();

		// Populate a minimal authenticated principal so the controller can call
		// authentication.getName() and authentication.getAuthorities().
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				"alice", null, List.of(new SimpleGrantedAuthority("smart-doc.read")));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@AfterEach
	void tearDown() throws Exception {
		SecurityContextHolder.clearContext();
		closeable.close();
	}

	// -------------------------------------------------------------------------
	// Happy path
	// -------------------------------------------------------------------------

	@Test
	void proxyGet_returnsUpstreamResponse() throws Exception {
		when(pluginRegistry.find("smart-doc")).thenReturn(Optional.of(SMART_DOC));
		doNothing().when(authorizationChecker).assertAccess(SMART_DOC);
		when(requestForwarder.forward(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(ResponseEntity.ok("upstream-body".getBytes()));

		MvcResult result = mockMvc.perform(get("/plugins/smart-doc/documents/42"))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).isEqualTo("upstream-body");
	}

	@Test
	void proxyPost_forwardsBodyAndReturns201() throws Exception {
		when(pluginRegistry.find("smart-doc")).thenReturn(Optional.of(SMART_DOC));
		doNothing().when(authorizationChecker).assertAccess(SMART_DOC);
		when(requestForwarder.forward(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(new byte[0]));

		mockMvc.perform(post("/plugins/smart-doc/documents")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"title\":\"test\"}"))
				.andExpect(status().isCreated());
	}

	// -------------------------------------------------------------------------
	// 404 — plugin not found
	// -------------------------------------------------------------------------

	@Test
	void returns404_whenPluginNotFound() throws Exception {
		when(pluginRegistry.find("unknown")).thenReturn(Optional.empty());

		mockMvc.perform(get("/plugins/unknown/path"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("unknown")));
	}

	// -------------------------------------------------------------------------
	// 403 — access denied
	// -------------------------------------------------------------------------

	@Test
	void returns403_whenAccessDenied() throws Exception {
		when(pluginRegistry.find("smart-doc")).thenReturn(Optional.of(SMART_DOC));
		doThrow(new PluginAccessDeniedException("smart-doc", "alice"))
				.when(authorizationChecker).assertAccess(SMART_DOC);

		mockMvc.perform(get("/plugins/smart-doc/documents"))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.status").value(403))
				.andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("alice")));
	}

	// -------------------------------------------------------------------------
	// Upstream error passthrough
	// -------------------------------------------------------------------------

	@Test
	void passesThrough502_fromUpstream() throws Exception {
		when(pluginRegistry.find("smart-doc")).thenReturn(Optional.of(SMART_DOC));
		doNothing().when(authorizationChecker).assertAccess(SMART_DOC);

		RestClientResponseException upstream = new RestClientResponseException(
				"Bad Gateway", 502, "Bad Gateway",
				new org.springframework.http.HttpHeaders(),
				"upstream error".getBytes(), null);
		when(requestForwarder.forward(any(), any(), any(), any(), any(), any(), any(), any()))
				.thenThrow(upstream);

		mockMvc.perform(get("/plugins/smart-doc/documents"))
				.andExpect(status().isBadGateway());
	}

	// -------------------------------------------------------------------------
	// PluginErrorResponse record
	// -------------------------------------------------------------------------

	@Test
	void pluginErrorResponse_recordAccessors() {
		PluginProxyController.PluginErrorResponse response =
				new PluginProxyController.PluginErrorResponse(404, "not found");

		assertThat(response.status()).isEqualTo(404);
		assertThat(response.message()).isEqualTo("not found");
	}
}
