/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2025 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.examination.rest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.isf.encounter.manager.EncounterBrowserManager;
import org.isf.encounter.model.Encounter;
import org.isf.examination.dto.PatientExaminationDTO;
import org.isf.examination.manager.ExaminationBrowserManager;
import org.isf.examination.mapper.PatientExaminationMapper;
import org.isf.examination.model.Ausculation;
import org.isf.examination.model.Bowel;
import org.isf.examination.model.Diurese;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.ExaminationParameters;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.shared.pagination.Page;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.pagination.PagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Examinations")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExaminationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExaminationController.class);

	private final ExaminationBrowserManager examinationBrowserManager;

	private final PatientExaminationMapper patientExaminationMapper;

	private final PatientBrowserManager patientBrowserManager;

	private final EncounterBrowserManager encounterBrowserManager;

	public ExaminationController(
		ExaminationBrowserManager examinationBrowserManager,
		PatientExaminationMapper patientExaminationMapper,
		PatientBrowserManager patientBrowserManager, EncounterBrowserManager encounterBrowserManager
	) {
		this.examinationBrowserManager = examinationBrowserManager;
		this.patientExaminationMapper = patientExaminationMapper;
		this.patientBrowserManager = patientBrowserManager;
		this.encounterBrowserManager = encounterBrowserManager;
	}

	@PostMapping("/examinations")
	@ResponseStatus(HttpStatus.CREATED)
	public PatientExaminationDTO newPatientExamination(
		@RequestBody PatientExaminationDTO newPatientExamination
	) throws OHServiceException {
		Patient patient = patientBrowserManager.getPatientById(newPatientExamination.getPatientCode());
		if (patient == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient does not exist."), HttpStatus.NOT_FOUND);
		}

		validateExamination(newPatientExamination);

		Encounter encounter = encounterBrowserManager.getCurrentEncounter(newPatientExamination.getPatientCode());
		if (encounter == null) {
			throw new OHAPIException(new OHExceptionMessage("Encounter not found for patient code " + newPatientExamination.getPatientCode()), HttpStatus.NOT_FOUND);
		}

		boolean admissionExists = hasAdmissionExamination(encounter);

		if (admissionExists && "admission".equalsIgnoreCase(newPatientExamination.getPex_type())) {
			throw new OHAPIException(
				new OHExceptionMessage("An examination of type Admission already exists for this encounter."),
				HttpStatus.CONFLICT
			);
		}

		PatientExamination patientExamination = patientExaminationMapper.map2Model(newPatientExamination);
		patientExamination.setPatient(patient);
		patientExamination.setPex_date(newPatientExamination.getPex_date());

		PatientExamination savedExam = examinationBrowserManager.saveOrUpdate(patientExamination);

		return patientExaminationMapper.map2DTO(savedExam);
	}

	@PutMapping("/examinations/{id}")
	public PatientExaminationDTO updateExamination(
		@PathVariable Integer id, @RequestBody PatientExaminationDTO dto
	) throws OHServiceException {
		if (!Objects.equals(dto.getPex_ID(), id)) {
			throw new OHAPIException(new OHExceptionMessage("Patient examination ID mismatch."));
		}

		PatientExamination existingExamination = examinationBrowserManager.getByID(id);
		if (existingExamination == null) {
			throw new OHAPIException(
				new OHExceptionMessage("Patient examination not found."),
				HttpStatus.NOT_FOUND
			);
		}

		Patient patient = patientBrowserManager.getPatientById(dto.getPatientCode());
		if (patient == null) {
			throw new OHAPIException(
				new OHExceptionMessage("Patient does not exist."),
				HttpStatus.NOT_FOUND
			);
		}

		validateExamination(dto);

		Encounter encounter = encounterBrowserManager.getCurrentEncounter(dto.getPatientCode());
		if (encounter == null) {
			throw new OHAPIException(
				new OHExceptionMessage("Encounter not found for patient code " + dto.getPatientCode()),
				HttpStatus.NOT_FOUND
			);
		}

		if ("admission".equalsIgnoreCase(dto.getPex_type())) {
			boolean hasAdmission = hasAdmissionExamination(encounter);

			if (hasAdmission && !"admission".equalsIgnoreCase(existingExamination.getPex_type())) {
				throw new OHAPIException(
					new OHExceptionMessage("Another examination of type Admission already exists for this encounter."),
					HttpStatus.CONFLICT
				);
			}
		}

		PatientExamination patientExamination = patientExaminationMapper.map2Model(dto);
		patientExamination.setPatient(patient);
		patientExamination.setPex_date(dto.getPex_date());

		PatientExamination savedExamination = examinationBrowserManager.saveOrUpdate(patientExamination);
		return patientExaminationMapper.map2DTO(savedExamination);
	}

	@GetMapping("/examinations/defaultPatientExamination")
	public PatientExaminationDTO getDefaultPatientExamination(@RequestParam Integer patId) throws OHServiceException {
		Patient patient = patientBrowserManager.getPatientById(patId);
		if (patient == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient does not exist."), HttpStatus.NOT_FOUND);
		}

		PatientExamination patientExamination = examinationBrowserManager.getDefaultPatientExamination(patient);

		if (patientExamination == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient examination not found."), HttpStatus.NOT_FOUND);
		}

		return patientExaminationMapper.map2DTO(patientExamination);
	}

	@GetMapping("/examinations/fromLastPatientExamination/{id}")
	public PatientExaminationDTO getFromLastPatientExamination(
		@PathVariable Integer id
	) throws OHServiceException {
		PatientExamination lastPatientExamination = examinationBrowserManager.getByID(id);

		if (lastPatientExamination == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient examination not found."), HttpStatus.NOT_FOUND);
		}

		PatientExamination patientExamination = examinationBrowserManager.getFromLastPatientExamination(lastPatientExamination);

		return patientExaminationMapper.map2DTO(patientExamination);
	}

	@GetMapping("/examinations/{id}")
	public PatientExaminationDTO getByID(@PathVariable Integer id) throws OHServiceException {
		PatientExamination patientExamination = examinationBrowserManager.getByID(id);

		if (patientExamination == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient examination not found."), HttpStatus.NOT_FOUND);
		}

		return patientExaminationMapper.map2DTO(patientExamination);
	}

	@GetMapping("/examinations/lastByPatientId/{patId}")
	public PatientExaminationDTO getLastByPatientId(
		@PathVariable Integer patId
	) throws OHServiceException {
		PatientExamination patientExamination = examinationBrowserManager.getLastByPatID(patId);

		if (patientExamination == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient examination not found."), HttpStatus.NOT_FOUND);
		}

		return patientExaminationMapper.map2DTO(patientExamination);
	}

	@GetMapping("/examinations/lastNByPatId")
	public Page<PatientExaminationDTO> getLastNByPatID(
		@RequestParam Integer limit, @RequestParam Integer patId
	) throws OHServiceException {
		LOGGER.info("Get examinations limit: {}", limit);
		PagedResponse<PatientExamination> patientExaminationListPageable = examinationBrowserManager
			.getLastNByPatIDPageable(patId, limit);

		Page<PatientExaminationDTO> patientExaminationPageableDTO = new Page<>();
		List<PatientExaminationDTO> patientExaminationDTO = patientExaminationMapper.map2DTOList(
			patientExaminationListPageable.getData()
		);
		patientExaminationPageableDTO.setData(patientExaminationDTO);
		patientExaminationPageableDTO.setPageInfo(patientExaminationMapper.setParameterPageInfo(
			patientExaminationListPageable.getPageInfo())
		);

		return patientExaminationPageableDTO;
	}

	@GetMapping("/examinations/byPatientId/{patId}")
	public List<PatientExaminationDTO> getByPatientId(
		@PathVariable Integer patId
	) throws OHServiceException {
		return patientExaminationMapper.map2DTOList(examinationBrowserManager.getByPatID(patId));
	}

	public void validateExamination(PatientExaminationDTO newPatientExamination) throws OHServiceException {
		ExaminationParameters.initialize();

		Integer pex_height = newPatientExamination.getPex_height();
		Double pex_weight = newPatientExamination.getPex_weight();
		if (pex_height == null || pex_weight == null) {
			throw new OHAPIException(new OHExceptionMessage("The height and weight are compulsory"));
		}
		if (pex_height < ExaminationParameters.HEIGHT_MIN
			|| pex_height > ExaminationParameters.HEIGHT_MAX) {
			throw new OHAPIException(new OHExceptionMessage(
				"The height should be between " + ExaminationParameters.HEIGHT_MIN + " and " + ExaminationParameters.HEIGHT_MAX));
		}
		if (pex_weight < ExaminationParameters.WEIGHT_MIN || pex_weight > ExaminationParameters.WEIGHT_MAX) {
			throw new OHAPIException(new OHExceptionMessage(
				"The weight should be between" + ExaminationParameters.WEIGHT_MIN + " and " + ExaminationParameters.WEIGHT_MAX));
		}
		Integer pex_ap_min = newPatientExamination.getPex_ap_min();
		Integer pex_ap_max = newPatientExamination.getPex_ap_max();
		if (pex_ap_min == null && pex_ap_max != null) {
			throw new OHAPIException(new OHExceptionMessage("Malformed minimum/maximum blood pressure: minimum missing"));
		}
		if (pex_ap_min != null && pex_ap_max == null) {
			throw new OHAPIException(new OHExceptionMessage("Malformed minimum/maximum blood pressure: maximum missing"));
		}
		if (pex_ap_min != null && pex_ap_min > pex_ap_max) {
			throw new OHAPIException(new OHExceptionMessage("The minimum blood pressure must be lower than the maximum blood pressure"));
		}
		Diurese pex_diuresis_desc = newPatientExamination.getPex_diuresis_desc();
		if (pex_diuresis_desc != null) {
			Diurese.valueOf(pex_diuresis_desc.toString());
		}
		Bowel pex_bowel_desc = newPatientExamination.getPex_bowel_desc();
		if (pex_bowel_desc != null) {
			Bowel.valueOf(pex_bowel_desc.toString());
		}
		Ausculation pex_auscultation = newPatientExamination.getPex_auscultation();
		if (pex_auscultation != null) {
			Ausculation.valueOf(pex_auscultation.toString());
		}
	}

	public boolean hasAdmissionExamination(Encounter encounter) throws OHServiceException {

		List<PatientExamination> examinations = examinationBrowserManager.getPatientExaminationsForEncounter(encounter);

		if (examinations == null || examinations.isEmpty()) {
			return false;
		}

		return examinations.stream()
			.filter(Objects::nonNull)
			.anyMatch(exam -> "admission".equalsIgnoreCase(
				Optional.ofNullable(exam.getPex_type()).orElse("")
			));
	}
}
