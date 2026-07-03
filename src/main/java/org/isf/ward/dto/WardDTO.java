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
package org.isf.ward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class WardDTO {

	@Schema(description = "The ward code", maxLength = 3)
	private String code;

	@NotNull
	@Schema(description = "The name of the ward", maxLength = 50)
	private String description;

	@Schema(description = "The phone number of the ward", maxLength = 50)
	private String telephone;

	@Schema(description = "The fax number of the ward", maxLength = 50)
	private String fax;

	@Schema(description = "The email of the ward", maxLength = 50)
	private String email;

	@NotNull
	private Integer beds;

	@NotNull
	private Integer nurs;

	@NotNull
	private Integer docs;

	@NotNull
	private boolean isPharmacy;

	@NotNull
	private boolean isMale;

	@NotNull
	private boolean isFemale;

	private boolean isOpd;

	@NotNull
	private int visitDuration;

	@Schema(description = "lock", example = "0")
	private Integer lock;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getBeds() {
		return this.beds;
	}

	public void setBeds(Integer beds) {
		this.beds = beds;
	}

	public Integer getNurs() {
		return this.nurs;
	}

	public void setNurs(Integer nurs) {
		this.nurs = nurs;
	}

	public Integer getDocs() {
		return this.docs;
	}

	public void setDocs(Integer docs) {
		this.docs = docs;
	}

	public boolean isPharmacy() {
		return this.isPharmacy;
	}

	public void setPharmacy(boolean isPharmacy) {
		this.isPharmacy = isPharmacy;
	}

	public boolean isMale() {
		return this.isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public boolean isFemale() {
		return this.isFemale;
	}

	public void setFemale(boolean isFemale) {
		this.isFemale = isFemale;
	}

	public Integer getVisitDuration() {
		return this.visitDuration;
	}

	public void setVisitDuration(Integer visitDuration) {
		this.visitDuration = visitDuration;
	}

	public Integer getLock() {
		return this.lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public boolean isOpd() {
		return isOpd;
	}

	public void setOpd(boolean isOpd) {
		this.isOpd = isOpd;
	}
}
