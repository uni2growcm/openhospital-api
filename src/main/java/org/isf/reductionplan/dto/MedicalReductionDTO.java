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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.isf.medical.dto.MedicalDTO;

@Schema(description = "Class representing a medical reduction")
public class MedicalReductionDTO {

	@Schema(description = "Identifier of the medical reduction", example = "1")
	private Integer id;

	@NotNull
	@Schema(description = "The medical item")
	private MedicalDTO medical;

	@NotNull
	@DecimalMin(value = "0.01", message = "Reduction rate must be greater than 0")
	@DecimalMax(value = "100.00", message = "Reduction rate must not exceed 100")
	@Schema(description = "Reduction rate percentage", example = "15.00")
	private BigDecimal reductionRate;

	@Schema(description = "Lock version", example = "0")
	private int lock;

	public MedicalReductionDTO() {
	}

	public MedicalReductionDTO(Integer id, MedicalDTO medical, BigDecimal reductionRate, int lock) {
		this.id = id;
		this.medical = medical;
		this.reductionRate = reductionRate;
		this.lock = lock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MedicalDTO getMedical() {
		return medical;
	}

	public void setMedical(MedicalDTO medical) {
		this.medical = medical;
	}

	public BigDecimal getReductionRate() {
		return reductionRate;
	}

	public void setReductionRate(BigDecimal reductionRate) {
		this.reductionRate = reductionRate;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
