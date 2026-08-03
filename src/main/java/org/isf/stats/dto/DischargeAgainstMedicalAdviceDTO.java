package org.isf.stats.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class DischargeAgainstMedicalAdviceDTO {

	@NotNull
	@Schema(description = "ID of the patient", example = "45")
	private Integer patID;

	@NotNull
	@Schema(description = "ID of patient admission", example = "513")
	private Integer admID;

	@NotNull
	@Schema(description = "Localisation of the person who want to remove patient", example = "Cotonou")
	private String localisation;

	@NotNull
	@Schema(description = "Reference of patient relationship: CNI / PASSPORT", example = "100345678")
	private String reference;

	@NotNull
	@Schema(description = "District of patient relationship", example = "Ouest")
	private String district;

	@NotNull
	@Schema(description = "Commune of patient relationship", example = "Cotonou 1")
	private String commune;

	@NotNull
	@Schema(description = "Phone number of the patient relationship", example = "68790983")
	private String phoneNumber;

	@NotNull
	@Schema(description = "Hospitalisation date", example = "2026-04-24T12:23:46")
	private LocalDateTime hospitalisationDate;

	@Schema(description = "Patient relationship occupation", example = "Data Analyst")
	private String patientRelationshipOccupation;

	@NotNull
	@Schema(description = "Patient relationship type", example = "Brother")
	private String patientRelationshipType;

	@NotNull
	@Schema(description = "Name of patient relationship", example = "George Doe")
	private String patientRelationshipName;

	@NotNull
	@Schema(description = "Date of discharge against medical advice", example = "2026-04-24T12:23:46")
	private LocalDateTime madeOnDate;

	public Integer getPatID() {
		return patID;
	}

	public void setPatID(Integer patID) {
		this.patID = patID;
	}

	public Integer getAdmID() {
		return admID;
	}

	public void setAdmID(Integer admID) {
		this.admID = admID;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getHospitalisationDate() {
		return hospitalisationDate;
	}

	public void setHospitalisationDate(LocalDateTime hospitalisationDate) {
		this.hospitalisationDate = hospitalisationDate;
	}

	public String getPatientRelationshipOccupation() {
		return patientRelationshipOccupation;
	}

	public void setPatientRelationshipOccupation(String patientRelationshipOccupation) {
		this.patientRelationshipOccupation = patientRelationshipOccupation;
	}

	public String getPatientRelationshipType() {
		return patientRelationshipType;
	}

	public void setPatientRelationshipType(String patientRelationshipType) {
		this.patientRelationshipType = patientRelationshipType;
	}

	public String getPatientRelationshipName() {
		return patientRelationshipName;
	}

	public void setPatientRelationshipName(String patientRelationshipName) {
		this.patientRelationshipName = patientRelationshipName;
	}

	public LocalDateTime getMadeOnDate() {
		return madeOnDate;
	}

	public void setMadeOnDate(LocalDateTime madeOnDate) {
		this.madeOnDate = madeOnDate;
	}

	public DischargeAgainstMedicalAdviceDTO() {
	}
}
