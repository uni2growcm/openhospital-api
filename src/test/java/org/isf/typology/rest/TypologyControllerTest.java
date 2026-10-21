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
package org.isf.typology.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.typology.data.TypologyHelper;
import org.isf.typology.dto.TypologyDTO;
import org.isf.typology.manager.TypologyBrowserManager;
import org.isf.typology.mapper.TypologyMapper;
import org.isf.typology.model.Family;
import org.isf.typology.model.Typology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

class TypologyControllerTest {

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	@Mock
	private TypologyBrowserManager typologyManager;

	private TypologyMapper mapper;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		mapper = new TypologyMapper();
		ReflectionTestUtils.setField(mapper, "modelMapper", new org.modelmapper.ModelMapper());

		TypologyController controller = new TypologyController(typologyManager, mapper);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();
	}

	@Test
	void testGetTypologiesByFamily() throws Exception {
		List<Typology> typologies = TypologyHelper.setupTypologyList(2);
		when(typologyManager.getTypologies(Family.VISITTYPE)).thenReturn(typologies);

		mockMvc.perform(get("/typologies?family=VISITTYPE"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("ANC")));

		mockMvc.perform(get("/typologies?family=angal.typology.visittype"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("ANC")));
	}

	@Test
	void testGetTypologyByCode() throws Exception {
		Typology typology = TypologyHelper.setup(1);
		when(typologyManager.getTypologyByCode("ANC1")).thenReturn(typology);

		mockMvc.perform(get("/typologies/ANC1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("ANC1")));
	}

	@Test
	void testCreateTypology() throws Exception {
		Typology typology = TypologyHelper.setup(1);
		TypologyDTO dto = TypologyHelper.setupDTO(1);
		when(typologyManager.newTypology(any(Typology.class))).thenReturn(typology);

		mockMvc.perform(post("/typologies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdateTypology() throws Exception {
		Typology typology = TypologyHelper.setup(1);
		TypologyDTO dto = TypologyHelper.setupDTO(1);
		when(typologyManager.isCodePresent(dto.getCode())).thenReturn(true);
		when(typologyManager.updateTypology(any(Typology.class))).thenReturn(typology);

		mockMvc.perform(put("/typologies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeleteTypology() throws Exception {
		Typology typology = TypologyHelper.setup(1);
		when(typologyManager.getTypologyByCode("ANC1")).thenReturn(typology);

		mockMvc.perform(delete("/typologies/ANC1"))
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
	}

	@Test
	void testCheckTypologyCode() throws Exception {
		when(typologyManager.isCodePresent("ANC1")).thenReturn(true);

		mockMvc.perform(get("/typologies/check/ANC1"))
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
	}

	@Test
	void testTypologyDTOSerialization() throws Exception {
		TypologyDTO dto = new TypologyDTO("VT1", "Visit Type 1", Family.VISITTYPE);
		String json = objectMapper.writeValueAsString(dto);
		org.junit.jupiter.api.Assertions.assertTrue(json.contains("\"family\":\"VISITTYPE\""));

		TypologyDTO fromEnumName = objectMapper.readValue("{\"code\":\"VT1\",\"description\":\"Visit Type 1\",\"family\":\"VISITTYPE\"}", TypologyDTO.class);
		org.junit.jupiter.api.Assertions.assertEquals(Family.VISITTYPE, fromEnumName.getFamily());

		TypologyDTO fromKey = objectMapper.readValue("{\"code\":\"VT1\",\"description\":\"Visit Type 1\",\"family\":\"angal.typology.visittype\"}", TypologyDTO.class);
		org.junit.jupiter.api.Assertions.assertEquals(Family.VISITTYPE, fromKey.getFamily());
	}
}
