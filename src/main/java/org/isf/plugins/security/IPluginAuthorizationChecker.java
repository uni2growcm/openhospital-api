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

import org.isf.plugins.config.PluginDefinition;
import org.isf.plugins.config.PluginPermission;
import org.isf.plugins.exception.PluginAccessDeniedException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

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
 * already validated the token and populated the security context. No changes to
 * {@link org.isf.config.SecurityConfig} are required.
 *
 * @author Steve Tsala
 */
public interface IPluginAuthorizationChecker {

	/**
	 * Asserts that the currently authenticated user has at least one privilege required
	 * by the given plugin.
	 *
	 * @param plugin the target plugin whose permission rules are evaluated
	 * @throws PluginAccessDeniedException if the user holds none of the required privileges
	 * @throws IllegalStateException       if there is no authenticated principal in the security context
	 */
	void assertAccess(PluginDefinition plugin);

	/**
	 * Returns the set of all required privileges across all permission entries of a plugin.
	 * Useful for diagnostic / documentation purposes.
	 *
	 * @param plugin the plugin definition
	 * @return flat set of all declared privilege strings
	 */
	Set<String> requiredPrivileges(PluginDefinition plugin);
}
