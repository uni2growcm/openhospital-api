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
import org.isf.integrations.labbook.dto.LabbookPatientHistoricDTO;
import org.isf.integrations.labbook.dto.LabbookRawPatientDTO;
import org.isf.integrations.labbook.models.PatientHistoricResponse;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class LabbookPatientHistoricMapper extends GenericMapper<PatientHistoricResponse, LabbookPatientHistoricDTO> {

	private final LabbookRawPatientMapper labbookRawPatientMapper;
	private final LabbookAnalysisMapper labbookAnalysisMapper;

	public LabbookPatientHistoricMapper(LabbookRawPatientMapper labbookRawPatientMapper, LabbookAnalysisMapper labbookAnalysisMapper) {
		super(PatientHistoricResponse.class, LabbookPatientHistoricDTO.class);
		this.labbookRawPatientMapper = labbookRawPatientMapper;
		this.labbookAnalysisMapper = labbookAnalysisMapper;
	}

	@Override
	public LabbookPatientHistoricDTO map2DTO(PatientHistoricResponse fromObj) {

		LabbookPatientHistoricDTO dto = new LabbookPatientHistoricDTO();

		dto.setPatient(
			labbookRawPatientMapper.map2DTO(fromObj.patient())
		);

		dto.setAnalyzes(
			fromObj.analyzes()
				.stream()
				.map(labbookAnalysisMapper::map2DTO)
				.toList()
		);

		return dto;
	}
}
