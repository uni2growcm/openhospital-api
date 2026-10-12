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

package org.isf.reductionplan.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.isf.reductionplan.dto.ReductionPlanDTO;
import org.isf.reductionplan.manager.ReductionPlanManager;
import org.isf.reductionplan.mapper.ReductionPlanMapper;
import org.isf.reductionplan.model.ReductionPlan;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReductionPlanControllerTest {

	@Mock
	private ReductionPlanManager reductionPlanManagerMock;

	private final ReductionPlanMapper mapper = new ReductionPlanMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		ModelMapper modelMapper = new ModelMapper();
		ReflectionTestUtils.setField(mapper, "modelMapper", modelMapper);

		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new ReductionPlanController(reductionPlanManagerMock, mapper))
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	private ReductionPlan createReductionPlan(int id, String description, BigDecimal rate) {
		ReductionPlan plan = new ReductionPlan(id, description, rate, rate, rate, rate);
		plan.setLock(0);
		return plan;
	}

	@Test
	void testGetReductionPlans_NoQuery_200() throws Exception {
		List<ReductionPlan> plans = List.of(
			createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10)),
			createReductionPlan(2, "VIP Plan", BigDecimal.valueOf(20))
		);

		when(reductionPlanManagerMock.getAll()).thenReturn(plans);

		mockMvc.perform(get("/reduction-plans"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].description", is("Staff Plan")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].description", is("VIP Plan")));

		verify(reductionPlanManagerMock, times(1)).getAll();
	}

	@Test
	void testGetReductionPlans_WithQuery_200() throws Exception {
		List<ReductionPlan> plans = List.of(
			createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10))
		);

		when(reductionPlanManagerMock.getByDescription("Staff")).thenReturn(plans);

		mockMvc.perform(get("/reduction-plans").param("q", "Staff"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].description", is("Staff Plan")));

		verify(reductionPlanManagerMock, times(1)).getByDescription("Staff");
	}

	@Test
	void testGetReductionPlanById_Success_200() throws Exception {
		ReductionPlan plan = createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10));

		when(reductionPlanManagerMock.getById(1)).thenReturn(plan);

		mockMvc.perform(get("/reduction-plans/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.description", is("Staff Plan")));

		verify(reductionPlanManagerMock, times(1)).getById(1);
	}

	@Test
	void testGetReductionPlanById_NotFound_404() throws Exception {
		when(reductionPlanManagerMock.getById(999)).thenReturn(null);

		mockMvc.perform(get("/reduction-plans/999"))
			.andExpect(status().isNotFound());

		verify(reductionPlanManagerMock, times(1)).getById(999);
	}

	@Test
	void testCreateReductionPlan_Success_201() throws Exception {
		ReductionPlanDTO dto = new ReductionPlanDTO(null, "Staff Plan", BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), 0);
		ReductionPlan saved = createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10));

		when(reductionPlanManagerMock.add(any(ReductionPlan.class))).thenReturn(saved);

		mockMvc.perform(post("/reduction-plans")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.description", is("Staff Plan")));

		verify(reductionPlanManagerMock, times(1)).add(any(ReductionPlan.class));
	}

	@Test
	void testCreateReductionPlan_ValidationError_400() throws Exception {
		ReductionPlanDTO dto = new ReductionPlanDTO(null, "Invalid Plan", BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), 0);

		when(reductionPlanManagerMock.add(any(ReductionPlan.class)))
			.thenThrow(new OHDataValidationException(Collections.singletonList(new OHExceptionMessage("Rate invalid"))));

		mockMvc.perform(post("/reduction-plans")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest());

		verify(reductionPlanManagerMock, times(1)).add(any(ReductionPlan.class));
	}

	@Test
	void testUpdateReductionPlan_Success_200() throws Exception {
		ReductionPlan existing = createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10));
		ReductionPlanDTO updateDto = new ReductionPlanDTO(1, "Staff Plan Updated", BigDecimal.valueOf(15), BigDecimal.valueOf(15), BigDecimal.valueOf(15), BigDecimal.valueOf(15), 0);
		ReductionPlan updated = createReductionPlan(1, "Staff Plan Updated", BigDecimal.valueOf(15));

		when(reductionPlanManagerMock.getById(1)).thenReturn(existing);
		when(reductionPlanManagerMock.update(any(ReductionPlan.class))).thenReturn(updated);

		mockMvc.perform(put("/reduction-plans/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.description", is("Staff Plan Updated")));

		verify(reductionPlanManagerMock, times(1)).update(any(ReductionPlan.class));
	}

	@Test
	void testUpdateReductionPlan_NotFound_404() throws Exception {
		ReductionPlanDTO updateDto = new ReductionPlanDTO(999, "Non-existent", BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(10), 0);

		when(reductionPlanManagerMock.getById(999)).thenReturn(null);

		mockMvc.perform(put("/reduction-plans/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isNotFound());

		verify(reductionPlanManagerMock, never()).update(any());
	}

	@Test
	void testDeleteReductionPlan_Success_204() throws Exception {
		ReductionPlan existing = createReductionPlan(1, "Staff Plan", BigDecimal.valueOf(10));

		when(reductionPlanManagerMock.getById(1)).thenReturn(existing);
		doNothing().when(reductionPlanManagerMock).delete(existing);

		mockMvc.perform(delete("/reduction-plans/1"))
			.andExpect(status().isNoContent());

		verify(reductionPlanManagerMock, times(1)).delete(existing);
	}

	@Test
	void testDeleteReductionPlan_NotFound_404() throws Exception {
		when(reductionPlanManagerMock.getById(999)).thenReturn(null);

		mockMvc.perform(delete("/reduction-plans/999"))
			.andExpect(status().isNotFound());

		verify(reductionPlanManagerMock, never()).delete(any());
	}
}
