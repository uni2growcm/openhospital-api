package org.isf.plugin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PluginEntryDTO {

	@Schema(description = "Unique id of the plugin.", example = "plugin-001")
	private String id;

	@Schema(description = "Display name of the plugin.", example = "Sample plugin")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
