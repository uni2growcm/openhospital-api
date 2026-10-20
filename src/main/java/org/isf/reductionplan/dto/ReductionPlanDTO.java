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

package org.isf.reductionplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Class representing a reduction plan")
public class ReductionPlanDTO {

	@Schema(description = "Identifier of the reduction plan", example = "1")
	private Integer id;

	@NotBlank
	@Size(max = 100)
	@Schema(description = "Description of the reduction plan", example = "Staff Discount", maxLength = 100)
	private String description;

	@NotNull
	@DecimalMin(value = "0.00", message = "Operation rate must be greater than or equal to 0")
	@DecimalMax(value = "100.00", message = "Operation rate must not exceed 100")
	@Schema(description = "General operation reduction rate percentage", example = "10.00")
	private BigDecimal operationRate;

	@NotNull
	@DecimalMin(value = "0.00", message = "Medical rate must be greater than or equal to 0")
	@DecimalMax(value = "100.00", message = "Medical rate must not exceed 100")
	@Schema(description = "General medical reduction rate percentage", example = "10.00")
	private BigDecimal medicalRate;

	@NotNull
	@DecimalMin(value = "0.00", message = "Exam rate must be greater than or equal to 0")
	@DecimalMax(value = "100.00", message = "Exam rate must not exceed 100")
	@Schema(description = "General exam reduction rate percentage", example = "10.00")
	private BigDecimal examRate;

	@NotNull
	@DecimalMin(value = "0.00", message = "Other rate must be greater than or equal to 0")
	@DecimalMax(value = "100.00", message = "Other rate must not exceed 100")
	@Schema(description = "General other items reduction rate percentage", example = "10.00")
	private BigDecimal otherRate;

	@Schema(description = "Lock version", example = "0")
	private int lock;

	@Valid
	@Schema(description = "Specific exam reductions")
	private List<ExamReductionDTO> examReductions = new ArrayList<>();

	@Valid
	@Schema(description = "Specific medical reductions")
	private List<MedicalReductionDTO> medicalReductions = new ArrayList<>();

	@Valid
	@Schema(description = "Specific operation reductions")
	private List<OperationReductionDTO> operationReductions = new ArrayList<>();

	@Valid
	@Schema(description = "Specific price other reductions")
	private List<PriceOtherReductionDTO> priceOtherReductions = new ArrayList<>();

	public ReductionPlanDTO() {
		this.operationRate = BigDecimal.ZERO;
		this.medicalRate = BigDecimal.ZERO;
		this.examRate = BigDecimal.ZERO;
		this.otherRate = BigDecimal.ZERO;
	}

	public ReductionPlanDTO(Integer id, String description, BigDecimal operationRate, BigDecimal medicalRate, BigDecimal examRate, BigDecimal otherRate, int lock) {
		this.id = id;
		this.description = description;
		this.operationRate = operationRate != null ? operationRate : BigDecimal.ZERO;
		this.medicalRate = medicalRate != null ? medicalRate : BigDecimal.ZERO;
		this.examRate = examRate != null ? examRate : BigDecimal.ZERO;
		this.otherRate = otherRate != null ? otherRate : BigDecimal.ZERO;
		this.lock = lock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getOperationRate() {
		return operationRate;
	}

	public void setOperationRate(BigDecimal operationRate) {
		this.operationRate = operationRate;
	}

	public BigDecimal getMedicalRate() {
		return medicalRate;
	}

	public void setMedicalRate(BigDecimal medicalRate) {
		this.medicalRate = medicalRate;
	}

	public BigDecimal getExamRate() {
		return examRate;
	}

	public void setExamRate(BigDecimal examRate) {
		this.examRate = examRate;
	}

	public BigDecimal getOtherRate() {
		return otherRate;
	}

	public void setOtherRate(BigDecimal otherRate) {
		this.otherRate = otherRate;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public List<ExamReductionDTO> getExamReductions() {
		return examReductions;
	}

	public void setExamReductions(List<ExamReductionDTO> examReductions) {
		this.examReductions = examReductions != null ? examReductions : new ArrayList<>();
	}

	public List<MedicalReductionDTO> getMedicalReductions() {
		return medicalReductions;
	}

	public void setMedicalReductions(List<MedicalReductionDTO> medicalReductions) {
		this.medicalReductions = medicalReductions != null ? medicalReductions : new ArrayList<>();
	}

	public List<OperationReductionDTO> getOperationReductions() {
		return operationReductions;
	}

	public void setOperationReductions(List<OperationReductionDTO> operationReductions) {
		this.operationReductions = operationReductions != null ? operationReductions : new ArrayList<>();
	}

	public List<PriceOtherReductionDTO> getPriceOtherReductions() {
		return priceOtherReductions;
	}

	public void setPriceOtherReductions(List<PriceOtherReductionDTO> priceOtherReductions) {
		this.priceOtherReductions = priceOtherReductions != null ? priceOtherReductions : new ArrayList<>();
	}
}
