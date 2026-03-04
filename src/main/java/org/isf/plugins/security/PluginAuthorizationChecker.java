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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Steve Tsala
 */
@Component
public class PluginAuthorizationChecker implements IPluginAuthorizationChecker {

	private final IAuthenticationSupplier authenticationSupplier;

	public PluginAuthorizationChecker(IAuthenticationSupplier authenticationSupplier) {
		this.authenticationSupplier = authenticationSupplier;
	}

	@Override
	public void assertAccess(PluginDefinition plugin) {
		Authentication authentication = authenticationSupplier.get();

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

	@Override
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
