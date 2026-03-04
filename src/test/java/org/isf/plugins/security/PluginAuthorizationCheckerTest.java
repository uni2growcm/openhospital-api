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

import org.isf.OpenHospitalApiApplication;
import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.config.PluginPermission;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = OpenHospitalApiApplication.class)
class PluginAuthorizationCheckerTest {

	@Autowired
	private IPluginAuthorizationChecker checker;

	// -------------------------------------------------------------------------
	// assertAccess — open plugin (no permissions declared)
	// -------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "user")
	@DisplayName("Should grant access to any authenticated user when plugin declares no permissions")
	void shouldGrantAccessWhenNoPermissionsDeclared() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", List.of());

		// Should not throw — no permissions means any authenticated user is allowed.
		checker.assertAccess(plugin);
	}

	@Test
	@WithMockUser(username = "user")
	@DisplayName("Should grant access to any authenticated user when plugin permissions is null")
	void shouldGrantAccessWhenPermissionsIsNull() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", null);

		checker.assertAccess(plugin);
	}

	// -------------------------------------------------------------------------
	// assertAccess — plugin with permission requirements
	// -------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "user", authorities = {"smart-doc.read", "smart-doc.write"})
	@DisplayName("Should grant access when user has at least one matching privilege across any permission group")
	void shouldGrantAccessWhenUserHasMatchingPrivilege() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));

		// Should not throw — user has smart-doc.read which is in the privilege list.
		checker.assertAccess(plugin);
	}

	@Test
	@WithMockUser(username = "user", authorities = {"smart-doc.read", "smart-doc.write"})
	@DisplayName("Should grant access when user has a matching privilege in at least one of multiple permission groups")
	void shouldGrantAccessWhenUserMatchesOneOfMultiplePermissionGroups() {
		PluginPermission adminPerm = new PluginPermission("admin", List.of("smart-doc.write"));
		PluginPermission viewerPerm = new PluginPermission("viewer", List.of("smart-doc.read"));
		PluginDefinition plugin = new PluginDefinition(
			"smart-doc", "http://localhost:4000", "/health", List.of(adminPerm, viewerPerm));

		checker.assertAccess(plugin);
	}

	@Test
	@WithMockUser(username = "user", authorities = {"smart-doc.create"})
	@DisplayName("Should deny access when user has authorities but none match any required privilege")
	void shouldDenyAccessWhenUserHasNoMatchingPrivilege() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));

		assertThatThrownBy(() -> checker.assertAccess(plugin))
			.isInstanceOf(PluginAccessDeniedException.class)
			.hasMessageContaining("user")
			.hasMessageContaining("smart-doc");
	}

	@Test
	@WithMockUser(username = "user", authorities = {})
	@DisplayName("Should deny access when user has no authorities at all")
	void shouldDenyAccessWhenUserHasNoAuthorities() {
		PluginPermission perm = new PluginPermission("admin", List.of("smart-doc.read"));
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of(perm));

		assertThatThrownBy(() -> checker.assertAccess(plugin))
			.isInstanceOf(PluginAccessDeniedException.class);
	}

	// -------------------------------------------------------------------------
	// assertAccess — unauthenticated / no principal
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Should throw IllegalStateException when there is no authentication in the security context")
	void shouldThrowIllegalStateWhenNoAuthentication() {
		SecurityContextHolder.clearContext();
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of());

		assertThatThrownBy(() -> checker.assertAccess(plugin))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("No authenticated principal");
	}

	@Test
	@DisplayName("Should throw IllegalStateException when authentication is present but not authenticated")
	void shouldThrowIllegalStateWhenPrincipalIsNotAuthenticated() {
		PluginDefinition plugin = new PluginDefinition("smart-doc", "http://localhost:4000", "/health", List.of());

		assertThatThrownBy(() -> checker.assertAccess(plugin))
			.isInstanceOf(IllegalStateException.class);
	}

	// -------------------------------------------------------------------------
	// requiredPrivileges
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Should return the flat set of all declared privileges across all permission groups")
	void shouldReturnAllDeclaredPrivileges() {
		PluginPermission adminPerm = new PluginPermission("admin", List.of("smart-doc.read", "smart-doc.write"));
		PluginPermission viewerPerm = new PluginPermission("viewer", List.of("smart-doc.read", "smart-doc.export"));
		PluginDefinition plugin = new PluginDefinition(
			"smart-doc", "http://localhost:4000", "/health", List.of(adminPerm, viewerPerm));

		Set<String> required = checker.requiredPrivileges(plugin);

		assertThat(required).containsExactlyInAnyOrder("smart-doc.read", "smart-doc.write", "smart-doc.export");
	}

	@Test
	@DisplayName("Should return an empty set when plugin permissions is null")
	void shouldReturnEmptySetWhenNoPermissionsDeclared() {
		PluginDefinition plugin = new PluginDefinition("open-plugin", "http://localhost:9000", "/health", null);

		assertThat(checker.requiredPrivileges(plugin)).isEmpty();
	}
}
