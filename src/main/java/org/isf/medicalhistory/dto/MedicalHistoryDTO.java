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
package org.isf.medicalhistory.dto;

import java.time.LocalDateTime;
import org.isf.patient.dto.PatientDTO;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Class representing the medical history of a patient")
public class MedicalHistoryDTO {

	@Schema(description = "Unique identifier of the medical history", example = "1")
	private Integer id;

	@NotNull
	@Schema(description = "The patient linked to this medical history")
	private PatientDTO patient;

	@Schema(description = "Rank of the patient among siblings", example = "2")
	private String siblingRank;

	@Schema(description = "Pregnancy term description", example = "FULL TERM", maxLength = 50)
	private String termPregnancy;

	@Schema(description = "Mode of delivery", example = "CESAREAN", maxLength = 50)
	private String deliveryMode;

	@Schema(description = "Reason for the delivery mode", example = "Fetal distress", maxLength = 100)
	private String reasonMode;

	@Schema(description = "Apgar score of the newborn", example = "8/10", maxLength = 10)
	private String apgarScore;

	@Schema(description = "Birth weight of the patient (kg)", example = "3.2")
	private Double birthWeight;

	@Schema(description = "Vaccination state for PEV vaccines", example = "Up to date")
	private String vaccinationStatePev;

	@Schema(description = "Vaccination state for non-PEV vaccines", example = "BCG pending")
	private String vaccinationStateNoPev;

	@Schema(description = "Use of Milda for anti-malarial prophylaxis", example = "YES")
	private String antiMalarialProphylaxisMilda;

	@Schema(description = "Use of VAP for anti-malarial prophylaxis", example = "NO")
	private String antiMalarialProphylaxisVap;

	@Schema(description = "Other anti-malarial prophylaxis methods used", example = "Herbal medicine")
	private String antiMalarialProphylaxisOthers;

	@Schema(description = "Indicates if a surgical procedure has been performed", example = "true")
	private Boolean surgicalProcedure;

	@Schema(description = "Condition leading to surgery", example = "Appendicitis")
	private String surgicalProcedureCondition;

	@Schema(description = "Type of surgical procedure performed", example = "Appendectomy")
	private String surgicalProcedureType;

	@Schema(description = "Date of the surgical procedure", example = "2024-09-15T08:30:00")
	private LocalDateTime surgicalProcedureDate;

	@Schema(description = "Details about food diversification", example = "Started at 6 months")
	private String diversification;

	@Schema(description = "Description of neonatal period condition", example = "Normal")
	private String neonatalPeriod;

	@Schema(description = "Previous hospitalizations details", example = "Hospitalized in 2023 for malaria")
	private String previousHospitalization;

	@Schema(description = "Father", example = "John Doe")
	private String father;

	@Schema(description = "Mother", example = "Jane Doe")
	private String mother;

	@Schema(description = "Siblings", example = "Jone")
	private String siblings;

	@Schema(description = "Any other useful medical or personal information", example = "History of asthma in family")
	private String otherUsefulInformation;

	@Schema(description = "Patient's diet information", example = "Vegetarian diet")
	private String diet;

	@Schema(description = "Indicates if the patient has undergone de-parasitization", example = "true")
	private Boolean deParasitization;

	@Schema(description = "Psychomotor development status", example = "Normal development")
	private String psychomotorDev;

	@Schema(description = "Somatic growth status", example = "Normal growth")
	private String somaticGrowth;

	@Schema(description = "Indicates if the patient receives iron supplements", example = "true")
	private Boolean ironSupplement;

	@Schema(description = "Indicates if the patient receives folic acid supplements", example = "false")
	private Boolean folicAcidSupplement;

	@Schema(description = "Indicates if the patient receives vitamin A supplements", example = "true")
	private Boolean vitASupplement;

	@Schema(description = "Details of any other supplements used", example = "Zinc and calcium")
	private String otherSupplements;

	@Schema(description = "Indicates if the patient has received blood transfusions", example = "false")
	private Boolean transfusion;

	@Schema(description = "Date of the last blood transfusion", example = "2023-12-01T14:00:00")
	private LocalDateTime lastTransfusionDate;

	@Schema(description = "Indicates if the patient has sickle cell disease", example = "false")
	private Boolean sickleCell;

	@Schema(description = "Indicates if the patient has a drug allergy", example = "true")
	private Boolean drugAllergy;

	@Schema(description = "Details about the allergy", example = "Allergic to penicillin")
	private String allergyPrecision;

	@Schema(description = "Information about hemolysis or related conditions", example = "None")
	private String hemylosis;

	@Schema(description = "Other personal pathologies", example = "Hypertension")
	private String otherPersonalPathologies;

	@Schema(description = "Other family pathologies", example = "Diabetes")
	private String otherFamilyPathologies;

	@Schema(description = "Medical history performed date", example = "2025-08-26T16:15:58")
	private LocalDateTime performedAt;

	@Schema(description = "Optimistic lock value", example = "0")
	private int lock;

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

	public String getSiblingRank() {
		return siblingRank;
	}

	public void setSiblingRank(String siblingRank) {
		this.siblingRank = siblingRank;
	}

	public String getTermPregnancy() {
		return termPregnancy;
	}

	public void setTermPregnancy(String termPregnancy) {
		this.termPregnancy = termPregnancy;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getReasonMode() {
		return reasonMode;
	}

	public void setReasonMode(String reasonMode) {
		this.reasonMode = reasonMode;
	}

	public String getApgarScore() {
		return apgarScore;
	}

	public void setApgarScore(String apgarScore) {
		this.apgarScore = apgarScore;
	}

	public Double getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(Double birthWeight) {
		this.birthWeight = birthWeight;
	}

	public String getVaccinationStatePev() {
		return vaccinationStatePev;
	}

	public void setVaccinationStatePev(String vaccinationStatePev) {
		this.vaccinationStatePev = vaccinationStatePev;
	}

	public String getVaccinationStateNoPev() {
		return vaccinationStateNoPev;
	}

	public void setVaccinationStateNoPev(String vaccinationStateNoPev) {
		this.vaccinationStateNoPev = vaccinationStateNoPev;
	}

	public String getAntiMalarialProphylaxisMilda() {
		return antiMalarialProphylaxisMilda;
	}

	public void setAntiMalarialProphylaxisMilda(String antiMalarialProphylaxisMilda) {
		this.antiMalarialProphylaxisMilda = antiMalarialProphylaxisMilda;
	}

	public String getAntiMalarialProphylaxisVap() {
		return antiMalarialProphylaxisVap;
	}

	public void setAntiMalarialProphylaxisVap(String antiMalarialProphylaxisVap) {
		this.antiMalarialProphylaxisVap = antiMalarialProphylaxisVap;
	}

	public String getAntiMalarialProphylaxisOthers() {
		return antiMalarialProphylaxisOthers;
	}

	public void setAntiMalarialProphylaxisOthers(String antiMalarialProphylaxisOthers) {
		this.antiMalarialProphylaxisOthers = antiMalarialProphylaxisOthers;
	}

	public Boolean getSurgicalProcedure() {
		return surgicalProcedure;
	}

	public void setSurgicalProcedure(Boolean surgicalProcedure) {
		this.surgicalProcedure = surgicalProcedure;
	}

	public String getSurgicalProcedureCondition() {
		return surgicalProcedureCondition;
	}

	public void setSurgicalProcedureCondition(String surgicalProcedureCondition) {
		this.surgicalProcedureCondition = surgicalProcedureCondition;
	}

	public String getSurgicalProcedureType() {
		return surgicalProcedureType;
	}

	public void setSurgicalProcedureType(String surgicalProcedureType) {
		this.surgicalProcedureType = surgicalProcedureType;
	}

	public LocalDateTime getSurgicalProcedureDate() {
		return surgicalProcedureDate;
	}

	public void setSurgicalProcedureDate(LocalDateTime surgicalProcedureDate) {
		this.surgicalProcedureDate = surgicalProcedureDate;
	}

	public String getDiversification() {
		return diversification;
	}

	public void setDiversification(String diversification) {
		this.diversification = diversification;
	}

	public String getNeonatalPeriod() {
		return neonatalPeriod;
	}

	public void setNeonatalPeriod(String neonatalPeriod) {
		this.neonatalPeriod = neonatalPeriod;
	}

	public String getPreviousHospitalization() {
		return previousHospitalization;
	}

	public void setPreviousHospitalization(String previousHospitalization) {
		this.previousHospitalization = previousHospitalization;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getSiblings() {
		return siblings;
	}

	public void setSiblings(String siblings) {
		this.siblings = siblings;
	}

	public String getOtherUsefulInformation() {
		return otherUsefulInformation;
	}

	public void setOtherUsefulInformation(String otherUsefulInformation) {
		this.otherUsefulInformation = otherUsefulInformation;
	}

	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
		this.diet = diet;
	}

	public Boolean getDeParasitization() {
		return deParasitization;
	}

	public void setDeParasitization(Boolean deParasitization) {
		this.deParasitization = deParasitization;
	}

	public String getPsychomotorDev() {
		return psychomotorDev;
	}

	public void setPsychomotorDev(String psychomotorDev) {
		this.psychomotorDev = psychomotorDev;
	}

	public String getSomaticGrowth() {
		return somaticGrowth;
	}

	public void setSomaticGrowth(String somaticGrowth) {
		this.somaticGrowth = somaticGrowth;
	}

	public Boolean getIronSupplement() {
		return ironSupplement;
	}

	public void setIronSupplement(Boolean ironSupplement) {
		this.ironSupplement = ironSupplement;
	}

	public Boolean getFolicAcidSupplement() {
		return folicAcidSupplement;
	}

	public void setFolicAcidSupplement(Boolean folicAcidSupplement) {
		this.folicAcidSupplement = folicAcidSupplement;
	}

	public Boolean getVitASupplement() {
		return vitASupplement;
	}

	public void setVitASupplement(Boolean vitASupplement) {
		this.vitASupplement = vitASupplement;
	}

	public String getOtherSupplements() {
		return otherSupplements;
	}

	public void setOtherSupplements(String otherSupplements) {
		this.otherSupplements = otherSupplements;
	}

	public Boolean getTransfusion() {
		return transfusion;
	}

	public void setTransfusion(Boolean transfusion) {
		this.transfusion = transfusion;
	}

	public LocalDateTime getLastTransfusionDate() {
		return lastTransfusionDate;
	}

	public void setLastTransfusionDate(LocalDateTime lastTransfusionDate) {
		this.lastTransfusionDate = lastTransfusionDate;
	}

	public Boolean getSickleCell() {
		return sickleCell;
	}

	public void setSickleCell(Boolean sickleCell) {
		this.sickleCell = sickleCell;
	}

	public Boolean getDrugAllergy() {
		return drugAllergy;
	}

	public void setDrugAllergy(Boolean drugAllergy) {
		this.drugAllergy = drugAllergy;
	}

	public String getAllergyPrecision() {
		return allergyPrecision;
	}

	public void setAllergyPrecision(String allergyPrecision) {
		this.allergyPrecision = allergyPrecision;
	}

	public String getHemylosis() {
		return hemylosis;
	}

	public void setHemylosis(String hemylosis) {
		this.hemylosis = hemylosis;
	}

	public String getOtherPersonalPathologies() {
		return otherPersonalPathologies;
	}

	public void setOtherPersonalPathologies(String otherPersonalPathologies) {
		this.otherPersonalPathologies = otherPersonalPathologies;
	}

	public String getOtherFamilyPathologies() {
		return otherFamilyPathologies;
	}

	public void setOtherFamilyPathologies(String otherFamilyPathologies) {
		this.otherFamilyPathologies = otherFamilyPathologies;
	}

	public LocalDateTime getPerformedAt() {
		return performedAt;
	}

	public void setPerformedAt(LocalDateTime performedAt) {
		this.performedAt = performedAt;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
