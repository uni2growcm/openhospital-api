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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.config.PluginPermission;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Enforces plugin-level access control by comparing the authenticated user's
 * {@link GrantedAuthority} list against the privilege declarations in a
 * {@link PluginDefinition}.
 *
 * <h3>Authorization model</h3>
 * A plugin declares one or more {@link PluginPermission} entries, each associating a
 * descriptive role label with a list of fine-grained privilege strings
 * (e.g. {@code "smart-doc.read"}). These privilege strings mirror the authority naming
 * convention used throughout the rest of the application (e.g. {@code "patients.read"}).
 *
 * <p>A user is <em>granted access</em> if their JWT carries <strong>at least one</strong>
 * authority that appears in <em>any</em> of the plugin's {@link PluginPermission#privileges()}
 * lists. This is an OR-across-permissions, OR-within-privileges model — consistent with
 * how {@code hasAnyAuthority()} is used in {@link org.isf.config.SecurityConfig}.</p>
 *
 * <h3>Integration</h3>
 * The check runs entirely within the controller layer, after the {@code JWTFilter} has
 * already validated the token and populated the {@link SecurityContextHolder}. No changes
 * to {@link org.isf.config.SecurityConfig} are required.
 */
@Component
public class PluginAuthorizationChecker {

	/**
	 * Asserts that the currently authenticated user has at least one privilege required
	 * by the given plugin.
	 *
	 * @param plugin the target plugin whose permission rules are evaluated
	 * @throws PluginAccessDeniedException if the user holds none of the required privileges
	 * @throws IllegalStateException if there is no authenticated principal in the security context
	 */
	public void assertAccess(PluginDefinition plugin) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("No authenticated principal found in SecurityContext");
		}

		List<PluginPermission> permissions = plugin.permissions();

		// A plugin with no permissions declared is accessible to any authenticated user.
		if (permissions == null || permissions.isEmpty()) {
			return;
		}

		Set<String> userAuthorities = extractAuthorities(authentication.getAuthorities());

		boolean hasAccess = permissions.stream()
				.map(PluginPermission::privileges)
				.flatMap(Collection::stream)
				.anyMatch(userAuthorities::contains);

		if (!hasAccess) {
			throw new PluginAccessDeniedException(plugin.id(), authentication.getName());
		}
	}

	/**
	 * Returns the set of all required privileges across all permission entries of a plugin.
	 * Useful for diagnostic / documentation purposes.
	 *
	 * @param plugin the plugin definition
	 * @return flat set of all declared privilege strings
	 */
	public Set<String> requiredPrivileges(PluginDefinition plugin) {
		if (plugin.permissions() == null) {
			return Set.of();
		}
		return plugin.permissions().stream()
				.map(PluginPermission::privileges)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	private Set<String> extractAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
		return grantedAuthorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}
}
