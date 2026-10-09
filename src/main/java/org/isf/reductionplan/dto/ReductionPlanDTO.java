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

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Class representing a reduction plan")
public class ReductionPlanDTO {

	@Schema(description = "Reduction plan id", example = "1")
	private int id;

	@Schema(description = "Description of the reduction plan", example = "Staff reduction", maxLength = 50)
	private String description;

	@Schema(description = "Reduction rate applied to operations", example = "10.00")
	private BigDecimal operationRate;

	@Schema(description = "Reduction rate applied to medicals", example = "10.00")
	private BigDecimal medicalRate;

	@Schema(description = "Reduction rate applied to exams", example = "10.00")
	private BigDecimal examRate;

	@Schema(description = "Reduction rate applied to other prices", example = "10.00")
	private BigDecimal otherRate;

	@Schema(description = "Lock", example = "0")
	private int lock;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getOperationRate() {
		return this.operationRate;
	}

	public void setOperationRate(BigDecimal operationRate) {
		this.operationRate = operationRate;
	}

	public BigDecimal getMedicalRate() {
		return this.medicalRate;
	}

	public void setMedicalRate(BigDecimal medicalRate) {
		this.medicalRate = medicalRate;
	}

	public BigDecimal getExamRate() {
		return this.examRate;
	}

	public void setExamRate(BigDecimal examRate) {
		this.examRate = examRate;
	}

	public BigDecimal getOtherRate() {
		return this.otherRate;
	}

	public void setOtherRate(BigDecimal otherRate) {
		this.otherRate = otherRate;
	}

	public int getLock() {
		return this.lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
