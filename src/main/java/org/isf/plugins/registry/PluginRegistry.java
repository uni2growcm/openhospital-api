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
package org.isf.plugins.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.isf.plugins.config.PluginDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Runtime store of <em>healthy</em> plugin definitions.
 *
 * <p>The registry is populated once during application startup by
 * {@link org.isf.plugins.health.PluginHealthChecker}: only plugins whose health endpoint
 * responded successfully are registered here. Plugins that failed their health check are
 * silently excluded — their routes will return {@code 404 Not Found} at request time.</p>
 *
 * <p>After startup the registry is effectively immutable. It is thread-safe for concurrent
 * reads (backed by an unmodifiable view of a {@link LinkedHashMap}) and requires no
 * synchronization during normal request processing.</p>
 */
@Component
public class PluginRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginRegistry.class);

	/** Insertion-ordered map of pluginId → definition for all healthy plugins. */
	private Map<String, PluginDefinition> registry = Collections.emptyMap();

	/**
	 * Replaces the current registry contents.
	 * Called exactly once by {@link org.isf.plugins.health.PluginHealthChecker} after
	 * startup health checks complete. Subsequent calls (e.g. in tests) are safe.
	 *
	 * @param definitions map of pluginId → {@link PluginDefinition} for healthy plugins
	 */
	public void register(Map<String, PluginDefinition> definitions) {
		this.registry = Collections.unmodifiableMap(new LinkedHashMap<>(definitions));
		LOGGER.info("Plugin registry initialized with {} active plugin(s): {}", registry.size(), registry.keySet());
	}

	/**
	 * Looks up a healthy plugin by its ID.
	 *
	 * @param pluginId the plugin identifier (e.g. {@code "smart-doc"})
	 * @return an {@link Optional} containing the {@link PluginDefinition}, or empty if not found
	 */
	public Optional<PluginDefinition> find(String pluginId) {
		return Optional.ofNullable(registry.get(pluginId));
	}

	/**
	 * Returns an unmodifiable view of all registered (healthy) plugin definitions.
	 *
	 * @return all active plugin definitions; never {@code null}
	 */
	public Collection<PluginDefinition> all() {
		return registry.values();
	}

	/**
	 * Returns the number of currently registered (healthy) plugins.
	 *
	 * @return active plugin count
	 */
	public int size() {
		return registry.size();
	}
}
