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
package org.isf.care.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.isf.care.dto.CareDTO;
import org.isf.care.mapper.CareMapper;
import org.isf.cares.TestCare;
import org.isf.cares.model.Care;
import org.isf.conditioning.dto.ConditioningDTO;
import org.isf.patient.TestPatient;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CareHelper {

	private static ObjectMapper objectMapper;

	public static Care setup() throws OHException {
		TestPatient testPatient = new TestPatient();
		Patient patient = testPatient.setup(false);

		TestCare testCare = new TestCare();
		return testCare.setup(patient, false);
	}

	public static List<Care> setupCareList(int size) {
		return IntStream.range(0, size)
			.mapToObj(i -> {
				try {
					return CareHelper.setup();
				} catch (OHException e) {
					e.printStackTrace();
				}
				return null;
			})
			.collect(Collectors.toList());
	}

	public static String asJsonString(CareDTO careDTO) {
		try {
			return getObjectMapper().writeValueAsString(careDTO);
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

	public static CareDTO setup(CareMapper careMapper) throws OHException {
		return careMapper.map2DTO(CareHelper.setup());
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