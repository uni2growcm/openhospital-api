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
package org.isf.admission.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.isf.admtype.dto.AdmissionTypeDTO;
import org.isf.disctype.dto.DischargeTypeDTO;
import org.isf.disease.dto.DiseaseDTO;
import org.isf.dlvrrestype.dto.DeliveryResultTypeDTO;
import org.isf.dlvrtype.dto.DeliveryTypeDTO;
import org.isf.operation.dto.OperationDTO;
import org.isf.patient.dto.PatientDTO;
import org.isf.pregtreattype.dto.PregnantTreatmentTypeDTO;
import org.isf.ward.dto.WardDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

/**
 * @author gildas
 */
public class AdmissionDTO {

	@Schema(description = "Admission key", example = "12")
	private int id;

	@NotNull
	@Schema(description = "If admitted or not", example = "0")
	private int admitted;

	@NotNull
	@Schema(description = "Type of admission", example = "N")
	private String type;

	@Schema(description = "Ward")
	private WardDTO ward;

	@NotNull
	@Schema(description = "A progr. in year for each ward", example = "1")
	private int yProg;

	@Schema(description = "Patient")
	private PatientDTO patient;

	@NotNull
	@Schema(description = "Admission date", type = "string")
	private LocalDateTime admDate;

	@Schema(description = "Admission type")
	private AdmissionTypeDTO admType;

	@Schema(description = "FromHealthUnit")
	private String FHU;

	@Schema(description = "Complication Diagnosis")
	private String complication;

	@Schema(description = "Diagnosis out")
	private List<DiseaseDTO> diagnosisOut;

	@Schema(description = "Diagnosis in")
	private List<DiseaseDTO> diagnosisIn;

	@Schema(description = "Operation ")
	private OperationDTO operation;

	@Schema(description = "Operation date", type = "string")
	private LocalDateTime opDate;

	@Schema(description = "Operation result value is 'P' or 'N' ", example = "N")
	private String opResult;

	@Schema(description = "Discharge date", type = "string")
	private LocalDateTime disDate;

	@Schema(description = "DisChargeType")
	private DischargeTypeDTO disType;

	@Schema(description = "Anamnesis", maxLength = 65535)
	private String anamnesis;

	@Schema(description = "Transfusional unit")
	private Float transUnit;

	@Schema(description = "Visit date", type = "string")
	private LocalDateTime visitDate;

	@Schema(description = "TreatmentType ")
	private PregnantTreatmentTypeDTO pregTreatmentType;

	@Schema(description = "Delivery date", type = "string")
	private LocalDateTime deliveryDate;

	@Schema(description = "Delivery type")
	private DeliveryTypeDTO deliveryType;

	@Schema(description = "Delivery result type")
	private DeliveryResultTypeDTO deliveryResult;

	@Schema(description = "Weight")
	private Float weight;

	private Boolean alertReceived;

	private Boolean referenceSheet;

	private Boolean qualifiedAgent;

	private LocalDateTime ctrlDate1;

	private LocalDateTime ctrlDate2;

	private LocalDateTime abortDate;

	@Schema(
		description = "Treatment received by the patient before admission. " +
			"Example: medication, outpatient care, prior therapy.",
		example = "Antibiotic treatment before hospitalization"
	)
	private String preTreatment;

	@Schema(
		description = "Assessment performed before the patient's admission. " +
			"Example: preliminary examinations, lab results, medical observations.",
		example = "Blood test and X-ray prior to admission"
	)
	private String preAssessment;

	@Schema(
		description = "Reason for patient admission to hospital",
		example = "Headache"
	)
	private String entryReason;

	@Schema(description = "Transportation", example = "Moto")
	private String transportation;

	@Schema(description = "User id")
	private String userID;

	private int hashCode;

	@Schema(description = "Lock", example = "0")
	private int lock;

	@NotNull
	@Schema(description = "Flag record deleted, values are 'Y' OR 'N' ", example = "N")
	private String deleted;
	
	@Schema(description = "physical exam made by doctor", example = "The patient appears alert, oriented to person, place, and time, and in no acute distress. Well-nourished and well-hydrated.")
	private String physicalExam;

	@Schema(description = "course of action prescribed by the doctor", example = "Probabilistic antibiotic therapy with amoxicillin for 7 days.")
	private String courseOfAction;

	@Schema(description = "Next appointment date", example = "string")
	private LocalDateTime nextAppointment;

	@Schema(description = "Death period")
	private String deathPeriod;

	@Schema(description = "Referral alert", example = "Urgent referral needed")
	private String referralAlert;

	@Schema(description = "Referral reason", example = "Specialized treatment required")
	private String referralReason;

	@Schema(description = "Treatment received", example = "Antibiotic therapy")
	private String treatmentReceived;

	@Schema(description = "Outcome", example = "Recovered")
	private String outcome;

	@Schema(description = "Improvement feedback", example = "Patient showing significant improvement")
	private String improvementFeedback;

	public int getId() {
		return this.id;
	}

	public int getAdmitted() {
		return this.admitted;
	}

	public String getType() {
		return this.type;
	}

	public WardDTO getWard() {
		return this.ward;
	}

	public int getYProg() {
		return this.yProg;
	}

	public PatientDTO getPatient() {
		return this.patient;
	}

	public LocalDateTime getAdmDate() {
		return this.admDate;
	}

	public AdmissionTypeDTO getAdmType() {
		return this.admType;
	}

	public String getFHU() {
		return this.FHU;
	}

	public String getComplication() {
		return this.complication;
	}

	public OperationDTO getOperation() {
		return this.operation;
	}

	public LocalDateTime getOpDate() {
		return this.opDate;
	}

	public String getOpResult() {
		return this.opResult;
	}

	public LocalDateTime getDisDate() {
		return this.disDate;
	}

	public DischargeTypeDTO getDisType() {
		return this.disType;
	}

	public String getAnamnesis() {
		return this.anamnesis;
	}

	public Float getTransUnit() {
		return this.transUnit;
	}

	public LocalDateTime getVisitDate() {
		return this.visitDate;
	}

	public PregnantTreatmentTypeDTO getPregTreatmentType() {
		return this.pregTreatmentType;
	}

	public LocalDateTime getDeliveryDate() {
		return this.deliveryDate;
	}

	public DeliveryTypeDTO getDeliveryType() {
		return this.deliveryType;
	}

	public DeliveryResultTypeDTO getDeliveryResult() {
		return this.deliveryResult;
	}

	public Float getWeight() {
		return this.weight;
	}

	public LocalDateTime getCtrlDate1() {
		return this.ctrlDate1;
	}

	public LocalDateTime getCtrlDate2() {
		return this.ctrlDate2;
	}

	public LocalDateTime getAbortDate() {
		return this.abortDate;
	}

	private String othersInformation;

	public String getUserID() {
		return this.userID;
	}

	public String getDeleted() {
		return this.deleted;
	}

	@Schema(accessMode = AccessMode.READ_ONLY)
	public int getHashCode() {
		return hashCode;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAdmitted(int admitted) {
		this.admitted = admitted;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWard(WardDTO ward) {
		this.ward = ward;
	}

	public void setYProg(int yProg) {
		this.yProg = yProg;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public void setAdmDate(LocalDateTime admDate) {
		this.admDate = admDate;
	}

	public void setAdmType(AdmissionTypeDTO admType) {
		this.admType = admType;
	}

	public void setFHU(String FHU) {
		this.FHU = FHU;
	}

	public void setComplication(String complication) {
		this.complication = complication;
	}

	public void setOperation(OperationDTO operation) {
		this.operation = operation;
	}

	public void setOpDate(LocalDateTime opDate) {
		this.opDate = opDate;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public void setDisDate(LocalDateTime disDate) {
		this.disDate = disDate;
	}

	public void setDisType(DischargeTypeDTO disType) {
		this.disType = disType;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public void setTransUnit(Float transUnit) {
		this.transUnit = transUnit;
	}

	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	public void setPregTreatmentType(PregnantTreatmentTypeDTO pregTreatmentType) {
		this.pregTreatmentType = pregTreatmentType;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setDeliveryType(DeliveryTypeDTO deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setDeliveryResult(DeliveryResultTypeDTO deliveryResult) {
		this.deliveryResult = deliveryResult;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public void setCtrlDate1(LocalDateTime ctrlDate1) {
		this.ctrlDate1 = ctrlDate1;
	}

	public void setCtrlDate2(LocalDateTime ctrlDate2) {
		this.ctrlDate2 = ctrlDate2;
	}

	public void setAbortDate(LocalDateTime abortDate) {
		this.abortDate = abortDate;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getPreTreatment() {
		return preTreatment;
	}

	public void setPreTreatment(String preTreatment) {
		this.preTreatment = preTreatment;
	}

	public String getPreAssessment() {
		return preAssessment;
	}

	public void setPreAssessment(String preAssessment) {
		this.preAssessment = preAssessment;
	}

	public String getEntryReason() {
		return entryReason;
	}

	public void setEntryReason(String entryReason) {
		this.entryReason = entryReason;
	}

	public String getTransportation() {
		return transportation;
	}

	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

	public String getOthersInformation() {
		return othersInformation;
	}

	public void setOthersInformation(String othersInformation) {
		this.othersInformation = othersInformation;
	}

	public Boolean getReferenceSheet() {
		return referenceSheet;
	}

	public void setReferenceSheet(Boolean referenceSheet) {
		this.referenceSheet = referenceSheet;
	}

	public Boolean getAlertReceived() {
		return alertReceived;
	}

	public void setAlertReceived(Boolean alertReceived) {
		this.alertReceived = alertReceived;
	}

	public Boolean getQualifiedAgent() {
		return qualifiedAgent;
	}

	public void setQualifiedAgent(Boolean qualifiedAgent) {
		this.qualifiedAgent = qualifiedAgent;
	}

	public LocalDateTime getNextAppointment() {
		return nextAppointment;
	}

	public void setNextAppointment(LocalDateTime nextAppointment) {
		this.nextAppointment = nextAppointment;
	}

	public String getPhysicalExam() {
		return physicalExam;
	}

	public void setPhysicalExam(String physicalExam) {
		this.physicalExam = physicalExam;
	}

	public String getCourseOfAction() {
		return courseOfAction;
	}

	public void setCourseOfAction(String courseOfAction) {
		this.courseOfAction = courseOfAction;
	}

	public String getDeathPeriod() {
		return deathPeriod;
	}

	public void setDeathPeriod(String deathPeriod) {
		this.deathPeriod = deathPeriod;
	}

	public String getReferralAlert() {
		return referralAlert;
	}

	public void setReferralAlert(String referralAlert) {
		this.referralAlert = referralAlert;
	}

	public String getReferralReason() {
		return referralReason;
	}

	public void setReferralReason(String referralReason) {
		this.referralReason = referralReason;
	}

	public String getTreatmentReceived() {
		return treatmentReceived;
	}

	public void setTreatmentReceived(String treatmentReceived) {
		this.treatmentReceived = treatmentReceived;
	}

	public String getOutcome() {
		return outcome;
	}

	public List<DiseaseDTO> getDiagnosisOut() {
		return diagnosisOut;
	}

	public void setDiagnosisOut(List<DiseaseDTO> diagnosisOut) {
		this.diagnosisOut = diagnosisOut;
	}

	public List<DiseaseDTO> getDiagnosisIn() {
		return diagnosisIn;
	}

	public void setDiagnosisIn(List<DiseaseDTO> diagnosisIn) {
		this.diagnosisIn = diagnosisIn;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getImprovementFeedback() {
		return improvementFeedback;
	}

	public void setImprovementFeedback(String improvementFeedback) {
		this.improvementFeedback = improvementFeedback;
	}
}
