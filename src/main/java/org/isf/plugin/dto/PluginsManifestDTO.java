package org.isf.plugin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class PluginsManifestDTO {

	@Schema(description = "List of available plugins.")
	private List<PluginEntryDTO> plugins;

	public List<PluginEntryDTO> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<PluginEntryDTO> plugins) {
		this.plugins = plugins;
	}
}
