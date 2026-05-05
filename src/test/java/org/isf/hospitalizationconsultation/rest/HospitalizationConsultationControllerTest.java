/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2025 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.hospitalizationconsultation.rest;

import org.isf.encounter.data.EncounterHelper;
import org.isf.encounter.dto.EncounterDTO;
import org.isf.encounter.manager.EncounterBrowserManager;
import org.isf.encounter.mapper.EncounterMapper;
import org.isf.encounter.model.Encounter;
import org.isf.hospitalizationconsultation.data.HospitalizationConsultationHelper;
import org.isf.hospitalizationconsultation.dto.HospitalizationConsultationDTO;
import org.isf.hospitalizationconsultation.manager.HospitalizationConsultationBrowserManager;
import org.isf.hospitalizationconsultation.mapper.HospitalizationConsultationMapper;
import org.isf.hospitalizationconsultation.model.HospitalizationConsultation;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.shared.mapper.mappings.PatientMapping;
import org.isf.shared.mapper.converter.BlobToByteArrayConverter;
import org.isf.shared.mapper.converter.ByteArrayToBlobConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HospitalizationConsultationControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HospitalizationConsultationControllerTest.class);

	@Mock
	protected HospitalizationConsultationBrowserManager hospitalizationConsultationBrowserManagerMock;

	@Mock
	protected EncounterBrowserManager encounterBrowserManagerMock;

	protected HospitalizationConsultationMapper hospitalizationConsultationMapper = new HospitalizationConsultationMapper();
	protected EncounterMapper encounterMapper = new EncounterMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new HospitalizationConsultationController(
				hospitalizationConsultationBrowserManagerMock,
				hospitalizationConsultationMapper,
				encounterBrowserManagerMock
			))
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(new BlobToByteArrayConverter());
		modelMapper.addConverter(new ByteArrayToBlobConverter());
		PatientMapping.addMapping(modelMapper);
		ReflectionTestUtils.setField(hospitalizationConsultationMapper, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(encounterMapper, "modelMapper", modelMapper);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	void testCreateHospitalizationConsultation_success() throws Exception {
		String request = "/hospitalizationconsultations";

		// Create test encounter directly
		EncounterDTO encounterDTO = new EncounterDTO();
		encounterDTO.setId(1);
		encounterDTO.setCode("ENC001");
		
		// Create test hospitalization consultation
		HospitalizationConsultationDTO body = new HospitalizationConsultationDTO();
		body.setEncounter(encounterDTO);
		body.setTeams("Cardiology, Neurology");
		body.setDateTime(java.time.LocalDateTime.now());
		body.setParentComplaints("Patient complains of chest pain and shortness of breath");
		body.setPhysicalExamination("Normal heart sounds, clear lungs");
		body.setDiagnosis("Acute myocardial infarction");
		body.setManagementPlan("Start thrombolytic therapy, monitor vitals");
		body.setLock(1);
		
		Encounter encounter = encounterMapper.map2Model(body.getEncounter());
		HospitalizationConsultation consultation = hospitalizationConsultationMapper.map2Model(body);

		when(encounterBrowserManagerMock.getEncountersByCode(body.getEncounter().getCode()))
			.thenReturn(encounter);

		when(hospitalizationConsultationBrowserManagerMock.newHospitalizationConsultation(any(HospitalizationConsultation.class)))
			.thenReturn(consultation);

		String jsonContent = Objects.requireNonNull(HospitalizationConsultationHelper.asJsonString(body));
		LOGGER.debug("Request JSON: {}", jsonContent);
		
		MvcResult result = this.mockMvc
			.perform(post(request)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			)
			.andDo(log())
			.andExpect(status().isCreated())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testCreateHospitalizationConsultation_encounterNotFound() throws Exception {
		String request = "/hospitalizationconsultations";

		HospitalizationConsultationDTO body = HospitalizationConsultationHelper.setup(hospitalizationConsultationMapper, encounterMapper);

		when(encounterBrowserManagerMock.getEncountersByCode(body.getEncounter().getCode()))
			.thenReturn(null);

		MvcResult result = this.mockMvc
			.perform(post(request)
				.contentType(MediaType.APPLICATION_JSON)
				.content(Objects.requireNonNull(HospitalizationConsultationHelper.asJsonString(body)))
			)
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testCreateHospitalizationConsultation_nullEncounter() throws Exception {
		String request = "/hospitalizationconsultations";

		HospitalizationConsultationDTO body = HospitalizationConsultationHelper.setup(hospitalizationConsultationMapper);
		body.setEncounter(null);

		MvcResult result = this.mockMvc
			.perform(post(request)
				.contentType(MediaType.APPLICATION_JSON)
				.content(Objects.requireNonNull(HospitalizationConsultationHelper.asJsonString(body)))
			)
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultations_success() throws Exception {
		String request = "/hospitalizationconsultations";

		List<HospitalizationConsultation> consultations = HospitalizationConsultationHelper.setupHospitalizationConsultationList(2);

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultations())
			.thenReturn(consultations);

		MvcResult result = this.mockMvc
			.perform(get(request))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(consultations.size()))
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultations_empty() throws Exception {
		String request = "/hospitalizationconsultations";

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultations())
			.thenReturn(Collections.emptyList());

		MvcResult result = this.mockMvc
			.perform(get(request))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(0))
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultationById_success() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 1;

		HospitalizationConsultation consultation = HospitalizationConsultationHelper.setup();

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(consultation);

		MvcResult result = this.mockMvc
			.perform(get(request, id))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultationById_notFound() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 999;

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(null);

		MvcResult result = this.mockMvc
			.perform(get(request, id))
			.andDo(log())
			.andExpect(status().isNotFound())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultationsByEncounter_success() throws Exception {
		String request = "/hospitalizationconsultations/encounter/{encounterCode}";
		String encounterCode = "ENC_001";

		Encounter encounter = EncounterHelper.setup();
		List<HospitalizationConsultation> consultations = HospitalizationConsultationHelper.setupHospitalizationConsultationList(2);

		when(encounterBrowserManagerMock.getEncountersByCode(encounterCode))
			.thenReturn(encounter);

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultationsByEncounter(encounter))
			.thenReturn(consultations);

		MvcResult result = this.mockMvc
			.perform(get(request, encounterCode))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(consultations.size()))
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultationsByEncounter_notFound() throws Exception {
		String request = "/hospitalizationconsultations/encounter/{encounterCode}";
		String encounterCode = "NOT_EXIST";

		when(encounterBrowserManagerMock.getEncountersByCode(encounterCode))
			.thenReturn(null);

		MvcResult result = this.mockMvc
			.perform(get(request, encounterCode))
			.andDo(log())
			.andExpect(status().isNotFound())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetHospitalizationConsultationsByDateRange_success() throws Exception {
		String request = "/hospitalizationconsultations/daterange";
		LocalDateTime dateFrom = LocalDateTime.now().minusDays(7);
		LocalDateTime dateTo = LocalDateTime.now();

		List<HospitalizationConsultation> consultations = HospitalizationConsultationHelper.setupHospitalizationConsultationList(2);

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultationsByDateRange(dateFrom, dateTo))
			.thenReturn(consultations);

		MvcResult result = this.mockMvc
			.perform(get(request)
				.param("dateFrom", dateFrom.toString())
				.param("dateTo", dateTo.toString()))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(consultations.size()))
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testUpdateHospitalizationConsultation_success() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 1;

		// Create test encounter directly
		EncounterDTO encounterDTO = new EncounterDTO();
		encounterDTO.setId(1);
		encounterDTO.setCode("ENC001");
		
		// Create test hospitalization consultation
		HospitalizationConsultationDTO updateDTO = new HospitalizationConsultationDTO();
		updateDTO.setEncounter(encounterDTO);
		updateDTO.setTeams("Cardiology, Neurology");
		updateDTO.setDateTime(java.time.LocalDateTime.now());
		updateDTO.setParentComplaints("Patient complains of chest pain and shortness of breath");
		updateDTO.setPhysicalExamination("Normal heart sounds, clear lungs");
		updateDTO.setDiagnosis("Acute myocardial infarction");
		updateDTO.setManagementPlan("Start thrombolytic therapy, monitor vitals");
		updateDTO.setLock(1);
		
		// Create existing consultation directly to avoid missing TestEncounter class
		HospitalizationConsultation existingConsultation = new HospitalizationConsultation();
		existingConsultation.setId(1);
		existingConsultation.setEncounter(new Encounter()); // This will be set by mock
		existingConsultation.setTeams("Cardiology, Neurology");
		existingConsultation.setDateTime(java.time.LocalDateTime.now());
		existingConsultation.setParentComplaints("Patient complains of chest pain and shortness of breath");
		existingConsultation.setPhysicalExamination("Normal heart sounds, clear lungs");
		existingConsultation.setDiagnosis("Acute myocardial infarction");
		existingConsultation.setManagementPlan("Start thrombolytic therapy, monitor vitals");
		existingConsultation.setLock(1);
		HospitalizationConsultation updatedConsultation = hospitalizationConsultationMapper.map2Model(updateDTO);

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(existingConsultation);

		// Mock encounter lookup for update
		Encounter mockEncounter = new Encounter();
		mockEncounter.setId(1);
		mockEncounter.setCode("ENC001");
		when(encounterBrowserManagerMock.getEncountersByCode("ENC001"))
			.thenReturn(mockEncounter);

		when(hospitalizationConsultationBrowserManagerMock.updateHospitalizationConsultation(any(HospitalizationConsultation.class)))
			.thenReturn(updatedConsultation);

		String jsonContent = Objects.requireNonNull(HospitalizationConsultationHelper.asJsonString(updateDTO));
		LOGGER.debug("Request JSON: {}", jsonContent);
		
		MvcResult result = this.mockMvc
			.perform(put(request, id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			)
			.andDo(log())
			.andExpect(status().isOk())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testUpdateHospitalizationConsultation_notFound() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 999;

		HospitalizationConsultationDTO updateDTO = HospitalizationConsultationHelper.setup(hospitalizationConsultationMapper);

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(null);

		MvcResult result = this.mockMvc
			.perform(put(request, id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(Objects.requireNonNull(HospitalizationConsultationHelper.asJsonString(updateDTO)))
			)
			.andDo(log())
			.andExpect(status().isNotFound())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testDeleteHospitalizationConsultation_success() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 1;

		HospitalizationConsultation consultation = HospitalizationConsultationHelper.setup();

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(consultation);

		MvcResult result = this.mockMvc
			.perform(delete(request, id))
			.andDo(log())
			.andExpect(status().isNoContent())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testDeleteHospitalizationConsultation_notFound() throws Exception {
		String request = "/hospitalizationconsultations/{id}";
		int id = 999;

		when(hospitalizationConsultationBrowserManagerMock.getHospitalizationConsultation(id))
			.thenReturn(null);

		MvcResult result = this.mockMvc
			.perform(delete(request, id))
			.andDo(log())
			.andExpect(status().isNotFound())
			.andReturn();

		LOGGER.debug("result: {}", result);
	}
}
