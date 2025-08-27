package org.isf.conditioning.rest;

import org.isf.conditioning.dto.ConditioningDTO;
import org.isf.conditioning.manager.ConditioningManager;
import org.isf.conditioning.mapper.ConditioningMapper;
import org.isf.conditioning.model.Conditioning;
import org.isf.utils.exception.OHServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/conditioning", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Conditioning")
public class ConditioningController {

	private final ConditioningManager conditioningManager;
	private final ConditioningMapper conditioningMapper;

	public ConditioningController(ConditioningManager conditioningManager,
								  ConditioningMapper conditioningMapper) {
		this.conditioningManager = conditioningManager;
		this.conditioningMapper = conditioningMapper;
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveConditioning(@RequestBody ConditioningDTO dto) {
		try {
			Conditioning conditioning = conditioningMapper.map2Model(dto);
			Conditioning saved = conditioningManager.saveConditioning(conditioning);
			return ResponseEntity.ok(conditioningMapper.map2DTO(saved));
		} catch (OHServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessages());
		}
	}
}
