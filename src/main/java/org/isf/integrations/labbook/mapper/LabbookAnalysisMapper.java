package org.isf.integrations.labbook.mapper;

import org.isf.integrations.labbook.dto.LabbookAnalysisDTO;
import org.isf.integrations.labbook.models.AnalysisResponse;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class LabbookAnalysisMapper extends GenericMapper<AnalysisResponse, LabbookAnalysisDTO> {
	public LabbookAnalysisMapper() {
		super(AnalysisResponse.class, LabbookAnalysisDTO.class);
	}

	@Override
	public LabbookAnalysisDTO map2DTO(AnalysisResponse entity) {
		LabbookAnalysisDTO dto = new LabbookAnalysisDTO();

		dto.setId(entity.id());
		dto.setRecordType(entity.recordType());
		dto.setPrescriptionDate(entity.prescriptionDate());
		dto.setAnalysis(entity.analysis());
		dto.setRecordNumber(entity.recordNumber());
		dto.setVariable(entity.variable());
		dto.setResult(entity.result());

		return dto;
	}
}
