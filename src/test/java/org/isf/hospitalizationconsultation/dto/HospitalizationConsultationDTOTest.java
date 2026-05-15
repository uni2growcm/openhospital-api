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
package org.isf.hospitalizationconsultation.dto;

import org.isf.encounter.dto.EncounterDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HospitalizationConsultationDTOTest {

	@Test
	void testGettersAndSetters() {
		// Given
		HospitalizationConsultationDTO dto = new HospitalizationConsultationDTO();
		Integer expectedId = 1;
		String expectedTeams = "Cardiology, Neurology";
		LocalDateTime expectedDateTime = LocalDateTime.now();
		String expectedParentComplaints = "Patient complains of chest pain";
		String expectedPhysicalExamination = "Normal heart sounds";
		String expectedDiagnosis = "Acute myocardial infarction";
		String expectedManagementPlan = "Start thrombolytic therapy";
		Integer expectedLock = 1;

		// When
		dto.setId(expectedId);
		dto.setTeams(expectedTeams);
		dto.setConsultationDate(expectedDateTime);
		dto.setParentComplaints(expectedParentComplaints);
		dto.setPhysicalExamination(expectedPhysicalExamination);
		dto.setDiagnosis(expectedDiagnosis);
		dto.setManagementPlan(expectedManagementPlan);
		dto.setLock(expectedLock);

		// Then
		assertEquals(expectedId, dto.getId());
		assertEquals(expectedTeams, dto.getTeams());
		assertEquals(expectedDateTime, dto.getConsultationDate());
		assertEquals(expectedParentComplaints, dto.getParentComplaints());
		assertEquals(expectedPhysicalExamination, dto.getPhysicalExamination());
		assertEquals(expectedDiagnosis, dto.getDiagnosis());
		assertEquals(expectedManagementPlan, dto.getManagementPlan());
		assertEquals(expectedLock, dto.getLock());
	}

	@Test
	void testEncounterGetterSetter() {
		// Given
		HospitalizationConsultationDTO dto = new HospitalizationConsultationDTO();
		EncounterDTO expectedEncounter = new EncounterDTO();
		expectedEncounter.setId(1);
		expectedEncounter.setCode("ENC_001");

		// When
		dto.setEncounter(expectedEncounter);

		// Then
		assertEquals(expectedEncounter, dto.getEncounter());
		assertEquals(expectedEncounter.getId(), dto.getEncounter().getId());
		assertEquals(expectedEncounter.getCode(), dto.getEncounter().getCode());
	}

	@Test
	void testDefaultConstructor() {
		// When
		HospitalizationConsultationDTO dto = new HospitalizationConsultationDTO();

		// Then
		assertNotNull(dto);
		assertNull(dto.getId());
		assertNull(dto.getTeams());
		assertNull(dto.getConsultationDate());
		assertNull(dto.getParentComplaints());
		assertNull(dto.getPhysicalExamination());
		assertNull(dto.getDiagnosis());
		assertNull(dto.getManagementPlan());
		assertNull(dto.getLock());
		assertNull(dto.getEncounter());
	}

	@Test
	void testSetNullValues() {
		// Given
		HospitalizationConsultationDTO dto = new HospitalizationConsultationDTO();
		dto.setId(1);
		dto.setTeams("test");
		dto.setConsultationDate(LocalDateTime.now());
		dto.setParentComplaints("test");
		dto.setPhysicalExamination("test");
		dto.setDiagnosis("test");
		dto.setManagementPlan("test");
		dto.setLock(1);
		dto.setEncounter(new EncounterDTO());

		// When
		dto.setId(null);
		dto.setTeams(null);
		dto.setConsultationDate(null);
		dto.setParentComplaints(null);
		dto.setPhysicalExamination(null);
		dto.setDiagnosis(null);
		dto.setManagementPlan(null);
		dto.setLock(null);
		dto.setEncounter(null);

		// Then
		assertNull(dto.getId());
		assertNull(dto.getTeams());
		assertNull(dto.getConsultationDate());
		assertNull(dto.getParentComplaints());
		assertNull(dto.getPhysicalExamination());
		assertNull(dto.getDiagnosis());
		assertNull(dto.getManagementPlan());
		assertNull(dto.getLock());
		assertNull(dto.getEncounter());
	}
}
