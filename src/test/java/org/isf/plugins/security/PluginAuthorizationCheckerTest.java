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
package org.isf.plugins.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.config.PluginPermission;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class PluginAuthorizationCheckerTest {

	@Mock
	private Authentication authentication;

	private PluginAuthorizationChecker checker;
	private AutoCloseable closeable;

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		checker = new PluginAuthorizationChecker();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getName()).thenReturn("testuser");
	}

	@AfterEach
	void tearDown() throws Exception {
		SecurityContextHolder.clearContext();
		closeable.close();
	}

	// -------------------------------------------------------------------------
	// assertAccess — open plugin (no permissions declared)
	// -------------------------------------------------------------------------

	@Test
	void accessGranted_noPermissionsDeclared() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", List.of());
		withAuthorities(List.of());  // user has no authorities at all

		// Should not throw — no permissions means any authenticated user is allowed.
		checker.assertAccess(plugin);
	}

	@Test
	void accessGranted_nullPermissions() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", null);
		withAuthorities(List.of());

		checker.assertAccess(plugin);
	}

	// -------------------------------------------------------------------------
	// assertAccess — plugin with permission requirements
	// -------------------------------------------------------------------------

	@Test
	void accessGranted_userHasMatchingPrivilege() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));
		withAuthorities(List.of("smart-doc.read", "patients.read"));

		// Should not throw — user has smart-doc.read which is in the privilege list.
		checker.assertAccess(plugin);
	}

	@Test
	void accessGranted_userHasOneOfMultiplePermissionGroups() {
		PluginPermission adminPerm = new PluginPermission("admin", List.of("smart-doc.write"));
		PluginPermission viewerPerm = new PluginPermission("viewer", List.of("smart-doc.read"));
		PluginDefinition plugin = new PluginDefinition(
				"smart-doc", "http://localhost:4000", "/health", List.of(adminPerm, viewerPerm));
		withAuthorities(List.of("smart-doc.read"));  // matches the viewer group only

		checker.assertAccess(plugin);
	}

	@Test
	void accessDenied_userHasNoMatchingPrivilege() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));
		withAuthorities(List.of("patients.read", "wards.read"));

		assertThatThrownBy(() -> checker.assertAccess(plugin))
				.isInstanceOf(PluginAccessDeniedException.class)
				.hasMessageContaining("testuser")
				.hasMessageContaining("smart-doc");
	}

	@Test
	void accessDenied_userHasNoAuthoritiesAtAll() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));
		withAuthorities(List.of());

		assertThatThrownBy(() -> checker.assertAccess(plugin))
				.isInstanceOf(PluginAccessDeniedException.class);
	}

	// -------------------------------------------------------------------------
	// assertAccess — unauthenticated / no principal
	// -------------------------------------------------------------------------

	@Test
	void throwsIllegalState_whenNoAuthentication() {
		SecurityContextHolder.clearContext();
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of());

		assertThatThrownBy(() -> checker.assertAccess(plugin))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("No authenticated principal");
	}

	@Test
	void throwsIllegalState_whenNotAuthenticated() {
		when(authentication.isAuthenticated()).thenReturn(false);
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of());

		assertThatThrownBy(() -> checker.assertAccess(plugin))
				.isInstanceOf(IllegalStateException.class);
	}

	// -------------------------------------------------------------------------
	// requiredPrivileges
	// -------------------------------------------------------------------------

	@Test
	void requiredPrivileges_returnsAllDeclaredPrivileges() {
		PluginPermission adminPerm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginPermission viewerPerm = new PluginPermission("viewer", List.of("smart-doc.read", "smart-doc.export"));
		PluginDefinition plugin = new PluginDefinition(
				"smart-doc", "http://localhost:4000", "/health", List.of(adminPerm, viewerPerm));

		Set<String> required = checker.requiredPrivileges(plugin);

		assertThat(required).containsExactlyInAnyOrder("smart-doc.read", "smart-doc.write", "smart-doc.export");
	}

	@Test
	void requiredPrivileges_returnsEmptySetWhenNoPermissions() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", null);

		assertThat(checker.requiredPrivileges(plugin)).isEmpty();
	}

	// -------------------------------------------------------------------------
	// Helpers
	// -------------------------------------------------------------------------

	private void withAuthorities(List<String> authorityStrings) {
		Collection<GrantedAuthority> authorities = authorityStrings.stream()
				.map(a -> (GrantedAuthority) new SimpleGrantedAuthority(a))
				.toList();
		when(authentication.getAuthorities()).thenAnswer(inv -> authorities);
	}
}
