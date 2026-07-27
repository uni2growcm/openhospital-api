package org.isf.integrations.labbook.dto;

import java.util.List;

public class LabbookAnalysisDTO {

	private Integer id;

	private String recordType;

	private String prescriptionDate;

	private String analysis;

	private String recordNumber;

	private List<LabbookAnalysisVariableDTO> variables;

	public LabbookAnalysisDTO() {}

	public LabbookAnalysisDTO(Integer id, String recordType, String prescriptionDate, String analysis, String recordNumber, List<LabbookAnalysisVariableDTO> variables) {
		this.id = id;
		this.recordType = recordType;
		this.prescriptionDate = prescriptionDate;
		this.analysis = analysis;
		this.recordNumber = recordNumber;
		this.variables = variables;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(String prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}

	public List<LabbookAnalysisVariableDTO> getVariables() {
		return variables;
	}

	public void setVariables(List<LabbookAnalysisVariableDTO> variables) {
		this.variables = variables;
	}
}
