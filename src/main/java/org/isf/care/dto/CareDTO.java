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
package org.isf.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.isf.patient.dto.PatientDTO;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Class representing a Care")
public class CareDTO {

	@Schema(description = "Id of the care", example = "1")
	private Integer id;

	@Schema(description = "Team of the care", example = "[\"Dr. Albert\", \"Inf. Clodel\"]")
	private List<String> team;

	@Schema(description = "Observation of the commune", example = "Good observation")
	private String observation;

	@Schema(description = "Planned care", example = "Take one perfusion")
	private String plannedCare;

	@Schema(description = "Note of the care", example = "The treatment has worked")
	private String note;

	@Schema(description = "Date of the care", example = "string")
	private LocalDateTime careDate;

	@NotNull
	@Schema(description = "patient", example = "22")
	private PatientDTO patient;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getTeam() {
		return team;
	}

	public void setTeam(List<String> team) {
		this.team = team;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getPlannedCare() {
		return plannedCare;
	}

	public void setPlannedCare(String plannedCare) {
		this.plannedCare = plannedCare;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDateTime getCareDate() {
		return careDate;
	}

	public void setCareDate(LocalDateTime careDate) {
		this.careDate = careDate;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}
}
