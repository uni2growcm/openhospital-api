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

package org.isf.articlefamily.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.isf.articlefamily.dto.ArticleFamilyDTO;
import org.isf.articlefamily.manager.ArticleFamilyBrowserManager;
import org.isf.articlefamily.mapper.ArticleFamilyMapper;
import org.isf.articlefamily.model.ArticleFamily;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Article Families")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/article-families", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleFamilyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleFamilyController.class);

	private final ArticleFamilyBrowserManager articleFamilyBrowserManager;
	private final ArticleFamilyMapper mapper;

	public ArticleFamilyController(ArticleFamilyBrowserManager articleFamilyBrowserManager, ArticleFamilyMapper mapper) {
		this.articleFamilyBrowserManager = articleFamilyBrowserManager;
		this.mapper = mapper;
	}

	/**
	 * Retrieves all active article families.
	 *
	 * @return List of {@link ArticleFamilyDTO}
	 * @throws OHServiceException When failing to retrieve article families
	 */
	@GetMapping
	@Operation(summary = "Get all active article families")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "List of active article families retrieved successfully")
	})
	public List<ArticleFamilyDTO> getAllArticleFamilies() throws OHServiceException {
		LOGGER.info("Retrieving all active article families...");
		List<ArticleFamily> articleFamilies = articleFamilyBrowserManager.getArticleFamilies();
		return mapper.map2DTOList(articleFamilies);
	}

	/**
	 * Searches active article families by keyword matching code or description.
	 *
	 * @param q The search keyword
	 * @return List of matching {@link ArticleFamilyDTO}
	 * @throws OHServiceException When failing to search article families
	 */
	@GetMapping("/search")
	@Operation(summary = "Search active article families")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Matching article families retrieved successfully")
	})
	public List<ArticleFamilyDTO> searchArticleFamilies(@RequestParam(value = "q", required = false) String q) throws OHServiceException {
		LOGGER.info("Searching active article families with keyword: {}", q);
		List<ArticleFamily> articleFamilies = articleFamilyBrowserManager.searchArticleFamilies(q);
		return mapper.map2DTOList(articleFamilies);
	}

	/**
	 * Retrieves an article family by ID.
	 *
	 * @param id Article family ID
	 * @return The retrieved {@link ArticleFamilyDTO}
	 * @throws OHServiceException When failing to fetch the article family
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get an article family by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Article family retrieved successfully"),
		@ApiResponse(responseCode = "404", description = "Article family not found")
	})
	public ArticleFamilyDTO getArticleFamily(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Retrieving article family with ID: {}", id);
		try {
			ArticleFamily articleFamily = articleFamilyBrowserManager.getArticleFamily(id);
			return mapper.map2DTO(articleFamily);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Article family with ID {} not found.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.not_found"), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Creates a new article family.
	 *
	 * @param articleFamilyDTO The article family payload
	 * @return The created {@link ArticleFamilyDTO}
	 * @throws OHServiceException When failing to create the article family
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new article family")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Article family created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "409", description = "Article family code already exists")
	})
	public ArticleFamilyDTO createArticleFamily(@RequestBody @Valid ArticleFamilyDTO articleFamilyDTO) throws OHServiceException {
		LOGGER.info("Creating new article family: {}", articleFamilyDTO.getCode());

		if (articleFamilyDTO.getCode() != null) {
			articleFamilyDTO.setCode(articleFamilyDTO.getCode().trim().toUpperCase());
		}

		String code = articleFamilyDTO.getCode();
		Integer reactivateId = null;
		if (code != null) {
			var archived = articleFamilyBrowserManager.getArchivedArticleFamilyByCode(code);
			if (archived.isPresent()) {
				reactivateId = archived.get().getId();
			}
		}

		validateArticleFamilyUniqueness(articleFamilyDTO, reactivateId);

		try {
			ArticleFamily articleFamily = mapper.map2Model(articleFamilyDTO);
			ArticleFamily savedArticleFamily = articleFamilyBrowserManager.saveOrReactivateArticleFamily(articleFamily);
			LOGGER.info("Article family created/reactivated with ID {}.", savedArticleFamily.getId());
			return mapper.map2DTO(savedArticleFamily);
		} catch (OHDataIntegrityViolationException | org.springframework.dao.DataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while creating article family: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.code_exists"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Updates an existing article family.
	 *
	 * @param id Article family ID
	 * @param articleFamilyDTO The article family payload
	 * @return The updated {@link ArticleFamilyDTO}
	 * @throws OHServiceException When failing to update the article family
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing article family")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Article family updated successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid request payload"),
		@ApiResponse(responseCode = "404", description = "Article family not found"),
		@ApiResponse(responseCode = "409", description = "Conflict with an existing article family")
	})
	public ArticleFamilyDTO updateArticleFamily(
		@PathVariable Integer id,
		@RequestBody @Valid ArticleFamilyDTO articleFamilyDTO
	) throws OHServiceException {
		LOGGER.info("Updating article family with ID {}...", id);

		ArticleFamily existingArticleFamily;
		try {
			existingArticleFamily = articleFamilyBrowserManager.getArticleFamily(id);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Article family with ID {} not found for update.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.not_found"), HttpStatus.NOT_FOUND);
		}

		if (articleFamilyDTO.getCode() != null) {
			articleFamilyDTO.setCode(articleFamilyDTO.getCode().trim().toUpperCase());
		}

		validateArticleFamilyUniqueness(articleFamilyDTO, id);

		try {
			ArticleFamily articleFamily = mapper.map2Model(articleFamilyDTO);
			articleFamily.setId(id);
			articleFamily.setLock(existingArticleFamily.getLock());
			ArticleFamily updatedArticleFamily = articleFamilyBrowserManager.updateArticleFamily(articleFamily);
			LOGGER.info("Article family with ID {} updated successfully.", id);
			return mapper.map2DTO(updatedArticleFamily);
		} catch (OHDataIntegrityViolationException e) {
			LOGGER.error("Data integrity violation while updating article family: {}", e.getMessage());
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.integrity_violation"), HttpStatus.CONFLICT);
		}
	}

	/**
	 * Soft-deletes an article family by ID.
	 *
	 * @param id Article family ID
	 * @throws OHServiceException When failing to delete the article family
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete an article family by ID (soft delete)")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Article family successfully deleted"),
		@ApiResponse(responseCode = "404", description = "Article family not found")
	})
	public void deleteArticleFamily(@PathVariable Integer id) throws OHServiceException {
		LOGGER.info("Deleting article family with ID {}...", id);
		try {
			articleFamilyBrowserManager.getArticleFamily(id);
		} catch (EntityNotFoundException e) {
			LOGGER.warn("Article family with ID {} not found for deletion.", id);
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.not_found"), HttpStatus.NOT_FOUND);
		}

		articleFamilyBrowserManager.deleteArticleFamily(id);
		LOGGER.info("Article family with ID {} deleted successfully.", id);
	}

	private void validateArticleFamilyUniqueness(ArticleFamilyDTO articleFamilyDTO, Integer excludeId) throws OHServiceException {
		String code = articleFamilyDTO.getCode() != null ? articleFamilyDTO.getCode().trim().toUpperCase() : "";
		if (!articleFamilyBrowserManager.isCodeUnique(code, excludeId)) {
			LOGGER.warn("Article family with code '{}' already exists.", code);
			throw new OHAPIException(new OHExceptionMessage("errors.articlefamily.code_exists"), HttpStatus.CONFLICT);
		}
	}
}
