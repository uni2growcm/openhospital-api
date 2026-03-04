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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Registers security-related beans used by the plugin subsystem.
 *
 * @author Steve Tsala
 */
@Configuration
public class PluginSecurityConfig {

	/**
	 * Provides the {@link IAuthenticationSupplier} bean that resolves the current
	 * {@link org.springframework.security.core.Authentication} from
	 * {@link SecurityContextHolder}.
	 *
	 * @return a lambda delegating to {@code SecurityContextHolder.getContext().getAuthentication()}
	 */
	@Bean
	public IAuthenticationSupplier authenticationSupplier() {
		return () -> SecurityContextHolder.getContext().getAuthentication();
	}
}
