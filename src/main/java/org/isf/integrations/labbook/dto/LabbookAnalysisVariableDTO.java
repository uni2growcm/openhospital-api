package org.isf.integrations.labbook.dto;

public class LabbookAnalysisVariableDTO {

	private String variable;
	private String result;

	public LabbookAnalysisVariableDTO() {
	}

	public LabbookAnalysisVariableDTO(String variable, String result) {
		this.variable = variable;
		this.result = result;
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
