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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.typology.dto.TypologyDTO;
import org.isf.typology.manager.TypologyBrowserManager;
import org.isf.typology.mapper.TypologyMapper;
import org.isf.typology.model.Family;
import org.isf.typology.model.Typology;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Typologies")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/typologies", produces = MediaType.APPLICATION_JSON_VALUE)
public class TypologyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TypologyController.class);

	private final TypologyBrowserManager typologyManager;

	private final TypologyMapper typologyMapper;

	public TypologyController(TypologyBrowserManager typologyManager, TypologyMapper typologyMapper) {
		this.typologyManager = typologyManager;
		this.typologyMapper = typologyMapper;
	}

	/**
	 * Get typologies by family or all.
	 *
	 * @param family optional family filter
	 * @return list of {@link TypologyDTO}
	 * @throws OHServiceException When failed to get typologies
	 */
	@GetMapping
	public List<TypologyDTO> getTypologies(@RequestParam(value = "family", required = false) String family) throws OHServiceException {
		LOGGER.info("Get typologies for family: {}", family);
		Family familyEnum = Family.fromString(family);
		if (familyEnum != null) {
			return typologyMapper.map2DTOList(typologyManager.getTypologies(familyEnum));
		}
		List<Typology> allTypologies = new ArrayList<>();
		for (Family f : Family.values()) {
			if (typologyManager.isFamilyEnabled(f)) {
				allTypologies.addAll(typologyManager.getTypologies(f));
			}
		}
		return typologyMapper.map2DTOList(allTypologies);
	}

	/**
	 * Search typologies with pagination.
	 *
	 * @param search search term
	 * @param family family filter
	 * @param page page index
	 * @param size page size
	 * @return page of {@link TypologyDTO}
	 * @throws OHServiceException When failed to search typologies
	 */
	@GetMapping("/search")
	public Page<TypologyDTO> searchTypologies(
		@RequestParam(value = "search", required = false) String search,
		@RequestParam(value = "family", required = false) String family,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) throws OHServiceException {
		LOGGER.info("Search typologies - search: {}, family: {}, page: {}, size: {}", search, family, page, size);
		Family familyEnum = Family.fromString(family);
		Page<Typology> typologies = typologyManager.searchTypologies(search, familyEnum, page, size);
		return typologies.map(typologyMapper::map2DTO);
	}

	/**
	 * Get typology by code.
	 *
	 * @param code the typology code
	 * @return the {@link TypologyDTO}
	 * @throws OHServiceException When failed to get typology
	 */
	@GetMapping("/{code}")
	public TypologyDTO getTypologyByCode(@PathVariable("code") String code) throws OHServiceException {
		LOGGER.info("Get typology by code: {}", code);
		Typology typology = typologyManager.getTypologyByCode(code);
		if (typology == null) {
			throw new OHAPIException(new OHExceptionMessage("Typology not found."), HttpStatus.NOT_FOUND);
		}
		return typologyMapper.map2DTO(typology);
	}

	/**
	 * Create a new {@link Typology}.
	 *
	 * @param typologyDTO typology payload
	 * @return created {@link TypologyDTO}
	 * @throws OHServiceException When failed to create typology
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TypologyDTO newTypology(@Valid @RequestBody TypologyDTO typologyDTO) throws OHServiceException {
		LOGGER.info("Create Typology {}", typologyDTO.getCode());
		try {
			Typology typology = typologyManager.newTypology(typologyMapper.map2Model(typologyDTO));
			if (typology == null) {
				throw new OHAPIException(new OHExceptionMessage("Failed to create typology."));
			}
			return typologyMapper.map2DTO(typology);
		} catch (OHDataIntegrityViolationException e) {
			throw new OHAPIException(new OHExceptionMessage("Typology code already present."));
		} catch (OHServiceException e) {
			throw new OHAPIException(new OHExceptionMessage(e.getMessage()));
		}
	}

	/**
	 * Update an existing {@link Typology}.
	 *
	 * @param typologyDTO typology payload
	 * @return updated {@link TypologyDTO}
	 * @throws OHServiceException When failed to update typology
	 */
	@PutMapping
	public TypologyDTO updateTypology(@Valid @RequestBody TypologyDTO typologyDTO) throws OHServiceException {
		LOGGER.info("Update Typology code: {}", typologyDTO.getCode());
		if (!typologyManager.isCodePresent(typologyDTO.getCode())) {
			throw new OHAPIException(new OHExceptionMessage("Typology not found."), HttpStatus.NOT_FOUND);
		}
		try {
			Typology typology = typologyManager.updateTypology(typologyMapper.map2Model(typologyDTO));
			return typologyMapper.map2DTO(typology);
		} catch (OHServiceException serviceException) {
			throw new OHAPIException(new OHExceptionMessage("Typology is not updated."));
		}
	}

	/**
	 * Delete {@link Typology} for specified code.
	 *
	 * @param code typology code
	 * @return {@code true} if deleted
	 * @throws OHServiceException When failed to delete typology
	 */
	@DeleteMapping("/{code}")
	public boolean deleteTypology(@PathVariable("code") String code) throws OHServiceException {
		LOGGER.info("Delete Typology code: {}", code);
		Typology typology = typologyManager.getTypologyByCode(code);
		if (typology == null) {
			throw new OHAPIException(new OHExceptionMessage("Typology not found."), HttpStatus.NOT_FOUND);
		}
		try {
			typologyManager.deleteTypology(typology);
			return true;
		} catch (OHServiceException serviceException) {
			LOGGER.error("Delete Typology: {} failed.", code);
			throw new OHAPIException(new OHExceptionMessage("Typology not deleted."));
		}
	}

	/**
	 * Check if the code is already used by another typology.
	 *
	 * @param code typology code
	 * @return {@code true} if code exists
	 * @throws OHServiceException When failed to check typology code
	 */
	@GetMapping("/check/{code}")
	public boolean checkTypologyCode(@PathVariable("code") String code) throws OHServiceException {
		LOGGER.info("Check typology code: {}", code);
		return typologyManager.isCodePresent(code);
	}
}
