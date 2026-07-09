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
package org.isf.conditioning.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.isf.patient.dto.PatientDTO;
import org.isf.users.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ConditioningDTO {

	@Schema(description = "Conditioning key", example = "12")
	private Integer id;

	@Schema(description = "Aspiration conditioning", example = "true")
	private Boolean aspiration;

	@Schema(description = "Conditioning mce", example = "4")
	private Boolean mce;

	@Schema(description = "Conditioning ventilation duration", example = "true")
	private Boolean ventilation;

	@Schema(description = "Oxygen debit", example = "3")
	private Double oxygenDebit;

	@Schema(description = "Sg volume", example = "10")
	private Double sgVolume;

	@Schema(description = "Diazepam dose", example = "12")
	private Double diazepamDose;

	@Schema(description = "Bolus volume ", example = "3")
	private Double bolusSsVolume;

	@Schema(description = "Conditioning sng number", example = "true")
	private Boolean sngNumber;

	@Schema(description = "Conditioning reheating", example = "true")
	private Boolean reheating;

	@Schema(description = "Conditioning others", example = "others")
	private String others;

	@Schema(
		description = "Patient's condition at the time of admission. " +
			"Stored as a list of descriptors (e.g., stable, critical, unconscious).",
		example = "[\"stable\", \"conscious\"]"
	)
	private List<String> conditionAtAdmission;

	@Schema(description = "Conditioning malaria ", example = "sonde")
	private String malaria;

	@Schema(description = "Conditioning HIV test ", example = "INDETERMINATE")
	private String hivTest;

	@Schema(description = "Conditioning blood glucose level ", example = "5.6")
	private Double bloodGlucoseLevel;

	@Schema(description = "Conditioning others rapid screening test ", example = "5.6")
	private String othersRapidScreeningTest;

	@Schema(description = "Conditioning cpap details ", example = "5.6")
	private String cpapDetails;

	@NotNull
	@Schema(description = "patient", example = "22")
	private PatientDTO patient;

	@NotNull
	@Schema(description = "user", example = "2")
	private UserDTO performedBy;

	@NotNull
	@Schema(description = "Conditioning date", example = "String")
	private LocalDateTime performedAt;
	
	@Schema(description = "Lock", example = "0")
	private int lock;
	
	@Schema(description = "CPAP", example = "true")
	private Boolean cpap;

	public Boolean getAspiration() {
		return aspiration;
	}

	public void setAspiration(Boolean aspiration) {
		this.aspiration = aspiration;
	}

	public Boolean getMce() {
		return mce;
	}

	public void setMce(Boolean mce) {
		this.mce = mce;
	}

	public Boolean getVentilation() {
		return ventilation;
	}

	public void setVentilation(Boolean ventilation) {
		this.ventilation = ventilation;
	}

	public Double getOxygenDebit() {
		return oxygenDebit;
	}

	public void setOxygenDebit(Double oxygenDebit) {
		this.oxygenDebit = oxygenDebit;
	}

	public Double getSgVolume() {
		return sgVolume;
	}

	public void setSgVolume(Double sgVolume) {
		this.sgVolume = sgVolume;
	}

	public Double getDiazepamDose() {
		return diazepamDose;
	}

	public void setDiazepamDose(Double diazepamDose) {
		this.diazepamDose = diazepamDose;
	}

	public Double getBolusSsVolume() {
		return bolusSsVolume;
	}

	public void setBolusSsVolume(Double bolusSsVolume) {
		this.bolusSsVolume = bolusSsVolume;
	}

	public Boolean getSngNumber() {
		return sngNumber;
	}

	public void setSngNumber(Boolean sngNumber) {
		this.sngNumber = sngNumber;
	}

	public Boolean getReheating() {
		return reheating;
	}

	public void setReheating(Boolean reheating) {
		this.reheating = reheating;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public List<String> getConditionAtAdmission() {
		return conditionAtAdmission;
	}

	public void setConditionAtAdmission(List<String> conditionAtAdmission) {this.conditionAtAdmission = conditionAtAdmission;}

	public String getMalaria() {return malaria;}

	public void setMalaria(String malaria) {this.malaria = malaria;}

	public String getHivTest() {return hivTest;}

	public void setHivTest(String hivTest) {this.hivTest = hivTest;}

	public Double getBloodGlucoseLevel() {return bloodGlucoseLevel;}

	public void setBloodGlucoseLevel(Double tdr) {this.bloodGlucoseLevel = bloodGlucoseLevel;}

	public LocalDateTime getPerformedAt() {
		return performedAt;
	}

	public String getOthersRapidScreeningTest() {
		return othersRapidScreeningTest;
	}

	public void setOthersRapidScreeningTest(String othersRapidScreeningTest) {
		this.othersRapidScreeningTest = othersRapidScreeningTest;
	}

	public String getCpapDetails() {
		return cpapDetails;
	}

	public  void setCpapDetails(String cpapDetails) {
		this.cpapDetails = cpapDetails;
	}

	public void setPerformedAt(LocalDateTime performedAt) {
		this.performedAt = performedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public UserDTO getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(UserDTO user) {
		this.performedBy = user;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
	
	public Boolean getCpap() {
		return cpap;
	}

	public void setCpap(Boolean cpap) {
		this.cpap = cpap;
	}
}
