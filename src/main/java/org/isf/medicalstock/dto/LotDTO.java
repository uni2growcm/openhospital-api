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
package org.isf.medicalstock.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class LotDTO {

	@NotNull(message = "The code is required")
	@Schema(description = "The lot's code", example = "LT001", maxLength = 50)
	private String code;

	@NotNull(message = "The preparation date is required")
	@Schema(description = "The preparation date", example = "2020-06-24", type = "string")
	private LocalDateTime preparationDate;

	@NotNull(message = "The due date is required")
	@Schema(description = "The due date", example = "2021-06-24", type = "string")
	private LocalDateTime dueDate;

	@Schema(description = "The lot's cost", example = "750")
	private BigDecimal cost;

	@Schema(description = "The lot's main store quantity", example = "10")
	private int mainStoreQuantity;

	@Schema(description = "The lot's ward total  quantity", example = "15")
	private double wardsTotalQuantity;

	@Schema(description = "The lot's overall quantity", example = "35")
	private double overallQuantity;

	public LotDTO() {
	}

	public LotDTO(String code, LocalDateTime preparationDate, LocalDateTime dueDate, BigDecimal cost) {
		this.code = code;
		this.preparationDate = preparationDate;
		this.dueDate = dueDate;
		this.cost = cost;
	}

	public LotDTO(String code, LocalDateTime preparationDate, LocalDateTime dueDate, BigDecimal cost, int mainStoreQuantity, double wardsTotalQuantity, double overallQuantity) {
		this(code, preparationDate, dueDate,cost);
		this.mainStoreQuantity = mainStoreQuantity;
		this.wardsTotalQuantity = wardsTotalQuantity;
		this.overallQuantity = overallQuantity;
	}

	public String getCode() {
		return this.code;
	}

	public LocalDateTime getPreparationDate() {
		return this.preparationDate;
	}

	public LocalDateTime getDueDate() {
		return this.dueDate;
	}

	public BigDecimal getCost() {
		return this.cost;
	}

	public int getMainStoreQuantity() {
		return this.mainStoreQuantity;
	}

	public double getWardsTotalQuantity() {
		return this.wardsTotalQuantity;
	}

	public double getOverallQuantity() {
		return this.mainStoreQuantity + wardsTotalQuantity;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setPreparationDate(LocalDateTime preparationDate) {
		this.preparationDate = preparationDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public void setMainStoreQuantity(int mainStoreQuantity) {
		this.mainStoreQuantity = mainStoreQuantity;
	}

	public void setWardsTotalQuantity(double wardsTotalQuantity) {
		this.wardsTotalQuantity = wardsTotalQuantity;
	}
}
