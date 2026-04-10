package org.isf.integrations.labbook.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.isf.integrations.labbook.models.PatientAnalysisResponse;
import org.isf.integrations.labbook.services.PatientSyncService;
import org.isf.utils.exception.OHException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Labbook")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/labbook", produces = MediaType.APPLICATION_JSON_VALUE)
public class LabBookPatientController {

	private final PatientSyncService patientSyncService;

	public LabBookPatientController(PatientSyncService patientSyncService) {
		this.patientSyncService = patientSyncService;
	}

	@GetMapping("/patients/{id}/analysis")
	@PreAuthorize("hasRole('ADMIN') or hasAuthority('exams.read')")
	public List<PatientAnalysisResponse> getPatientAnalysis(@PathVariable Integer id) throws OHException {
		return patientSyncService.getPatientAnalysis(id);
	}
}