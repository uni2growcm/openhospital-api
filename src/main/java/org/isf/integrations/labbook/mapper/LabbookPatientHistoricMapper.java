/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.integrations.labbook.mapper;

import org.isf.integrations.labbook.dto.LabbookAnalysisDTO;
import org.isf.integrations.labbook.dto.LabbookAnalysisVariableDTO;
import org.isf.integrations.labbook.dto.LabbookPatientHistoricDTO;
import org.isf.integrations.labbook.dto.LabbookRawPatientDTO;
import org.isf.integrations.labbook.models.AnalysisResponse;
import org.isf.integrations.labbook.models.PatientHistoricResponse;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LabbookPatientHistoricMapper extends GenericMapper<PatientHistoricResponse, LabbookPatientHistoricDTO> {

	private final LabbookRawPatientMapper labbookRawPatientMapper;

	public LabbookPatientHistoricMapper(LabbookRawPatientMapper labbookRawPatientMapper) {
		super(PatientHistoricResponse.class, LabbookPatientHistoricDTO.class);
		this.labbookRawPatientMapper = labbookRawPatientMapper;
	}

	@Override
	public LabbookPatientHistoricDTO map2DTO(PatientHistoricResponse fromObj) {

		LabbookPatientHistoricDTO dto = new LabbookPatientHistoricDTO();

		dto.setPatient(
			labbookRawPatientMapper.map2DTO(fromObj.patient())
		);

		Map<String, List<AnalysisResponse>> grouped =
			fromObj.analyzes()
				.stream()
				.collect(Collectors.groupingBy(a ->
					a.recordNumber() + "|" +
						a.analysis() + "|" +
						a.prescriptionDate()
				));

		List<LabbookAnalysisDTO> analyses = grouped.values()
			.stream()
			.map(list -> {

				AnalysisResponse first = list.get(0);

				LabbookAnalysisDTO analysisDTO = new LabbookAnalysisDTO();

				analysisDTO.setId(first.id());
				analysisDTO.setRecordType(first.recordType());
				analysisDTO.setPrescriptionDate(first.prescriptionDate());
				analysisDTO.setAnalysis(first.analysis());
				analysisDTO.setRecordNumber(first.recordNumber());

				List<LabbookAnalysisVariableDTO> variables =
					list.stream()
						.map(a -> new LabbookAnalysisVariableDTO(
							a.variable(),
							a.result()
						))
						.toList();

				analysisDTO.setVariables(variables);

				return analysisDTO;
			})
			.toList();

		dto.setAnalyzes(analyses);

		return dto;
	}
}
