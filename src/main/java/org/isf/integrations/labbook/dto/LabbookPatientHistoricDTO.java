package org.isf.integrations.labbook.dto;

import java.util.List;

public class LabbookPatientHistoricDTO {

	private LabbookRawPatientDTO patient;

	private List<LabbookAnalysisDTO> analyzes;

	public LabbookPatientHistoricDTO() {
	}

	public LabbookPatientHistoricDTO(LabbookRawPatientDTO patient, List<LabbookAnalysisDTO> analyzes) {
		this.patient = patient;
		this.analyzes = analyzes;
	}

	public LabbookRawPatientDTO getPatient() {
		return patient;
	}

	public void setPatient(LabbookRawPatientDTO patient) {
		this.patient = patient;
	}

	public List<LabbookAnalysisDTO> getAnalyzes() {
		return analyzes;
	}

	public void setAnalyzes(List<LabbookAnalysisDTO> analyzes) {
		this.analyzes = analyzes;
	}
}
