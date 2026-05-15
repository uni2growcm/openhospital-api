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
package org.isf.hospitalizationconsultation.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.isf.encounter.data.EncounterHelper;
import org.isf.encounter.mapper.EncounterMapper;
import org.isf.hospitalizationconsultation.dto.HospitalizationConsultationDTO;
import org.isf.hospitalizationconsultation.mapper.HospitalizationConsultationMapper;
import org.isf.hospitalizationconsultation.model.HospitalizationConsultation;
import org.isf.utils.exception.OHException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HospitalizationConsultationHelper {

	private static ObjectMapper objectMapper;

	public static HospitalizationConsultation setup() throws OHException {
		HospitalizationConsultation consultation = new HospitalizationConsultation();
		consultation.setId(1);
		consultation.setEncounter(EncounterHelper.setup());
		consultation.setTeams("Cardiology, Neurology");
		consultation.setConsultationDate(LocalDateTime.now());
		consultation.setParentComplaints("Patient complains of chest pain and shortness of breath");
		consultation.setPhysicalExamination("Normal heart sounds, clear lungs");
		consultation.setDiagnosis("Acute myocardial infarction");
		consultation.setManagementPlan("Start thrombolytic therapy, monitor vitals");
		consultation.setLock(1);
		return consultation;
	}

	public static List<HospitalizationConsultation> setupHospitalizationConsultationList(int size) {
		return IntStream.range(0, size)
			.mapToObj(i -> {
				try {
					HospitalizationConsultation consultation = setup();
					consultation.setId(i + 1);
					return consultation;
				} catch (OHException e) {
					e.printStackTrace();
				}
				return null;
			})
			.collect(Collectors.toList());
	}

	public static HospitalizationConsultationDTO setup(HospitalizationConsultationMapper mapper) throws OHException {
		return mapper.map2DTO(setup());
	}

	public static HospitalizationConsultationDTO setup(HospitalizationConsultationMapper mapper, EncounterMapper encounterMapper) throws OHException {
		HospitalizationConsultation consultation = setup();
		HospitalizationConsultationDTO dto = mapper.map2DTO(consultation);
		dto.setEncounter(encounterMapper.map2DTO(consultation.getEncounter()));
		return dto;
	}

	public static String asJsonString(HospitalizationConsultationDTO consultationDTO) {
		try {
			return getObjectMapper().writeValueAsString(consultationDTO);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String asJsonString(List<?> list) {
		try {
			return getObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper()
				.registerModule(new ParameterNamesModule())
				.registerModule(new Jdk8Module())
				.registerModule(new JavaTimeModule());
		}
		return objectMapper;
	}
}
