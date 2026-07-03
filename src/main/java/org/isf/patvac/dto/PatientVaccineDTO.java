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
package org.isf.patvac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.NotNull;
import org.isf.patient.dto.PatientDTO;
import org.isf.vaccine.dto.VaccineDTO;

import java.time.LocalDateTime;

public class PatientVaccineDTO {
	private int code;

	@NotNull
	@Schema(description = "A progr. in year", example = "1")
	private int progr;

	@NotNull
	@Schema(description = "The vaccine date", type = "string")
	private LocalDateTime vaccineDate;

	@NotNull
	@Schema(description = "The patient to be vaccine")
	private PatientDTO patient;

	@NotNull
	@Schema(description = "The vaccine")
	private VaccineDTO vaccine;

	@Schema(description = "Lock", example = "0")
	private int lock;

	private int hashCode;

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	@Schema(accessMode = AccessMode.READ_ONLY)
	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getProgr() {
		return this.progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public LocalDateTime getVaccineDate() {
		return this.vaccineDate;
	}

	public void setVaccineDate(LocalDateTime vaccineDate) {
		this.vaccineDate = vaccineDate;
	}

	public PatientDTO getPatient() {
		return this.patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public VaccineDTO getVaccine() {
		return this.vaccine;
	}

	public void setVaccine(VaccineDTO vaccine) {
		this.vaccine = vaccine;
	}
}
