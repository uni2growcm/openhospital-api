package org.isf.integrations.labbook.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.isf.integrations.labbook.dto.LabbookPatientHistoricDTO;
import org.isf.integrations.labbook.mapper.LabbookPatientHistoricMapper;
import org.isf.integrations.labbook.models.PatientHistoricResponse;
import org.isf.integrations.labbook.services.PatientSyncService;
import org.isf.utils.exception.OHException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Labbook")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/labbook", produces = MediaType.APPLICATION_JSON_VALUE)
public class LabBookPatientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LabBookPatientController.class);
	private final PatientSyncService patientSyncService;
	private final LabbookPatientHistoricMapper mapper;

	public LabBookPatientController(PatientSyncService patientSyncService, LabbookPatientHistoricMapper mapper) {
		this.patientSyncService = patientSyncService;
		this.mapper = mapper;
	}

	@GetMapping("/patients/{id}/analysis")
	public LabbookPatientHistoricDTO getPatientAnalysis(@PathVariable Integer id) throws OHException {
		LOGGER.info("Retrieving patient analysis");
		PatientHistoricResponse patientAnalysis = patientSyncService.getPatientAnalysis(id);
		LOGGER.info("Patient analysis retrieved successfully");
		return mapper.map2DTO(patientAnalysis);
	}
}