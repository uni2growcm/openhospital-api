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

package org.isf.reductionplan.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.isf.reductionplan.dto.ExamReductionDTO;
import org.isf.reductionplan.dto.MedicalReductionDTO;
import org.isf.reductionplan.dto.OperationReductionDTO;
import org.isf.reductionplan.dto.PriceOtherReductionDTO;
import org.isf.reductionplan.dto.ReductionPlanDTO;
import org.isf.reductionplan.manager.ReductionPlanManager;
import org.isf.reductionplan.mapper.ReductionPlanMapper;
import org.isf.reductionplan.model.ExamReduction;
import org.isf.reductionplan.model.MedicalReduction;
import org.isf.reductionplan.model.OperationReduction;
import org.isf.reductionplan.model.PriceOtherReduction;
import org.isf.reductionplan.model.ReductionPlan;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
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

@RestController
@Tag(name = "Reduction Plans")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/reduction-plans", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReductionPlanController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReductionPlanController.class);

	private final ReductionPlanManager reductionPlanManager;
	private final ReductionPlanMapper mapper;

	public ReductionPlanController(ReductionPlanManager reductionPlanManager, ReductionPlanMapper mapper) {
		this.reductionPlanManager = reductionPlanManager;
		this.mapper = mapper;
	}

	/**
	 * Retrieves all reduction plans or searches by description.
	 *
	 * @param q Optional search keyword
	 * @return List of {@link ReductionPlanDTO}
	 * @throws OHServiceException When failing to fetch reduction plans
	 */
	@GetMapping
	@Operation(summary = "Get all reduction plans or search by description")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "List of reduction plans retrieved successfully")
	})
	public List<ReductionPlanDTO> getReductionPlans(@RequestParam(value = "q", required = false) String q) throws OHServiceException {
		LOGGER.info("Retrieving reduction plans with keyword: {}", q);
		List<ReductionPlan> plans;
		if (q != null && !q.trim().isEmpty()) {
			plans = reductionPlanManager.getByDescription(q.trim());
		} else {
			plans = reductionPlanManager.getAll();
		}
		return mapper.map2DTOList(plans);
	}

	/**
	 * Retrieves a reduction plan by ID.
	 *
	 * @param id Reduction plan ID
	 * @return The retrieved {@link ReductionPlanDTO}
	 * @throws OHServiceException When failing to fetch reduction plan
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get a reduction plan by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Reduction plan retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Reduction plan not found")
	})
	public ReductionPlanDTO getReductionPlan(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Retrieving reduction plan with ID: {}", id);
		ReductionPlan plan = reductionPlanManager.getById(id);
		if (plan == null) {
			LOGGER.warn("Reduction plan with ID {} not found.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.reductionplan.not_found"), HttpStatus.NOT_FOUND);
		}
		return mapper.map2DTO(plan);
	}

	/**
	 * Creates a new reduction plan.
	 *
	 * @param reductionPlanDTO The reduction plan payload
	 * @return The created {@link ReductionPlanDTO}
	 * @throws OHServiceException When failing to create reduction plan
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new reduction plan")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Reduction plan created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "409", description = "Conflict or data integrity violation")
	})
	public ReductionPlanDTO createReductionPlan(@RequestBody @Valid ReductionPlanDTO reductionPlanDTO) throws OHServiceException {
		LOGGER.info("Creating new reduction plan: {}", reductionPlanDTO.getDescription());

		try {
			ReductionPlan model = mapper.map2Model(reductionPlanDTO);
			ReductionPlan saved = reductionPlanManager.add(model);
			LOGGER.info("Reduction plan created with ID {}.", saved.getId());
			return mapper.map2DTO(saved);
		} catch (OHDataIntegrityViolationException | DataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while creating reduction plan: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.reductionplan.integrity_violation"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Updates an existing reduction plan.
	 *
	 * @param id Reduction plan ID
	 * @param reductionPlanDTO The reduction plan payload
	 * @return The updated {@link ReductionPlanDTO}
	 * @throws OHServiceException When failing to update reduction plan
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing reduction plan")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Reduction plan updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "404", description = "Reduction plan not found"),
		@ApiResponse(responseCode = "409", description = "Conflict or data integrity violation")
	})
	public ReductionPlanDTO updateReductionPlan(
		@PathVariable Integer id,
		@RequestBody @Valid ReductionPlanDTO reductionPlanDTO
	) throws OHServiceException {
		LOGGER.info("Updating reduction plan with ID: {}", id);

		ReductionPlan existingPlan = reductionPlanManager.getById(id);
		if (existingPlan == null) {
			LOGGER.warn("Reduction plan with ID {} not found for update.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.reductionplan.not_found"), HttpStatus.NOT_FOUND);
		}

		reductionPlanDTO.setId(id);
		if (reductionPlanDTO.getLock() == 0) {
			reductionPlanDTO.setLock(existingPlan.getLock());
		}

		synchronizeChildReductions(reductionPlanDTO, existingPlan);

		try {
			ReductionPlan model = mapper.map2Model(reductionPlanDTO);
			ReductionPlan updated = reductionPlanManager.update(model);
			LOGGER.info("Reduction plan with ID {} updated successfully.", id);
			return mapper.map2DTO(updated);
		} catch (OHDataIntegrityViolationException | DataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while updating reduction plan: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.reductionplan.integrity_violation"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Synchronizes the IDs and lock versions of child reductions with existing persistent entities
	 * to prevent optimistic locking failures on updates.
	 */
	private void synchronizeChildReductions(ReductionPlanDTO reductionPlanDTO, ReductionPlan existingPlan) {
		if (reductionPlanDTO.getMedicalReductions() != null && existingPlan.getMedicalReductions() != null) {
			Map<String, MedicalReduction> existingMedicals = existingPlan.getMedicalReductions().stream()
				.filter(mr -> mr.getMedical() != null && mr.getMedical().getCode() != null)
				.collect(Collectors.toMap(mr -> String.valueOf(mr.getMedical().getCode()), mr -> mr, (a, b) -> a));

			for (MedicalReductionDTO mrDTO : reductionPlanDTO.getMedicalReductions()) {
				if (mrDTO.getMedical() != null && mrDTO.getMedical().getCode() != null) {
					MedicalReduction existingMr = existingMedicals.get(String.valueOf(mrDTO.getMedical().getCode()));
					if (existingMr == null && mrDTO.getId() != null) {
						existingMr = existingPlan.getMedicalReductions().stream()
							.filter(mr -> Objects.equals(mr.getId(), mrDTO.getId()))
							.findFirst()
							.orElse(null);
					}
					if (existingMr != null) {
						mrDTO.setId(existingMr.getId());
						mrDTO.setLock(existingMr.getLock());
					} else {
						mrDTO.setId(null);
						mrDTO.setLock(0);
					}
				}
			}
		}

		if (reductionPlanDTO.getExamReductions() != null && existingPlan.getExamReductions() != null) {
			Map<String, ExamReduction> existingExams = existingPlan.getExamReductions().stream()
				.filter(er -> er.getExam() != null && er.getExam().getCode() != null)
				.collect(Collectors.toMap(er -> String.valueOf(er.getExam().getCode()), er -> er, (a, b) -> a));

			for (ExamReductionDTO erDTO : reductionPlanDTO.getExamReductions()) {
				if (erDTO.getExam() != null && erDTO.getExam().getCode() != null) {
					ExamReduction existingEr = existingExams.get(String.valueOf(erDTO.getExam().getCode()));
					if (existingEr == null && erDTO.getId() != null) {
						existingEr = existingPlan.getExamReductions().stream()
							.filter(er -> Objects.equals(er.getId(), erDTO.getId()))
							.findFirst()
							.orElse(null);
					}
					if (existingEr != null) {
						erDTO.setId(existingEr.getId());
						erDTO.setLock(existingEr.getLock());
					} else {
						erDTO.setId(null);
						erDTO.setLock(0);
					}
				}
			}
		}

		if (reductionPlanDTO.getOperationReductions() != null && existingPlan.getOperationReductions() != null) {
			Map<String, OperationReduction> existingOps = existingPlan.getOperationReductions().stream()
				.filter(or -> or.getOperation() != null && or.getOperation().getCode() != null)
				.collect(Collectors.toMap(or -> String.valueOf(or.getOperation().getCode()), or -> or, (a, b) -> a));

			for (OperationReductionDTO orDTO : reductionPlanDTO.getOperationReductions()) {
				if (orDTO.getOperation() != null && orDTO.getOperation().getCode() != null) {
					OperationReduction existingOr = existingOps.get(String.valueOf(orDTO.getOperation().getCode()));
					if (existingOr == null && orDTO.getId() != null) {
						existingOr = existingPlan.getOperationReductions().stream()
							.filter(or -> Objects.equals(or.getId(), orDTO.getId()))
							.findFirst()
							.orElse(null);
					}
					if (existingOr != null) {
						orDTO.setId(existingOr.getId());
						orDTO.setLock(existingOr.getLock());
					} else {
						orDTO.setId(null);
						orDTO.setLock(0);
					}
				}
			}
		}

		if (reductionPlanDTO.getPriceOtherReductions() != null && existingPlan.getPriceOtherReductions() != null) {
			Map<Integer, PriceOtherReduction> existingOthers = existingPlan.getPriceOtherReductions().stream()
				.filter(por -> por.getPricesOthers() != null)
				.collect(Collectors.toMap(por -> por.getPricesOthers().getId(), por -> por, (a, b) -> a));

			for (PriceOtherReductionDTO porDTO : reductionPlanDTO.getPriceOtherReductions()) {
				if (porDTO.getPricesOthers() != null) {
					PriceOtherReduction existingPor = existingOthers.get(porDTO.getPricesOthers().getId());
					if (existingPor == null && porDTO.getId() != null) {
						existingPor = existingPlan.getPriceOtherReductions().stream()
							.filter(por -> Objects.equals(por.getId(), porDTO.getId()))
							.findFirst()
							.orElse(null);
					}
					if (existingPor != null) {
						porDTO.setId(existingPor.getId());
						porDTO.setLock(existingPor.getLock());
					} else {
						porDTO.setId(null);
						porDTO.setLock(0);
					}
				}
			}
		}
	}

	/**
	 * Deletes a reduction plan by ID (soft delete).
	 *
	 * @param id Reduction plan ID
	 * @throws OHServiceException When failing to delete reduction plan
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a reduction plan by ID (soft delete)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Reduction plan successfully deleted"),
		@ApiResponse(responseCode = "404", description = "Reduction plan not found")
	})
	public void deleteReductionPlan(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Deleting reduction plan with ID: {}", id);

		ReductionPlan existingPlan = reductionPlanManager.getById(id);
		if (existingPlan == null) {
			LOGGER.warn("Reduction plan with ID {} not found for deletion.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.reductionplan.not_found"), HttpStatus.NOT_FOUND);
		}

		reductionPlanManager.delete(existingPlan);
		LOGGER.info("Reduction plan with ID {} deleted successfully.", id);
	}
}
