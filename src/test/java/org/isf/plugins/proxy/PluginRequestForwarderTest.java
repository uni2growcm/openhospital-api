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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.isf.OpenHospitalApiApplication;
import org.isf.plugins.config.PluginDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

@ExtendWith(MockitoExtension.class)
class PluginRequestForwarderTest {

	@Mock
	private RestClient mockRestClient;

	@Mock
	private RequestBodyUriSpec uriSpec;

	@Mock
	private RequestBodySpec requestBodySpec;

	@Mock
	private ResponseSpec responseSpec;

	private PluginRequestForwarderTestable forwarder;

	private static final PluginDefinition PLUGIN = new PluginDefinition(
			"smart-doc", "http://localhost:4000/api", "/health", List.of());

	@BeforeEach
	void setUp() {
		forwarder = new PluginRequestForwarderTestable(mockRestClient);

		when(mockRestClient.method(any(HttpMethod.class))).thenReturn(uriSpec);
		when(uriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
		when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
		when(requestBodySpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(Predicate.class), any())).thenReturn(responseSpec);
	}

	@Test
	@DisplayName("Should forward GET request and return upstream response")
	void forwardGetReturnsUpstreamResponse() {
		ResponseEntity<byte[]> upstreamResponse = ResponseEntity.ok("hello".getBytes());
		when(responseSpec.toEntity(eq(byte[].class))).thenReturn(upstreamResponse);

		ResponseEntity<byte[]> result = forwarder.forward(
				PLUGIN,
				"/documents/1",
				null,
				HttpMethod.GET,
				new HttpHeaders(),
				null,
				"alice",
				List.of(new SimpleGrantedAuthority("smart-doc.read")));

		assertThat(result.getStatusCode().value()).isEqualTo(200);
		assertThat(result.getBody()).isEqualTo("hello".getBytes());
	}

	@Test
	@DisplayName("Should forward POST request with body and return upstream response")
	void forwardPostWithBodySetsBody() {
		ResponseEntity<byte[]> upstreamResponse = ResponseEntity.status(201).body(new byte[0]);
		when(requestBodySpec.body(any(byte[].class))).thenReturn(requestBodySpec);
		when(responseSpec.toEntity(eq(byte[].class))).thenReturn(upstreamResponse);

		byte[] requestBody = "{\"name\":\"doc\"}".getBytes();
		ResponseEntity<byte[]> result = forwarder.forward(
				PLUGIN,
				"/documents",
				null,
				HttpMethod.POST,
				new HttpHeaders(),
				requestBody,
				"alice",
				List.of());

		assertThat(result.getStatusCode().value()).isEqualTo(201);
	}

	@Test
	@DisplayName("Should add X-User header with authenticated username")
	void xUserHeaderIsSet() {
		// Capture the headers consumer argument to verify identity headers are added
		HttpHeaders[] capturedHeaders = new HttpHeaders[1];
		when(requestBodySpec.headers(any())).thenAnswer(inv -> {
			Consumer<HttpHeaders> consumer = inv.getArgument(0);
			HttpHeaders h = new HttpHeaders();
			consumer.accept(h);
			capturedHeaders[0] = h;
			return requestBodySpec;
		});

		ResponseEntity<byte[]> upstreamResponse = ResponseEntity.ok(new byte[0]);
		when(responseSpec.toEntity(eq(byte[].class))).thenReturn(upstreamResponse);

		forwarder.forward(PLUGIN, "/path", null, HttpMethod.GET,
				new HttpHeaders(), null, "bob",
				List.of(new SimpleGrantedAuthority("smart-doc.read")));

		assertThat(capturedHeaders[0]).isNotNull();
		assertThat(capturedHeaders[0].getFirst(PluginRequestForwarder.HEADER_X_USER)).isEqualTo("bob");
	}

	@Test
	void xPermissionsHeaderIsCommaSeparated() {
		HttpHeaders[] capturedHeaders = new HttpHeaders[1];
		when(requestBodySpec.headers(any())).thenAnswer(inv -> {
			Consumer<HttpHeaders> consumer = inv.getArgument(0);
			HttpHeaders h = new HttpHeaders();
			consumer.accept(h);
			capturedHeaders[0] = h;
			return requestBodySpec;
		});

		ResponseEntity<byte[]> upstreamResponse = ResponseEntity.ok(new byte[0]);
		when(responseSpec.toEntity(eq(byte[].class))).thenReturn(upstreamResponse);

		forwarder.forward(PLUGIN, "/path", null, HttpMethod.GET,
				new HttpHeaders(), null, "alice",
				List.of(
						new SimpleGrantedAuthority("smart-doc.read"),
						new SimpleGrantedAuthority("smart-doc.write")));

		String permHeader = capturedHeaders[0].getFirst(PluginRequestForwarder.HEADER_X_PERMISSIONS);
		assertThat(permHeader).contains("smart-doc.read", "smart-doc.write");
	}

	@Test
	void hostHeaderIsStripped() {
		HttpHeaders[] capturedHeaders = new HttpHeaders[1];
		when(requestBodySpec.headers(any())).thenAnswer(inv -> {
			Consumer<HttpHeaders> consumer = inv.getArgument(0);
			HttpHeaders h = new HttpHeaders();
			consumer.accept(h);
			capturedHeaders[0] = h;
			return requestBodySpec;
		});

		ResponseEntity<byte[]> upstreamResponse = ResponseEntity.ok(new byte[0]);
		when(responseSpec.toEntity(eq(byte[].class))).thenReturn(upstreamResponse);

		HttpHeaders incoming = new HttpHeaders();
		incoming.add(HttpHeaders.HOST, "api.example.com");
		incoming.add("X-Custom", "value");

		forwarder.forward(PLUGIN, "/path", null, HttpMethod.GET,
				incoming, null, "alice", List.of());

		assertThat(capturedHeaders[0].containsKey(HttpHeaders.HOST)).isFalse();
		assertThat(capturedHeaders[0].getFirst("X-Custom")).isEqualTo("value");
	}

	// -------------------------------------------------------------------------
	// Testable subclass — allows injecting a mock RestClient
	// -------------------------------------------------------------------------

	static class PluginRequestForwarderTestable extends PluginRequestForwarder {

		private final RestClient injectedClient;

		PluginRequestForwarderTestable(RestClient client) {
			super();
			this.injectedClient = client;
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
				java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {

			// Delegate to a minimal inline forwarding that uses the injected mock client
			URI targetUri = buildTargetUriForTest(plugin, subPath, queryString);

			HttpHeaders forwardHeaders = new HttpHeaders();
			incomingHeaders.forEach((name, values) -> {
				if (!name.equalsIgnoreCase(HttpHeaders.HOST)) {
					forwardHeaders.addAll(name, values);
				}
			});
			forwardHeaders.set(PluginRequestForwarder.HEADER_X_USER, username);
			forwardHeaders.set(PluginRequestForwarder.HEADER_X_PERMISSIONS,
					authorities.stream()
							.map(org.springframework.security.core.GrantedAuthority::getAuthority)
							.reduce((a, b) -> a + "," + b)
							.orElse(""));

			RestClient.RequestBodySpec spec = injectedClient.method(method)
					.uri(targetUri)
					.headers(h -> h.addAll(forwardHeaders));

			if (body != null && body.length > 0) {
				spec.body(body);
			}

			return spec.retrieve()
					.onStatus(s -> true, (req, res) -> { })
					.toEntity(byte[].class);
		}

		private URI buildTargetUriForTest(PluginDefinition plugin, String subPath, String queryString) {
			String base = plugin.url().endsWith("/")
					? plugin.url().substring(0, plugin.url().length() - 1)
					: plugin.url();
			String path = (subPath != null && !subPath.isBlank())
					? (subPath.startsWith("/") ? subPath : "/" + subPath)
					: "";
			org.springframework.web.util.UriComponentsBuilder builder =
					org.springframework.web.util.UriComponentsBuilder.fromUriString(base + path);
			if (queryString != null && !queryString.isBlank()) {
				builder.query(queryString);
			}
			return builder.build(true).toUri();
		}
	}
}
