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
package org.isf.plugins.config;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Represents the access-control entry for a single role within a plugin definition.
 *
 * <p>Each {@code PluginPermission} binds a logical role label (e.g. {@code "admin"}) to
 * the set of fine-grained privilege strings (e.g. {@code "smart-doc.read"}) that a user
 * must hold in order to access the plugin's routes. The role label is purely descriptive;
 * authorization is enforced exclusively via {@link #privileges()}, which are matched against
 * the {@code GrantedAuthority} list carried in the user's JWT.</p>
 *
 * <p>Example YAML fragment:</p>
 * <pre>{@code
 * permissions:
 *   - role: admin
 *     privileges:
 *       - smart-doc.read
 *       - smart-doc.write
 * }</pre>
 *
 * @param role       a human-readable role label (e.g. {@code "admin"}, {@code "user"});
 *                   not used for programmatic access control — see {@link #privileges()}
 * @param privileges fine-grained authority strings; defaults to an empty list if omitted in YAML
 * @author Steve Tsala
 */
public record PluginPermission(
	String role,
	@DefaultValue List<String> privileges) {
}
