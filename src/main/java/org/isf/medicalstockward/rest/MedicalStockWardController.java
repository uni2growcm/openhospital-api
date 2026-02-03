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
package org.isf.medicalstockward.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.isf.medical.dto.MedicalDTO;
import org.isf.medical.mapper.MedicalMapper;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.dto.MedicalWardDTO;
import org.isf.medicalstockward.dto.MedicalWardQuantityDTO;
import org.isf.medicalstockward.dto.MovementWardDTO;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.mapper.MedicalWardMapper;
import org.isf.medicalstockward.mapper.MovementWardMapper;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.mapper.WardMapper;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Medical Stock Ward")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MedicalStockWardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MedicalStockWardController.class);

	private final MedicalWardMapper medicalWardMapper;

	private final MovementWardMapper movementWardMapper;

	private final MovWardBrowserManager movWardBrowserManager;

	private final MedicalBrowsingManager medicalManager;

	private final WardBrowserManager wardManager;

	private final MedicalMapper medicalMapper;

	private final WardMapper wardMapper;

	public MedicalStockWardController(
		MedicalWardMapper medicalWardMapper,
		MovementWardMapper movementWardMapper,
		MovWardBrowserManager movWardBrowserManager,
		MedicalBrowsingManager medicalManager,
		WardBrowserManager wardManager,
		MedicalMapper medicalMapper,
		WardMapper wardMapper
	) {
		this.medicalWardMapper = medicalWardMapper;
		this.movementWardMapper = movementWardMapper;
		this.movWardBrowserManager = movWardBrowserManager;
		this.medicalManager = medicalManager;
		this.wardManager = wardManager;
		this.medicalMapper = medicalMapper;
		this.wardMapper = wardMapper;
	}

	/**
	 * Gets all the {@link MedicalWard}s associated to the specified ward.
	 *
	 * @param wardId the ward id.
	 * @return the retrieved {@link MedicalWard}s.
	 * @throws OHServiceException When failed to get ward medicals
	 */
	@GetMapping(value = "/medicalstockward/{ward_code}")
	public List<MedicalWardDTO> getMedicalsWard(@PathVariable("ward_code") String wardId) throws OHServiceException {
		// FIXME: provide provision for boolean ,false?
		List<MedicalWard> medWards = movWardBrowserManager.getMedicalsWard(wardId, true);

		return medicalWardMapper.map2DTOList(medWards);
	}

	/**
	 * Gets the current quantity for the specified {@link Medical} and specified {@link Ward}.
	 *
	 * @param wardId    - if {@code null} the quantity is counted for the whole hospital
	 * @param medicalId - the {@link Medical} to check.
	 * @return the total quantity.
	 * @throws OHServiceException if an error occurs retrieving the quantity.
	 */
	@GetMapping(value = "/medicalstockward/current/{ward_code}")
	public Integer getCurrentQuantityInWard(
		@PathVariable("ward_code") String wardId,
		@RequestParam("med_id") int medicalId
	) throws OHServiceException {
		Medical medical = medicalManager.getMedical(medicalId);
		if (medical == null) {
			throw new OHAPIException(new OHExceptionMessage("Medical not found."), HttpStatus.NOT_FOUND);
		}

		List<Ward> wards = wardManager.getWards().stream().filter(w -> w.getCode().equals(wardId)).toList();
		if (wards.isEmpty()) {
			throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
		}

		return movWardBrowserManager.getCurrentQuantityInWard(wards.get(0), medical);
	}

	/**
	 * Gets the current quantity for a specified {@link Medical} across wards that contain it.
	 *
	 * @param medicalId - the ID of the {@link Medical} to check.
	 * @return a {@link List} of {@link MedicalWardQuantityDTO} containing the quantity
	 * for each ward, including ward and medical details.
	 * @throws OHServiceException if an error occurs during quantity calculation.
	 */
	@GetMapping(value = "/medicalstockward/current-all-wards")
	public List<MedicalWardQuantityDTO> getCurrentQuantityInAllWards(
		@RequestParam("med_id") int medicalId
	) throws OHServiceException {

		Medical medical = medicalManager.getMedical(medicalId);
		if (medical == null) {
			throw new OHAPIException(new OHExceptionMessage("Medical not found."), HttpStatus.NOT_FOUND);
		}

		MedicalDTO medicalDTO = medicalMapper.map2DTO(medical);

		return Optional.ofNullable(wardManager.getWards()).orElse(Collections.emptyList()).stream()
			.filter(Objects::nonNull)
			.sorted(Comparator.comparing(Ward::getDescription, Comparator.nullsLast(Comparator.naturalOrder())))
			.map(ward -> {
				try {
					List<MedicalWard> medWards = movWardBrowserManager.getMedicalsWard(ward.getCode(), medicalId, true);
					if (medWards == null || medWards.isEmpty()) {
						return null;
					}

					int qty = 0;
					try {
						qty = movWardBrowserManager.getCurrentQuantityInWard(ward, medical);
					} catch (OHServiceException e) {
						LOGGER.error("Error processing ward: {}" + ward.getCode(), e);
					}

					MedicalWardQuantityDTO dto = new MedicalWardQuantityDTO();
					dto.setWard(wardMapper.map2DTO(ward));
					dto.setMedical(medicalDTO);
					dto.setQuantity(qty);
					return dto;

				} catch (OHServiceException e) {
					LOGGER.error("Error processing ward: " + ward.getCode(), e);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	/**
	 * Gets all the movement ward with the specified criteria.
	 *
	 * @param wardId   the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo   the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 * @throws OHServiceException When failed to get ward movements
	 */
	@GetMapping("/medicalstockward/movements/{ward_code}")
	public List<MovementWardDTO> getMovementWard(
		@PathVariable("ward_code") String wardId,
		@RequestParam(value = "from", required = false) LocalDate dateFrom,
		@RequestParam(value = "to", required = false) LocalDate dateTo
	) throws OHServiceException {
		LocalDateTime dateFromTime = null;
		if (dateFrom != null) {
			dateFromTime = dateFrom.atStartOfDay();
		}

		LocalDateTime dateToTime = null;
		if (dateTo != null) {
			dateToTime = dateTo.atStartOfDay();
		}

		return movementWardMapper.map2DTOList(movWardBrowserManager.getMovementWard(wardId, dateFromTime, dateToTime));
	}

	/**
	 * Gets all the movement wards with the specified criteria.
	 *
	 * @param idWardTo the target ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo   the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 * @throws OHServiceException When failed to get ward movements
	 */
	@GetMapping(value = "/medicalstockward/movements/to/{target_ward_code}")
	public List<MovementWardDTO> getWardMovementsToWard(
		@PathVariable("target_ward_code") String idWardTo,
		@RequestParam("from") LocalDate dateFrom,
		@RequestParam("to") LocalDate dateTo
	) throws OHServiceException {
		LocalDateTime dateFromTime = null;
		if (dateFrom != null) {
			dateFromTime = dateFrom.atStartOfDay();
		}

		LocalDateTime dateToTime = null;
		if (dateTo != null) {
			dateToTime = dateTo.atStartOfDay();
		}
		return movementWardMapper.map2DTOList(movWardBrowserManager.getWardMovementsToWard(idWardTo, dateFromTime, dateToTime));
	}

	/**
	 * Persists the specified movement.
	 *
	 * @param newMovementDTO the movement to persist.
	 * @return {@code true} if the movement has been persisted, {@code false} otherwise.
	 * @throws OHServiceException When failed to create ward stock movement
	 */
	@PostMapping(value = "/medicalstockward/movements")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean newMovementWard(
		@Valid @RequestBody MovementWardDTO newMovementDTO
	) throws OHServiceException {
		MovementWard newMovement = movementWardMapper.map2Model(newMovementDTO);
		movWardBrowserManager.newMovementWard(newMovement);

		return true;
	}
}
