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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.isf.country.dto.CountryDTO;
import org.isf.country.manager.CountryBrowserManager;
import org.isf.country.mapper.CountryMapper;
import org.isf.country.model.Country;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.shared.pagination.Page;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.pagination.PagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Countries")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

	private final CountryBrowserManager countryManager;
	private final CountryMapper mapper;

	public CountryController(CountryBrowserManager countryManager, CountryMapper mapper) {
		this.countryManager = countryManager;
		this.mapper = mapper;
	}

	/**
	 * Returns the list of active countries, optionally filtered by keyword.
	 *
	 * @param search Search keyword (matches name, ISO code or phone code)
	 * @return List of {@link CountryDTO}
	 * @throws OHServiceException When failing to fetch countries
	 */
	@GetMapping
	@Operation(summary = "Get all active countries or search by keyword")
	public List<CountryDTO> getCountries(
		@RequestParam(name = "search", required = false) String search
	) throws OHServiceException {
		LOGGER.info("Retrieving countries (search={})...", search);
		List<Country> countries;
		if (search != null && !search.trim().isEmpty()) {
			countries = countryManager.searchCountries(search.trim());
		} else {
			countries = countryManager.getCountries();
		}
		LOGGER.info("Found {} countries.", countries.size());
		return mapper.map2DTOList(countries);
	}

	/**
	 * Returns the list of active countries page by page.
	 *
	 * @param page Zero-based page index
	 * @param size Page size
	 * @return The requested page of {@link CountryDTO}
	 * @throws OHServiceException When failing to fetch countries
	 */
	@GetMapping("/paged")
	@Operation(summary = "Get active countries page by page")
	public Page<CountryDTO> getCountriesPaged(
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") int size
	) throws OHServiceException {
		LOGGER.info("Retrieving countries page: {} size: {}", page, size);
		PagedResponse<Country> countriesPaged = countryManager.getCountries(page, size);

		Page<CountryDTO> result = new Page<>();
		result.setData(mapper.map2DTOList(countriesPaged.getData()));
		result.setPageInfo(mapper.setParameterPageInfo(countriesPaged.getPageInfo()));
		return result;
	}

	/**
	 * Returns a country by its ID.
	 *
	 * @param id Country ID
	 * @return The found {@link CountryDTO}
	 * @throws OHServiceException When failing to fetch the country
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get country by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Country found"),
		@ApiResponse(responseCode = "404", description = "Country not found")
	})
	public CountryDTO getCountry(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Retrieving country with ID {}...", id);
		try {
			Country country = countryManager.getCountry(id);
			return mapper.map2DTO(country);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Country with ID {} not found.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.country.not_found"), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Creates a new country.
	 *
	 * @param countryDTO The country payload
	 * @return The created {@link CountryDTO}
	 * @throws OHServiceException When failing to create the country
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new country")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Country created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "409", description = "Country already exists (duplicate ISO code, name or phone code)")
	})
	public CountryDTO createCountry(@RequestBody @Valid CountryDTO countryDTO) throws OHServiceException {
		LOGGER.info("Creating new country: {}", countryDTO.getName());

		String isoCode = countryDTO.getIsoCode() != null ? countryDTO.getIsoCode().trim().toUpperCase() : null;
		String name = countryDTO.getName() != null ? countryDTO.getName().trim() : null;

		Integer reactivateId = null;
		if (isoCode != null) {
			var archivedByIso = countryManager.getArchivedCountryByIsoCode(isoCode);
			if (archivedByIso.isPresent()) {
				reactivateId = archivedByIso.get().getId();
			}
		}
		if (reactivateId == null && name != null) {
			var archivedByName = countryManager.getArchivedCountryByName(name);
			if (archivedByName.isPresent()) {
				reactivateId = archivedByName.get().getId();
			}
		}

		validateCountryUniqueness(countryDTO, reactivateId);

		try {
			Country country = mapper.map2Model(countryDTO);
			Country savedCountry = countryManager.saveOrReactivateCountry(country);
			LOGGER.info("Country created/reactivated with ID {}.", savedCountry.getId());
			return mapper.map2DTO(savedCountry);
		} catch (OHDataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while creating country: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.country.integrity_violation"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Updates an existing country.
	 *
	 * @param id Country ID
	 * @param countryDTO The country payload
	 * @return The updated {@link CountryDTO}
	 * @throws OHServiceException When failing to update the country
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing country")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Country updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "404", description = "Country not found"),
		@ApiResponse(responseCode = "409", description = "Conflict with an existing country")
	})
	public CountryDTO updateCountry(
		@PathVariable Integer id,
		@RequestBody @Valid CountryDTO countryDTO
	) throws OHServiceException {
		LOGGER.info("Updating country with ID {}...", id);

		Country existingCountry;
		try {
			existingCountry = countryManager.getCountry(id);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Country with ID {} not found for update.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.country.not_found"), HttpStatus.NOT_FOUND);
		}

		validateCountryUniqueness(countryDTO, id);

		countryDTO.setId(id);
		try {
			Country country = mapper.map2Model(countryDTO);
			country.setLock(countryDTO.getLock() > 0 ? countryDTO.getLock() : existingCountry.getLock());
			Country updatedCountry = countryManager.updateCountry(country);
			LOGGER.info("Country with ID {} updated successfully.", id);
			return mapper.map2DTO(updatedCountry);
		} catch (OHDataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while updating country: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.country.integrity_violation"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Deletes a country (soft-delete).
	 *
	 * @param id Country ID
	 * @throws OHServiceException When failing to delete the country
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Soft-delete a country by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Country successfully deleted"),
		@ApiResponse(responseCode = "404", description = "Country not found")
	})
	public void deleteCountry(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Deleting country with ID {}...", id);
		try {
			countryManager.getCountry(id);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Country with ID {} not found for deletion.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.country.not_found"), HttpStatus.NOT_FOUND);
		}

		countryManager.deleteCountry(id);
		LOGGER.info("Country with ID {} deleted successfully.", id);
	}

	private void validateCountryUniqueness(CountryDTO countryDTO, Integer excludeId) throws OHServiceException {
		String isoCode = countryDTO.getIsoCode().trim().toUpperCase();
		if (!countryManager.isCodeUnique(isoCode, excludeId)) {
			LOGGER.warn("Country with ISO code '{}' already exists.", isoCode);
			throw new OHAPIException(new OHExceptionMessage("errors.country.isocode_exists"), HttpStatus.CONFLICT);
		}

		if (countryDTO.getName() != null) {
			String name = countryDTO.getName().trim();
			var existingName = countryManager.getCountryByName(name);
			if (existingName.isPresent() && (excludeId == null || existingName.get().getId() != excludeId)) {
				LOGGER.warn("Country with name '{}' already exists.", name);
				throw new OHAPIException(new OHExceptionMessage("errors.country.name_exists"), HttpStatus.CONFLICT);
			}
		}

		if (countryDTO.getPhoneCode() != null) {
			String phoneCode = countryDTO.getPhoneCode().trim();
			var existingPhone = countryManager.getActiveCountryByPhoneCode(phoneCode);
			if (existingPhone.isPresent() && (excludeId == null || existingPhone.get().getId() != excludeId)) {
				LOGGER.warn("Country with phone code '{}' already exists.", phoneCode);
				throw new OHAPIException(new OHExceptionMessage("errors.country.phonecode_exists"), HttpStatus.CONFLICT);
			}
		}
	}
}
