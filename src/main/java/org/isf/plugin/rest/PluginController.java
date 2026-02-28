package org.isf.plugin.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.plugin.dto.PluginsManifestDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Plugins")
public class PluginController {

	@GetMapping(value = "/plugins")
	public ResponseEntity<PluginsManifestDTO> getPluginsManifest() {
		return ResponseEntity.ok(new PluginsManifestDTO());
	}

	@GetMapping(value = "/plugins/{id}")
	public ResponseEntity<Resource> getPluginResource(
		@PathVariable String id,
		@RequestParam String pluginId) {
		return ResponseEntity.ok()
							 .body(null);
	}
}
