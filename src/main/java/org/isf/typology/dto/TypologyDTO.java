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
package org.isf.typology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.isf.typology.model.Family;

public class TypologyDTO {

	@NotNull
	@Schema(description = "Code of the typology", example = "VT1")
	private String code;

	@NotNull
	@Schema(description = "Description of the typology", example = "Visit Type 1", maxLength = 255)
	private String description;

	@NotNull
	@Schema(description = "Family of the typology", example = "VISITTYPE")
	private Family family;

	public TypologyDTO() {
	}

	public TypologyDTO(String code, String description, Family family) {
		this.code = code;
		this.description = description;
		this.family = family;
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

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	@com.fasterxml.jackson.annotation.JsonSetter("family")
	public void setFamilyFromJson(Object family) {
		if (family instanceof Family f) {
			this.family = f;
		} else if (family != null) {
			this.family = Family.fromString(family.toString());
		}
	}
}
