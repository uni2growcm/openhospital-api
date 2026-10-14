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
package org.isf.staff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Class representing a staff member (personnel)")
public class StaffDTO {

	@Schema(description = "The staff member's code")
	private Integer code;

	@NotNull
	@Schema(description = "The first name of the staff member", maxLength = 50)
	private String firstName;

	@NotNull
	@Schema(description = "The last name of the staff member", maxLength = 50)
	private String lastName;

	@Schema(description = "The profession of the staff member", maxLength = 50)
	private String profession;

	@NotNull
	@Schema(description = "The position of the staff member", maxLength = 50)
	private String position;

	@Schema(description = "The phone number of the staff member", maxLength = 50)
	private String phone;

	@Schema(description = "lock", example = "0")
	private Integer lock;

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getLock() {
		return this.lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}
}
