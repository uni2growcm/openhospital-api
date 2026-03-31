package org.isf.plugins.config;

import jakarta.validation.constraints.NotNull;

public record PluginBundle(
	@NotNull String label,
	@NotNull String manifest,
	@NotNull String type,
	@NotNull String location,
	String styles
) {
}
