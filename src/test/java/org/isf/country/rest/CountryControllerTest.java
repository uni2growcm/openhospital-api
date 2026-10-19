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
package org.isf.country.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.isf.country.dto.CountryDTO;
import org.isf.country.manager.CountryBrowserManager;
import org.isf.country.mapper.CountryMapper;
import org.isf.country.model.Country;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.utils.pagination.PageInfo;
import org.isf.utils.pagination.PagedResponse;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CountryControllerTest {

	@Mock
	private CountryBrowserManager countryBrowserManagerMock;

	private final CountryMapper countryMapper = new CountryMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		ModelMapper modelMapper = new ModelMapper();
		ReflectionTestUtils.setField(countryMapper, "modelMapper", modelMapper);

		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new CountryController(countryBrowserManagerMock, countryMapper))
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	private Country createCountry(int id, String isoCode, String name, String phoneCode) {
		Country country = new Country(id, isoCode, phoneCode, name);
		return country;
	}

	@Test
	void testGetAllCountries_200() throws Exception {
		List<Country> countries = List.of(
			createCountry(1, "CM", "Cameroon", "+237"),
			createCountry(2, "MG", "Madagascar", "+261")
		);

		when(countryBrowserManagerMock.getCountries()).thenReturn(countries);

		mockMvc.perform(get("/countries"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].isoCode", is("CM")))
			.andExpect(jsonPath("$[0].name", is("Cameroon")))
			.andExpect(jsonPath("$[1].isoCode", is("MG")))
			.andExpect(jsonPath("$[1].name", is("Madagascar")));

		verify(countryBrowserManagerMock, times(1)).getCountries();
	}

	@Test
	void testSearchCountries_200() throws Exception {
		List<Country> countries = List.of(
			createCountry(1, "CM", "Cameroon", "+237")
		);

		when(countryBrowserManagerMock.searchCountries("came")).thenReturn(countries);

		mockMvc.perform(get("/countries").param("search", "came"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is("Cameroon")));

		verify(countryBrowserManagerMock, times(1)).searchCountries("came");
		verify(countryBrowserManagerMock, never()).getCountries();
	}

	@Test
	void testGetCountriesPaged_200() throws Exception {
		List<Country> countries = List.of(
			createCountry(1, "CM", "Cameroon", "+237"),
			createCountry(2, "MG", "Madagascar", "+261")
		);
		PagedResponse<Country> pagedResponse = new PagedResponse<>();
		pagedResponse.setData(countries);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(0);
		pageInfo.setSize(10);
		pageInfo.setTotalPages(1);
		pageInfo.setTotalNbOfElements(2L);
		pagedResponse.setPageInfo(pageInfo);

		when(countryBrowserManagerMock.getCountries(0, 10)).thenReturn(pagedResponse);

		mockMvc.perform(get("/countries/paged").param("page", "0").param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].isoCode", is("CM")))
			.andExpect(jsonPath("$.pageInfo.totalNbOfElements", is(2)))
			.andExpect(jsonPath("$.pageInfo.totalPages", is(1)));

		verify(countryBrowserManagerMock, times(1)).getCountries(0, 10);
	}

	@Test
	void testGetCountryById_Success_200() throws Exception {
		Country country = createCountry(1, "CM", "Cameroon", "+237");
		when(countryBrowserManagerMock.getCountry(1)).thenReturn(country);

		mockMvc.perform(get("/countries/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.isoCode", is("CM")))
			.andExpect(jsonPath("$.name", is("Cameroon")))
			.andExpect(jsonPath("$.phoneCode", is("+237")));

		verify(countryBrowserManagerMock, times(1)).getCountry(1);
	}

	@Test
	void testGetCountryById_NotFound_404() throws Exception {
		when(countryBrowserManagerMock.getCountry(999))
			.thenThrow(new EntityNotFoundException("Country not found"));

		mockMvc.perform(get("/countries/999"))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.country.not_found")));

		verify(countryBrowserManagerMock, times(1)).getCountry(999);
	}

	@Test
	void testCreateCountry_Success_201() throws Exception {
		CountryDTO dto = new CountryDTO(null, "CM", "Cameroon", "+237");
		Country savedCountry = createCountry(1, "CM", "Cameroon", "+237");

		when(countryBrowserManagerMock.getArchivedCountryByIsoCode("CM")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getArchivedCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.isCodeUnique(eq("CM"), isNull())).thenReturn(true);
		when(countryBrowserManagerMock.getCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getActiveCountryByPhoneCode("+237")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.saveOrReactivateCountry(any(Country.class))).thenReturn(savedCountry);

		mockMvc.perform(post("/countries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.isoCode", is("CM")))
			.andExpect(jsonPath("$.name", is("Cameroon")))
			.andExpect(jsonPath("$.phoneCode", is("+237")));

		verify(countryBrowserManagerMock, times(1)).saveOrReactivateCountry(any(Country.class));
	}

	@Test
	void testCreateCountry_ReactivateArchived_201() throws Exception {
		CountryDTO dto = new CountryDTO(null, "GA", "Gabon", "+241");
		Country archived = createCountry(10, "GA", "Gabon", "+241");
		Country reactivated = createCountry(10, "GA", "Gabon", "+241");

		when(countryBrowserManagerMock.getArchivedCountryByIsoCode("GA")).thenReturn(Optional.of(archived));
		when(countryBrowserManagerMock.isCodeUnique(eq("GA"), eq(10))).thenReturn(true);
		when(countryBrowserManagerMock.getCountryByName("Gabon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getActiveCountryByPhoneCode("+241")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.saveOrReactivateCountry(any(Country.class))).thenReturn(reactivated);

		mockMvc.perform(post("/countries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(10)))
			.andExpect(jsonPath("$.isoCode", is("GA")))
			.andExpect(jsonPath("$.name", is("Gabon")));

		verify(countryBrowserManagerMock, times(1)).saveOrReactivateCountry(any(Country.class));
	}

	@Test
	void testCreateCountry_DuplicateIsoCode_409() throws Exception {
		CountryDTO dto = new CountryDTO(null, "CM", "Cameroon", "+237");

		when(countryBrowserManagerMock.getArchivedCountryByIsoCode("CM")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getArchivedCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.isCodeUnique(eq("CM"), isNull())).thenReturn(false);

		mockMvc.perform(post("/countries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isConflict())
			.andExpect(content().string(containsString("errors.country.isocode_exists")));

		verify(countryBrowserManagerMock, never()).saveOrReactivateCountry(any());
	}

	@Test
	void testCreateCountry_DuplicateName_409() throws Exception {
		CountryDTO dto = new CountryDTO(null, "CM", "Cameroon", "+237");
		Country existing = createCountry(5, "XX", "Cameroon", "+999");

		when(countryBrowserManagerMock.getArchivedCountryByIsoCode("CM")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getArchivedCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.isCodeUnique(eq("CM"), isNull())).thenReturn(true);
		when(countryBrowserManagerMock.getCountryByName("Cameroon")).thenReturn(Optional.of(existing));

		mockMvc.perform(post("/countries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isConflict())
			.andExpect(content().string(containsString("errors.country.name_exists")));

		verify(countryBrowserManagerMock, never()).saveOrReactivateCountry(any());
	}

	@Test
	void testCreateCountry_DuplicatePhoneCode_409() throws Exception {
		CountryDTO dto = new CountryDTO(null, "CM", "Cameroon", "+237");
		Country existing = createCountry(5, "XX", "Other", "+237");

		when(countryBrowserManagerMock.getArchivedCountryByIsoCode("CM")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getArchivedCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.isCodeUnique(eq("CM"), isNull())).thenReturn(true);
		when(countryBrowserManagerMock.getCountryByName("Cameroon")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getActiveCountryByPhoneCode("+237")).thenReturn(Optional.of(existing));

		mockMvc.perform(post("/countries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isConflict())
			.andExpect(content().string(containsString("errors.country.phonecode_exists")));

		verify(countryBrowserManagerMock, never()).saveOrReactivateCountry(any());
	}

	@Test
	void testUpdateCountry_Success_200() throws Exception {
		Country existing = createCountry(1, "CM", "Cameroon", "+237");
		CountryDTO updateDto = new CountryDTO(1, "CM", "Cameroon Republic", "+237");
		Country updated = createCountry(1, "CM", "Cameroon Republic", "+237");

		when(countryBrowserManagerMock.getCountry(1)).thenReturn(existing);
		when(countryBrowserManagerMock.isCodeUnique(eq("CM"), eq(1))).thenReturn(true);
		when(countryBrowserManagerMock.getCountryByName("Cameroon Republic")).thenReturn(Optional.empty());
		when(countryBrowserManagerMock.getActiveCountryByPhoneCode("+237")).thenReturn(Optional.of(existing));
		when(countryBrowserManagerMock.updateCountry(any(Country.class))).thenReturn(updated);

		mockMvc.perform(put("/countries/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Cameroon Republic")));

		verify(countryBrowserManagerMock, times(1)).updateCountry(any(Country.class));
	}

	@Test
	void testUpdateCountry_NotFound_404() throws Exception {
		CountryDTO updateDto = new CountryDTO(999, "CM", "Cameroon", "+237");
		when(countryBrowserManagerMock.getCountry(999))
			.thenThrow(new EntityNotFoundException("Country not found"));

		mockMvc.perform(put("/countries/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.country.not_found")));

		verify(countryBrowserManagerMock, never()).updateCountry(any());
	}

	@Test
	void testDeleteCountry_Success_204() throws Exception {
		Country existing = createCountry(1, "CM", "Cameroon", "+237");
		when(countryBrowserManagerMock.getCountry(1)).thenReturn(existing);
		doNothing().when(countryBrowserManagerMock).deleteCountry(1);

		mockMvc.perform(delete("/countries/1"))
			.andExpect(status().isNoContent());

		verify(countryBrowserManagerMock, times(1)).deleteCountry(1);
	}

	@Test
	void testDeleteCountry_NotFound_404() throws Exception {
		when(countryBrowserManagerMock.getCountry(999))
			.thenThrow(new EntityNotFoundException("Country not found"));

		mockMvc.perform(delete("/countries/999"))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.country.not_found")));

		verify(countryBrowserManagerMock, never()).deleteCountry(999);
	}
}
