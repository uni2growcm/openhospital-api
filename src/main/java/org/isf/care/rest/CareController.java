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
package org.isf.care.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.care.dto.CareDTO;
import org.isf.care.mapper.CareMapper;
import org.isf.cares.manager.CareManager;
import org.isf.cares.model.Care;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.User;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Cares")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CareController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CareController.class);

	private final PatientBrowserManager patientBrowserManager;
	private final CareMapper careMapper;
	private final CareManager careManager;
	private final UserBrowsingManager userBrowsingManager;

	public CareController(PatientBrowserManager patientBrowserManager, CareMapper careMapper, CareManager careManager, UserBrowsingManager userBrowsingManager) {
		this.patientBrowserManager = patientBrowserManager;
		this.careMapper = careMapper;
		this.careManager = careManager;
		this.userBrowsingManager = userBrowsingManager;
	}

	/**
	 * Inserts a new care.
	 *
	 * @param careDTO - the care to insert.
	 * @return {@code true} if the care has been successfully inserted, {@code false} otherwise.
	 * @throws OHServiceException
	 */
	@PostMapping("/cares")
	public ResponseEntity<CareDTO> newConditioning(@RequestBody CareDTO careDTO) throws OHServiceException {
		LOGGER.info("Create care");
		if (careDTO.getPatient() != null) {
			Patient patient = patientBrowserManager.getPatientById(careDTO.getPatient().getCode());
			if (patient == null) {
				throw new OHAPIException(new OHExceptionMessage("Patient not found."), HttpStatus.NOT_FOUND);
			}
		} else {
			throw new OHAPIException(new OHExceptionMessage("Patient is required."), HttpStatus.BAD_REQUEST);
		}

		if (careDTO.getTeam() == null) {
			throw new OHAPIException(new OHExceptionMessage("Team is required."), HttpStatus.BAD_REQUEST);
		} else {
			for (String user: careDTO.getTeam()) {
				User userFound = userBrowsingManager.getUserByName(user);
				if (userFound == null) {
					throw new OHAPIException(new OHExceptionMessage("User not found."), HttpStatus.NOT_FOUND);
				}
			}
		}

		Care newCare = careMapper.map2Model(careDTO);
		Care savedCare = careManager.saveCare(newCare);
		if (savedCare == null) {
			throw new OHAPIException(new OHExceptionMessage("Care not save."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(careMapper.map2DTO(savedCare));
	}

	/**
	 * Retrieve all existing {@link Care} by patient code.
	 *
	 * @param patientCode - the patient code.
	 * @return a list of {@link CareDTO} objects, empty if none found
	 * @throws OHServiceException When the retrieval operation fails
	 */
	@GetMapping("/cares/{patientCode}")
	public ResponseEntity<List<CareDTO>> getCareByPatientCode(@PathVariable("patientCode") int patientCode) throws OHServiceException {
		LOGGER.info("get care by patient code : {}", patientCode);

		List<Care> careList = careManager.getCaresByPatient(patientCode);
		if (careList == null) {
			throw new OHAPIException(new OHExceptionMessage("Care not found."), HttpStatus.NOT_FOUND);
		}
		List<CareDTO> careDTOS = careList.stream()
			.map(careMapper::map2DTO)
			.toList();
		return ResponseEntity.ok(careDTOS);
	}

	/**
	 * Update an existing {@link Care}.
	 *
	 * @param id - the care id.
	 * @param updateCareDTO - Care data to update.
	 * @return updated {@link CareDTO} if successful, or error message if not found or invalid
	 * @throws OHServiceException When the update operation fails
	 */
	@PutMapping("/cares/{id}")
	public ResponseEntity<CareDTO> updateCare(@PathVariable("id") int id, @RequestBody CareDTO updateCareDTO)
		throws OHServiceException {
		LOGGER.info("Update care with id : {}", id);
		if (id != updateCareDTO.getId()) {
			throw new OHAPIException(new OHExceptionMessage("Care does not match."), HttpStatus.BAD_REQUEST);
		}
		Care old = careManager.getCareById(id);
		if (old == null) {
			throw new OHAPIException(new OHExceptionMessage("Care not found with id "+ id), HttpStatus.NOT_FOUND);
		}

		if (updateCareDTO.getPatient() != null) {
			Patient patient = careManager.getCareById(updateCareDTO.getId()).getPatient();
			if (patient == null) {
				throw new OHAPIException(new OHExceptionMessage("Patient not found."), HttpStatus.NOT_FOUND);
			}
		} else {
			throw new OHAPIException(new OHExceptionMessage("Patient is required."), HttpStatus.BAD_REQUEST);
		}

		if (updateCareDTO.getTeam() == null) {
			throw new OHAPIException(new OHExceptionMessage("Team is required."), HttpStatus.BAD_REQUEST);
		} else {
			for (String user: updateCareDTO.getTeam()) {
				User userFound = userBrowsingManager.getUserByName(user);
				if (userFound == null) {
					throw new OHAPIException(new OHExceptionMessage("User not found."), HttpStatus.NOT_FOUND);
				}
			}
		}

		Care updateCare = careMapper.map2Model(updateCareDTO);
		Care updatedCare = careManager.updateCare(updateCare);
		if (updatedCare == null) {
			throw new OHAPIException(new OHExceptionMessage("Care not updated."), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(careMapper.map2DTO(updatedCare));
	}
}
