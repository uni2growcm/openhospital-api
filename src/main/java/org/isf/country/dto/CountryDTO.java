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

@Schema(description = "Class representing a country")
public class CountryDTO {

	@Schema(description = "Country id", example = "1")
	private int id;

	@Schema(description = "ISO code of the country", example = "IT", maxLength = 2)
	private String isoCode;

	@Schema(description = "Phone code of the country", example = "+39", maxLength = 10)
	private String phoneCode;

	@Schema(description = "Name of the country", example = "Italy", maxLength = 100)
	private String name;

	@Schema(description = "Lock", example = "0")
	private int lock;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getPhoneCode() {
		return this.phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLock() {
		return this.lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
