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
package org.isf.hospitalizationconsultation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class HospitalizationConsultationDTO {

	@Schema(description = "id of the hospitalization consultation", example = "13")
	private Integer id;

	@NotNull
	@Schema(description = "Encounter")
	private org.isf.encounter.dto.EncounterDTO encounter;

	@Schema(description = "Teams involved in the consultation", example = "Cardiology, Neurology")
	private String teams;

	@NotNull
	@Schema(description = "Date and time of the consultation", example = "2025-08-26T16:15:58")
	private LocalDateTime consultationDate;

	@Schema(description = "Parent complaints", example = "Patient complains of chest pain and shortness of breath")
	private String parentComplaints;

	@Schema(description = "Physical examination findings", example = "Normal heart sounds, clear lungs")
	private String physicalExamination;

	@Schema(description = "Diagnosis", example = "Acute myocardial infarction")
	private String diagnosis;

	@Schema(description = "Management plan", example = "Start thrombolytic therapy, monitor vitals")
	private String managementPlan;

	@Schema(description = "Lock version for optimistic locking", example = "1")
	private Integer lock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public org.isf.encounter.dto.EncounterDTO getEncounter() {
		return encounter;
	}

	public void setEncounter(org.isf.encounter.dto.EncounterDTO encounter) {
		this.encounter = encounter;
	}

	public String getTeams() {
		return teams;
	}

	public void setTeams(String teams) {
		this.teams = teams;
	}

	public LocalDateTime getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(LocalDateTime dateTime) {
		this.consultationDate = dateTime;
	}

	public String getParentComplaints() {
		return parentComplaints;
	}

	public void setParentComplaints(String parentComplaints) {
		this.parentComplaints = parentComplaints;
	}

	public String getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getManagementPlan() {
		return managementPlan;
	}

	public void setManagementPlan(String managementPlan) {
		this.managementPlan = managementPlan;
	}

	public Integer getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public HospitalizationConsultationDTO() {
	}
}
