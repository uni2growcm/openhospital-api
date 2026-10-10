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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.isf.country.dto.CountryDTO;
import org.isf.country.manager.CountryBrowserManager;
import org.isf.country.mapper.CountryMapper;
import org.isf.country.model.Country;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Countries")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

	private final CountryBrowserManager countryManager;

	private final CountryMapper mapper;

	public CountryController(CountryBrowserManager countryManager, CountryMapper countryMapper) {
		this.countryManager = countryManager;
		this.mapper = countryMapper;
	}

	/**
	 * Get all the countries stored
	 *
	 * @return the list of countries found
	 * @throws OHServiceException When failed to get countries
	 */
	@GetMapping(value = "/countries")
	public List<CountryDTO> getCountries() throws OHServiceException {
		LOGGER.info("Get countries");

		return mapper.map2DTOList(countryManager.getCountries());
	}

	/**
	 * Create a new {@link Country}
	 *
	 * @param countryDTO Country payload
	 * @return the created {@link Country}
	 * @throws OHServiceException When failed to create the country
	 */
	@PostMapping(value = "/countries")
	@ResponseStatus(HttpStatus.CREATED)
	public CountryDTO newCountry(@Valid @RequestBody CountryDTO countryDTO) throws OHServiceException {
		LOGGER.info("Create country {}", countryDTO.getName());

		if (countryManager.getCountryByIsoCode(countryDTO.getIsoCode()).isPresent()) {
			throw new OHAPIException(new OHExceptionMessage("A country with this ISO code already exists."));
		}
		if (countryManager.getCountryByPhoneCode(countryDTO.getPhoneCode()).isPresent()) {
			throw new OHAPIException(new OHExceptionMessage("A country with this phone code already exists."));
		}
		if (countryManager.getCountryByName(countryDTO.getName()).isPresent()) {
			throw new OHAPIException(new OHExceptionMessage("A country with this name already exists."));
		}

		Country newCountry = countryManager.saveCountry(mapper.map2Model(countryDTO));
		return mapper.map2DTO(newCountry);
	}
}
