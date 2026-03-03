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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.isf.OpenHospitalApiApplication;
import org.isf.plugins.config.PluginDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OpenHospitalApiApplication.class)
class PluginRegistryTest {


	@Autowired
	private PluginRegistry registry;

	private static final PluginDefinition SMART_DOC = new PluginDefinition(
			"smart-doc", "http://localhost:4000/api", "/health", List.of());

	private static final PluginDefinition REPORTS = new PluginDefinition(
			"reports", "http://localhost:5000", "/ping", List.of());

	@Test
	void registerSinglePlugin() {
		registry.register(Map.of("smart-doc", SMART_DOC));

		assertThat(registry.size()).isEqualTo(1);
		Optional<PluginDefinition> found = registry.find("smart-doc");
		assertThat(found).isPresent();
		assertThat(found.get().id()).isEqualTo("smart-doc");
		assertThat(found.get().url()).isEqualTo("http://localhost:4000/api");
	}

	@Test
	void registerMultiplePlugins() {
		registry.register(Map.of("smart-doc", SMART_DOC, "reports", REPORTS));

		assertThat(registry.size()).isEqualTo(2);
		assertThat(registry.find("smart-doc")).isPresent();
		assertThat(registry.find("reports")).isPresent();
	}

	@Test
	void findUnknownPluginReturnsEmpty() {
		registry.register(Map.of("smart-doc", SMART_DOC));

		assertThat(registry.find("no-such-plugin")).isEmpty();
	}

	@Test
	void allReturnsRegisteredDefinitions() {
		registry.register(Map.of("smart-doc", SMART_DOC, "reports", REPORTS));

		assertThat(registry.all())
				.hasSize(2)
				.containsExactlyInAnyOrder(SMART_DOC, REPORTS);
	}

	@Test
	void secondRegisterCallReplacesContents() {
		registry.register(Map.of("smart-doc", SMART_DOC));
		assertThat(registry.size()).isEqualTo(1);

		registry.register(Map.of("reports", REPORTS));
		assertThat(registry.size()).isEqualTo(1);
		assertThat(registry.find("smart-doc")).isEmpty();
		assertThat(registry.find("reports")).isPresent();
	}

	@Test
	void registerWithEmptyMapClearsRegistry() {
		registry.register(Map.of("smart-doc", SMART_DOC));
		registry.register(Map.of());

		assertThat(registry.size()).isZero();
		assertThat(registry.all()).isEmpty();
	}
}
