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
package org.isf.priceslist.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.isf.priceslist.dto.PriceDTO;
import org.isf.priceslist.dto.PriceListDTO;
import org.isf.priceslist.manager.PriceListManager;
import org.isf.priceslist.mapper.PriceListMapper;
import org.isf.priceslist.mapper.PriceMapper;
import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.shared.mapper.mappings.PriceMapping;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

class PriceListControllerTest {

	@Mock
	private PriceListManager priceListManagerMock;

	private final PriceListMapper priceListMapper = new PriceListMapper();

	private final PriceMapper priceMapper = new PriceMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		ModelMapper modelMapper = new ModelMapper();
		ReflectionTestUtils.setField(priceListMapper, "modelMapper", modelMapper);
		ReflectionTestUtils.setField(priceMapper, "modelMapper", modelMapper);
		PriceMapping.addMapping(modelMapper);

		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new PriceListController(priceListManagerMock, priceListMapper, priceMapper))
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	private PriceList createPriceList(int id, String code, String name) {
		return new PriceList(id, code, name, "description " + name, "FCFA");
	}

	private Price createPrice(int id, PriceList list, String desc, double price) {
		return new Price(id, list, "GRP", "ITM", desc, price);
	}

	@Test
	void testGetPriceLists_200() throws Exception {
		List<PriceList> lists = List.of(
			createPriceList(1, "LST1", "List One"),
			createPriceList(2, "LST2", "List Two")
		);

		when(priceListManagerMock.getLists()).thenReturn(lists);

		mockMvc.perform(get("/pricelists"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].code", is("LST1")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].code", is("LST2")));

		verify(priceListManagerMock, times(1)).getLists();
	}

	@Test
	void testGetPrices_200() throws Exception {
		PriceList list = createPriceList(1, "LST1", "List One");
		List<Price> prices = List.of(createPrice(10, list, "Consultation", 1500.0));

		when(priceListManagerMock.getPrices()).thenReturn(prices);

		mockMvc.perform(get("/pricelists/prices"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].id", is(10)))
			.andExpect(jsonPath("$[0].description", is("Consultation")))
			.andExpect(jsonPath("$[0].price", is(1500.0)));

		verify(priceListManagerMock, times(1)).getPrices();
	}

	@Test
	void testCreatePriceList_Success_201() throws Exception {
		PriceListDTO dto = new PriceListDTO();
		dto.setCode("LST1");
		dto.setName("List One");
		dto.setDescription("description List One");
		dto.setCurrency("FCFA");

		PriceList saved = createPriceList(1, "LST1", "List One");

		when(priceListManagerMock.newList(any(PriceList.class))).thenReturn(saved);

		mockMvc.perform(post("/pricelists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.code", is("LST1")));

		verify(priceListManagerMock, times(1)).newList(any(PriceList.class));
	}

	@Test
	void testCreatePriceList_ValidationError_400() throws Exception {
		PriceListDTO dto = new PriceListDTO();
		dto.setCode("LST1");

		when(priceListManagerMock.newList(any(PriceList.class)))
			.thenThrow(new OHDataValidationException(Collections.singletonList(new OHExceptionMessage("Invalid price list"))));

		mockMvc.perform(post("/pricelists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest());

		verify(priceListManagerMock, times(1)).newList(any(PriceList.class));
	}

	@Test
	void testUpdatePriceList_Success_200() throws Exception {
		PriceListDTO dto = new PriceListDTO();
		dto.setId(1);
		dto.setCode("LST1");
		dto.setName("List One Updated");
		dto.setDescription("updated description");
		dto.setCurrency("FCFA");

		PriceList updated = createPriceList(1, "LST1", "List One Updated");

		when(priceListManagerMock.updateList(any(PriceList.class))).thenReturn(updated);

		mockMvc.perform(put("/pricelists/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("List One Updated")));

		verify(priceListManagerMock, times(1)).updateList(any(PriceList.class));
	}

	@Test
	void testUpdatePriceList_ServiceError_400() throws Exception {
		PriceListDTO dto = new PriceListDTO();
		dto.setId(1);
		dto.setCode("LST1");
		dto.setName("List One");
		dto.setDescription("description");
		dto.setCurrency("FCFA");

		when(priceListManagerMock.updateList(any(PriceList.class)))
			.thenThrow(new OHServiceException(new OHExceptionMessage("Update failed")));

		mockMvc.perform(put("/pricelists/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest());

		verify(priceListManagerMock, times(1)).updateList(any(PriceList.class));
	}

	@Test
	void testDeletePriceList_Success_200() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		doNothing().when(priceListManagerMock).deleteList(existing);

		mockMvc.perform(delete("/pricelists/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));

		verify(priceListManagerMock, times(1)).deleteList(existing);
	}

	@Test
	void testDeletePriceList_NotFound_404() throws Exception {
		when(priceListManagerMock.getLists()).thenReturn(List.of(createPriceList(2, "LST2", "List Two")));

		mockMvc.perform(delete("/pricelists/1"))
			.andExpect(status().isNotFound());

		verify(priceListManagerMock, never()).deleteList(any());
	}

	@Test
	void testDeletePriceList_ServiceError_400() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		doNothing().when(priceListManagerMock).deleteList(existing);
		org.mockito.Mockito.doThrow(new OHServiceException(new OHExceptionMessage("Delete failed")))
			.when(priceListManagerMock).deleteList(existing);

		mockMvc.perform(delete("/pricelists/1"))
			.andExpect(status().isBadRequest());

		verify(priceListManagerMock, times(1)).deleteList(existing);
	}

	@Test
	void testUpdatePrices_Success_200() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");

		PriceListDTO listDTO = new PriceListDTO();
		listDTO.setId(1);
		listDTO.setCode("LST1");
		listDTO.setName("List One");
		listDTO.setDescription("description");
		listDTO.setCurrency("FCFA");

		PriceDTO priceDTO = new PriceDTO();
		priceDTO.setId(10);
		priceDTO.setList(listDTO);
		priceDTO.setGroup("GRP");
		priceDTO.setItem("ITM");
		priceDTO.setDescription("Consultation");
		priceDTO.setPrice(2000.0);

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		doNothing().when(priceListManagerMock).updatePrices(eq(existing), any());

		mockMvc.perform(put("/pricelists/1/prices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(List.of(priceDTO))))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));

		ArgumentCaptor<List<Price>> captor = ArgumentCaptor.forClass(List.class);
		verify(priceListManagerMock, times(1)).updatePrices(eq(existing), captor.capture());
		List<Price> captured = captor.getValue();
		assertEquals(1, captured.size());
		assertEquals("Consultation", captured.get(0).getDesc());
		assertEquals(2000.0, captured.get(0).getPrice());
	}

	@Test
	void testUpdatePrices_NotFound_404() throws Exception {
		PriceDTO priceDTO = new PriceDTO();
		PriceListDTO listDTO = new PriceListDTO();
		listDTO.setId(1);
		priceDTO.setList(listDTO);
		priceDTO.setGroup("GRP");
		priceDTO.setItem("ITM");
		priceDTO.setDescription("Consultation");
		priceDTO.setPrice(2000.0);

		when(priceListManagerMock.getLists()).thenReturn(List.of(createPriceList(2, "LST2", "List Two")));

		mockMvc.perform(put("/pricelists/1/prices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(List.of(priceDTO))))
			.andExpect(status().isNotFound());

		verify(priceListManagerMock, never()).updatePrices(any(), any());
	}

	@Test
	void testUpdatePrices_ServiceError_400() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");

		PriceListDTO listDTO = new PriceListDTO();
		listDTO.setId(1);
		PriceDTO priceDTO = new PriceDTO();
		priceDTO.setList(listDTO);
		priceDTO.setGroup("GRP");
		priceDTO.setItem("ITM");
		priceDTO.setDescription("Consultation");
		priceDTO.setPrice(2000.0);

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		org.mockito.Mockito.doThrow(new OHServiceException(new OHExceptionMessage("Update failed")))
			.when(priceListManagerMock).updatePrices(eq(existing), any());

		mockMvc.perform(put("/pricelists/1/prices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(List.of(priceDTO))))
			.andExpect(status().isBadRequest());

		verify(priceListManagerMock, times(1)).updatePrices(eq(existing), any());
	}

	@Test
	void testCopyList_Success_200() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");
		PriceList duplicated = createPriceList(2, "LST1_COPY", "List One Copy");

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		when(priceListManagerMock.copyList(existing)).thenReturn(duplicated);

		mockMvc.perform(get("/pricelists/duplicate/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));

		verify(priceListManagerMock, times(1)).copyList(existing);
	}

	@Test
	void testCopyList_NotFound_404() throws Exception {
		when(priceListManagerMock.getLists()).thenReturn(List.of(createPriceList(2, "LST2", "List Two")));

		mockMvc.perform(get("/pricelists/duplicate/1"))
			.andExpect(status().isNotFound());

		verify(priceListManagerMock, never()).copyList(any(PriceList.class));
	}

	@Test
	void testCopyByFactorAndStep_Success_200() throws Exception {
		PriceList existing = createPriceList(1, "LST1", "List One");
		PriceList duplicated = createPriceList(2, "LST1_COPY", "List One Copy");

		when(priceListManagerMock.getLists()).thenReturn(List.of(existing));
		when(priceListManagerMock.copyList(existing, 1.5, 0.5)).thenReturn(duplicated);

		mockMvc.perform(get("/pricelists/duplicate/byfactor/1")
				.param("factor", "1.5")
				.param("step", "0.5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));

		verify(priceListManagerMock, times(1)).copyList(existing, 1.5, 0.5);
	}

	@Test
	void testCopyByFactorAndStep_NotFound_404() throws Exception {
		when(priceListManagerMock.getLists()).thenReturn(List.of(createPriceList(2, "LST2", "List Two")));

		mockMvc.perform(get("/pricelists/duplicate/byfactor/1")
				.param("factor", "1.5")
				.param("step", "0.5"))
			.andExpect(status().isNotFound());

		verify(priceListManagerMock, never()).copyList(any(PriceList.class), any(Double.class), any(Double.class));
	}
}
