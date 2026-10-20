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
package org.isf.country.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Class representing a country")
public class CountryDTO {

	@Schema(description = "ID of the country", example = "1")
	private Integer id;

	@NotBlank(message = "ISO code is required")
	@Size(min = 2, max = 2, message = "ISO code must be exactly 2 characters")
	@Pattern(regexp = "^[A-Za-z]{2}$", message = "ISO code must consist of 2 letters")
	@Schema(description = "ISO code of the country (2 letters)", example = "CM", maxLength = 2)
	private String isoCode;

	@NotBlank(message = "Country name is required")
	@Size(max = 100, message = "Country name cannot exceed 100 characters")
	@Schema(description = "Name of the country", example = "Cameroon", maxLength = 100)
	private String name;

	@NotBlank(message = "Phone code is required")
	@Size(max = 10, message = "Phone code cannot exceed 10 characters")
	@Schema(description = "Phone code / dial code of the country", example = "+237", maxLength = 10)
	private String phoneCode;

	@Schema(description = "Optimistic lock version", example = "0")
	private int lock;

	public CountryDTO() {
	}

	public CountryDTO(Integer id, String isoCode, String name, String phoneCode) {
		this.id = id;
		this.isoCode = isoCode;
		this.name = name;
		this.phoneCode = phoneCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
