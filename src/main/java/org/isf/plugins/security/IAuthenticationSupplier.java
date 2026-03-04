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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Functional interface that resolves the current {@link Authentication} from the
 * security context.
 *
 * <p>Abstracting {@link SecurityContextHolder} access behind this interface allows
 * service and controller classes to avoid static calls to
 * {@code SecurityContextHolder.getContext().getAuthentication()}, making them fully
 * testable without requiring a real security context to be populated.</p>
 *
 * <p>The default Spring bean is registered in {@link PluginSecurityConfig} and simply
 * delegates to {@code SecurityContextHolder.getContext().getAuthentication()}.</p>
 *
 * @author Steve Tsala
 */
@FunctionalInterface
public interface IAuthenticationSupplier {

	/**
	 * Returns the {@link Authentication} currently stored in the security context.
	 *
	 * @return the current {@link Authentication}, or {@code null} if none is present
	 */
	Authentication get();
}
