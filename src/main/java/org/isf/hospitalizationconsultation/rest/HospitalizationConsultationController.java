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
package org.isf.hospitalizationconsultation.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.isf.encounter.dto.EncounterDTO;
import org.isf.encounter.manager.EncounterBrowserManager;
import org.isf.encounter.model.Encounter;
import org.isf.hospitalizationconsultation.dto.HospitalizationConsultationDTO;
import org.isf.hospitalizationconsultation.manager.HospitalizationConsultationBrowserManager;
import org.isf.hospitalizationconsultation.mapper.HospitalizationConsultationMapper;
import org.isf.hospitalizationconsultation.model.HospitalizationConsultation;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.shared.pagination.Page;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.pagination.PagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Hospitalization Consultation")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class HospitalizationConsultationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HospitalizationConsultationController.class);

	private final HospitalizationConsultationBrowserManager hospitalizationConsultationBrowserManager;
	private final HospitalizationConsultationMapper hospitalizationConsultationMapper;
	private final EncounterBrowserManager encounterBrowserManager;

	public HospitalizationConsultationController(
		HospitalizationConsultationBrowserManager hospitalizationConsultationBrowserManager,
		HospitalizationConsultationMapper hospitalizationConsultationMapper,
		EncounterBrowserManager encounterBrowserManager
	) {
		this.hospitalizationConsultationBrowserManager = hospitalizationConsultationBrowserManager;
		this.hospitalizationConsultationMapper = hospitalizationConsultationMapper;
		this.encounterBrowserManager = encounterBrowserManager;
	}

	@PostMapping(value = "/hospitalizationconsultations")
	@ResponseStatus(HttpStatus.CREATED)
	public HospitalizationConsultationDTO createHospitalizationConsultation(@RequestBody HospitalizationConsultationDTO hospitalizationConsultationDTO) throws OHServiceException {
		if (hospitalizationConsultationDTO.getEncounter() == null || hospitalizationConsultationDTO.getEncounter().getId() == null) {
			throw new OHAPIException(new OHExceptionMessage("Encounter must not be null."));
		}
		
		LOGGER.info("Create hospitalization consultation for encounter {}", hospitalizationConsultationDTO.getEncounter().getId());

		Encounter encounter = encounterBrowserManager.getEncountersByCode(hospitalizationConsultationDTO.getEncounter().getCode());
		if (encounter == null) {
			throw new OHAPIException(new OHExceptionMessage("Encounter not found."));
		}

		HospitalizationConsultation hospitalizationConsultation = hospitalizationConsultationMapper.map2Model(hospitalizationConsultationDTO);
		hospitalizationConsultation.setEncounter(encounter);
		hospitalizationConsultation = hospitalizationConsultationBrowserManager.newHospitalizationConsultation(hospitalizationConsultation);
		
		if (hospitalizationConsultation == null) {
			throw new OHAPIException(new OHExceptionMessage("Failed to create hospitalization consultation"));
		}

		return hospitalizationConsultationMapper.map2DTO(hospitalizationConsultation);
	}

	@GetMapping("/hospitalizationconsultations")
	public List<HospitalizationConsultationDTO> getHospitalizationConsultations() throws OHServiceException {
		LOGGER.info("Get all hospitalization consultations");
		List<HospitalizationConsultation> hospitalizationConsultations = hospitalizationConsultationBrowserManager.getHospitalizationConsultations();
		return hospitalizationConsultationMapper.map2DTOList(hospitalizationConsultations);
	}

	@GetMapping("/hospitalizationconsultations/{id}")
	public HospitalizationConsultationDTO getHospitalizationConsultation(@PathVariable int id) throws OHServiceException {
		LOGGER.info("Get hospitalization consultation with id {}", id);
		HospitalizationConsultation hospitalizationConsultation = hospitalizationConsultationBrowserManager.getHospitalizationConsultation(id);
		if (hospitalizationConsultation == null) {
			throw new OHAPIException(new OHExceptionMessage("Hospitalization consultation not found"), HttpStatus.NOT_FOUND);
		}
		return hospitalizationConsultationMapper.map2DTO(hospitalizationConsultation);
	}

	@GetMapping("/hospitalizationconsultations/encounter/{encounterCode}")
	public List<HospitalizationConsultationDTO> getHospitalizationConsultationsByEncounter(@PathVariable String encounterCode) throws OHServiceException {
		LOGGER.info("Get hospitalization consultations for encounter {}", encounterCode);
		Encounter encounter = encounterBrowserManager.getEncountersByCode(encounterCode);
		if (encounter == null) {
			throw new OHAPIException(new OHExceptionMessage("Encounter not found"), HttpStatus.NOT_FOUND);
		}
		List<HospitalizationConsultation> hospitalizationConsultations = hospitalizationConsultationBrowserManager.getHospitalizationConsultationsByEncounter(encounter);
		return hospitalizationConsultationMapper.map2DTOList(hospitalizationConsultations);
	}

	@GetMapping("/hospitalizationconsultations/daterange")
	public List<HospitalizationConsultationDTO> getHospitalizationConsultationsByDateRange(
		@RequestParam LocalDateTime dateFrom,
		@RequestParam LocalDateTime dateTo
	) throws OHServiceException {
		LOGGER.info("Get hospitalization consultations from {} to {}", dateFrom, dateTo);
		List<HospitalizationConsultation> hospitalizationConsultations = hospitalizationConsultationBrowserManager.getHospitalizationConsultationsByDateRange(dateFrom, dateTo);
		return hospitalizationConsultationMapper.map2DTOList(hospitalizationConsultations);
	}

	@GetMapping("/hospitalizationconsultations/pageable")
	public Page<HospitalizationConsultationDTO> getHospitalizationConsultationsPageable(
		@RequestParam int page,
		@RequestParam int size
	) throws OHServiceException {
		LOGGER.info("Get pageable hospitalization consultations page {} size {}", page, size);
		PagedResponse<HospitalizationConsultation> pagedResponse = hospitalizationConsultationBrowserManager.getHospitalizationConsultationsPageable(page, size);
		Page<HospitalizationConsultationDTO> pageDTO = new Page<>();
		pageDTO.setData(hospitalizationConsultationMapper.map2DTOList(pagedResponse.getData()));
		pageDTO.setPageInfo(hospitalizationConsultationMapper.setParameterPageInfo(pagedResponse.getPageInfo()));
		return pageDTO;
	}

	@GetMapping("/hospitalizationconsultations/encounter/{encounterCode}/pageable")
	public Page<HospitalizationConsultationDTO> getHospitalizationConsultationsByEncounterPageable(
		@PathVariable String encounterCode,
		@RequestParam int page,
		@RequestParam int size
	) throws OHServiceException {
		LOGGER.info("Get pageable hospitalization consultations for encounter {} page {} size {}", encounterCode, page, size);
		Encounter encounter = encounterBrowserManager.getEncountersByCode(encounterCode);
		if (encounter == null) {
			throw new OHAPIException(new OHExceptionMessage("Encounter not found"), HttpStatus.NOT_FOUND);
		}
		PagedResponse<HospitalizationConsultation> pagedResponse = hospitalizationConsultationBrowserManager.getHospitalizationConsultationsByEncounterPageable(encounter, page, size);
		Page<HospitalizationConsultationDTO> pageDTO = new Page<>();
		pageDTO.setData(hospitalizationConsultationMapper.map2DTOList(pagedResponse.getData()));
		pageDTO.setPageInfo(hospitalizationConsultationMapper.setParameterPageInfo(pagedResponse.getPageInfo()));
		return pageDTO;
	}

	@GetMapping("/hospitalizationconsultations/daterange/pageable")
	public Page<HospitalizationConsultationDTO> getHospitalizationConsultationsByDateRangePageable(
		@RequestParam LocalDateTime dateFrom,
		@RequestParam LocalDateTime dateTo,
		@RequestParam int page,
		@RequestParam int size
	) throws OHServiceException {
		LOGGER.info("Get pageable hospitalization consultations from {} to {} page {} size {}", dateFrom, dateTo, page, size);
		PagedResponse<HospitalizationConsultation> pagedResponse = hospitalizationConsultationBrowserManager.getHospitalizationConsultationsByDateRangePageable(dateFrom, dateTo, page, size);
		Page<HospitalizationConsultationDTO> pageDTO = new Page<>();
		pageDTO.setData(hospitalizationConsultationMapper.map2DTOList(pagedResponse.getData()));
		pageDTO.setPageInfo(hospitalizationConsultationMapper.setParameterPageInfo(pagedResponse.getPageInfo()));
		return pageDTO;
	}

	@PutMapping(value = "/hospitalizationconsultations/{id}")
	public HospitalizationConsultationDTO updateHospitalizationConsultation(
		@PathVariable int id,
		@RequestBody HospitalizationConsultationDTO hospitalizationConsultationDTO
	) throws OHServiceException {
		LOGGER.info("Update hospitalization consultation with id {}", id);
		
		HospitalizationConsultation existingConsultation = hospitalizationConsultationBrowserManager.getHospitalizationConsultation(id);
		if (existingConsultation == null) {
			throw new OHAPIException(new OHExceptionMessage("Hospitalization consultation not found"), HttpStatus.NOT_FOUND);
		}

		if (hospitalizationConsultationDTO.getEncounter() != null) {
			if (hospitalizationConsultationDTO.getEncounter().getCode() != null) {
				Encounter encounter = encounterBrowserManager.getEncountersByCode(hospitalizationConsultationDTO.getEncounter().getCode());
				if (encounter == null) {
					throw new OHAPIException(new OHExceptionMessage("Encounter not found"));
				}
				hospitalizationConsultationDTO.setEncounter(new EncounterDTO());
				hospitalizationConsultationDTO.getEncounter().setId(encounter.getId());
				hospitalizationConsultationDTO.getEncounter().setCode(encounter.getCode());
			}
		}

		hospitalizationConsultationDTO.setId(id);
		HospitalizationConsultation hospitalizationConsultation = hospitalizationConsultationMapper.map2Model(hospitalizationConsultationDTO);
		hospitalizationConsultation = hospitalizationConsultationBrowserManager.updateHospitalizationConsultation(hospitalizationConsultation);
		
		return hospitalizationConsultationMapper.map2DTO(hospitalizationConsultation);
	}

	@DeleteMapping(value = "/hospitalizationconsultations/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteHospitalizationConsultation(@PathVariable int id) throws OHServiceException {
		LOGGER.info("Delete hospitalization consultation with id {}", id);
		
		HospitalizationConsultation hospitalizationConsultation = hospitalizationConsultationBrowserManager.getHospitalizationConsultation(id);
		if (hospitalizationConsultation == null) {
			throw new OHAPIException(new OHExceptionMessage("Hospitalization consultation not found"), HttpStatus.NOT_FOUND);
		}
		
		hospitalizationConsultationBrowserManager.deleteHospitalizationConsultation(hospitalizationConsultation);
	}
}
