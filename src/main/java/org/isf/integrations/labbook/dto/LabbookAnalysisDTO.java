package org.isf.integrations.labbook.dto;

public class LabbookAnalysisDTO {

	private Integer id;

	private String recordType;

	private String prescriptionDate;

	private String analysis;

	private String recordNumber;

	private String variable;

	private String result;

	public LabbookAnalysisDTO() {}

	public LabbookAnalysisDTO(Integer id, String recordType, String prescriptionDate, String analysis, String recordNumber, String variable, String result) {
		this.id = id;
		this.recordType = recordType;
		this.prescriptionDate = prescriptionDate;
		this.analysis = analysis;
		this.recordNumber = recordNumber;
		this.variable = variable;
		this.result = result;
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

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
