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
package org.isf.staff.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.EntityNotFoundException;

import org.isf.homevisit.manager.StaffBrowserManager;
import org.isf.homevisit.model.Staff;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.shared.mapper.converter.BlobToByteArrayConverter;
import org.isf.shared.mapper.converter.ByteArrayToBlobConverter;
import org.isf.staff.data.StaffHelper;
import org.isf.staff.dto.StaffDTO;
import org.isf.staff.mapper.StaffMapper;
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

class StaffControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffControllerTest.class);

	@Mock
	protected StaffBrowserManager staffBrowserManagerMock;

	protected StaffMapper staffMapper = new StaffMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new StaffController(staffBrowserManagerMock, staffMapper))
				.setControllerAdvice(new OHResponseEntityExceptionHandler())
				.build();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(new BlobToByteArrayConverter());
		modelMapper.addConverter(new ByteArrayToBlobConverter());
		ReflectionTestUtils.setField(staffMapper, "modelMapper", modelMapper);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	void testGetStaff_200() throws Exception {
		String request = "/staff";

		List<Staff> staffList = StaffHelper.setupStaffList(4);

		when(staffBrowserManagerMock.getStaff()).thenReturn(staffList);

		List<StaffDTO> expectedStaffDTOs = staffMapper.map2DTOList(staffList);

		MvcResult result = this.mockMvc
				.perform(get(request))
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(StaffHelper.getObjectMapper().writeValueAsString(expectedStaffDTOs))))
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetStaffByCode_200() throws Exception {
		String request = "/staff/{code}";
		int code = 1;

		Staff staff = StaffHelper.setup(code);

		when(staffBrowserManagerMock.getStaff(code)).thenReturn(staff);

		StaffDTO expectedStaffDTO = staffMapper.map2DTO(staff);

		MvcResult result = this.mockMvc
				.perform(get(request, code))
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(StaffHelper.getObjectMapper().writeValueAsString(expectedStaffDTO))))
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testGetStaffByCode_404() throws Exception {
		String request = "/staff/{code}";
		int code = 99;

		when(staffBrowserManagerMock.getStaff(code))
				.thenThrow(new EntityNotFoundException());

		MvcResult result = this.mockMvc
				.perform(get(request, code))
				.andDo(log())
				.andExpect(status().isNotFound())
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testSearchStaff_200() throws Exception {
		String request = "/staff/search";
		String keyword = "nurse";

		List<Staff> staffList = StaffHelper.setupStaffList(2);

		when(staffBrowserManagerMock.searchStaff(keyword))
				.thenReturn(staffList);

		List<StaffDTO> expectedStaffDTOs = staffMapper.map2DTOList(staffList);

		MvcResult result = this.mockMvc
				.perform(get(request).param("keyword", keyword))
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(StaffHelper.getObjectMapper().writeValueAsString(expectedStaffDTOs))))
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testNewStaff_201() throws Exception {
		String request = "/staff";
		int code = 1;
		Staff staff = StaffHelper.setup(code);
		StaffDTO body = staffMapper.map2DTO(staff);

		when(staffBrowserManagerMock.saveStaff(any(Staff.class)))
				.thenReturn(staff);

		MvcResult result = this.mockMvc
				.perform(post(request)
						.contentType(MediaType.APPLICATION_JSON)
						.content(Objects.requireNonNull(StaffHelper.asJsonString(body)))
				)
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isCreated())
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testUpdateStaff_200() throws Exception {
		String request = "/staff";
		int code = 1;
		Staff staff = StaffHelper.setup(code);
		StaffDTO body = staffMapper.map2DTO(staff);

		when(staffBrowserManagerMock.saveStaff(any(Staff.class)))
				.thenReturn(staff);

		MvcResult result = this.mockMvc
				.perform(put(request)
						.contentType(MediaType.APPLICATION_JSON)
						.content(Objects.requireNonNull(StaffHelper.asJsonString(body)))
				)
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testDeleteStaff_200() throws Exception {
		String request = "/staff/{code}";
		int code = 1;

		Staff staff = StaffHelper.setup(code);

		when(staffBrowserManagerMock.getStaff(code))
				.thenReturn(staff);

		String isDeleted = "true";
		MvcResult result = this.mockMvc
				.perform(delete(request, code))
				.andDo(log())
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(isDeleted)))
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

	@Test
	void testDeleteStaff_404() throws Exception {
		String request = "/staff/{code}";
		int code = 99;

		when(staffBrowserManagerMock.getStaff(code))
				.thenThrow(new EntityNotFoundException());

		MvcResult result = this.mockMvc
				.perform(delete(request, code))
				.andDo(log())
				.andExpect(status().isNotFound())
				.andReturn();

		LOGGER.debug("result: {}", result);
	}

}