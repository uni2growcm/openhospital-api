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

package org.isf.articlefamily.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArticleFamilyDTO {

	@Schema(description = "Identifier of the article family", example = "1")
	private Integer id;

	@NotBlank
	@Size(max = 50)
	@Schema(description = "Code of the article family", example = "MED", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
	private String code;

	@NotBlank
	@Size(max = 255)
	@Schema(description = "Description of the article family", example = "Medicines and Drugs", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
	private String description;

	@Schema(description = "Optimistic locking version indicator", example = "0")
	private int lock;

	public ArticleFamilyDTO() {
	}

	public ArticleFamilyDTO(Integer id, String code, String description, int lock) {
		this.id = id;
		this.code = code;
		this.description = description;
		this.lock = lock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
