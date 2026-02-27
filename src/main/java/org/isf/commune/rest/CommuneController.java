/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2025 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.commune.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.commune.dto.CommuneDTO;
import org.isf.commune.manager.CommuneManager;
import org.isf.commune.mapper.CommuneMapper;
import org.isf.commune.model.Commune;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Communes")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CommuneController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommuneController.class);

    private final CommuneManager communeManager;

    private final CommuneMapper mapper;

    public CommuneController(CommuneManager communeManager, CommuneMapper communeMapper) {
        this.communeManager = communeManager;
        this.mapper = communeMapper;
    }

    /**
     * Get all the communes.
     *
     * @return List of communes.
     * @throws OHServiceException When failed to get communes
     */
    @GetMapping("/communes")
    public List<CommuneDTO> getCommunes() throws OHServiceException {
        LOGGER.info("Get communes");

        return mapper.map2DTOList(communeManager.getAllCommunes());
    }

    /**
     * Get commune related to a commune id.
     *
     * @param id of the commune
     * @return The commune related to the supplied commune id
     * @throws OHServiceException When failed to get commune
     */
    @GetMapping("/communes/{id}")
    public CommuneDTO getCommuneById(@PathVariable Integer id) throws OHServiceException {
        LOGGER.info("Get commune by id: {}", id);

        return mapper.map2DTO(communeManager.getCommune(id));
    }

    /**
     * Create a new commune.
     *
     * @param commune commune payload
     * @return an error message if there is a problem, ok otherwise.
     * @throws OHServiceException When failed to create the commune
     */
    @PostMapping("/communes")
    @ResponseStatus(HttpStatus.CREATED)
    public CommuneDTO newCommune(@RequestBody CommuneDTO commune) throws OHServiceException {
        LOGGER.info("Create commune: {}", commune);
		Commune newCommune = new Commune();
		newCommune.setName(mapper.map2Model(commune).getName());
        try {
            return mapper.map2DTO(communeManager.newCommune(newCommune));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Commune not created."));
        }
    }

    /**
     * Update a commune.
     *
     * @param updateCommune commune payload
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to update commune
     */
    @PutMapping("/communes")
    public CommuneDTO updateCommune(@RequestBody CommuneDTO updateCommune) throws OHServiceException {
        LOGGER.info("Update commune: {}", updateCommune);

        Commune commune = mapper.map2Model(updateCommune);

        try {
            return mapper.map2DTO(communeManager.updateCommune(commune));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Commune not updated."));
        }
    }

    /**
     * Delete a commune.
     *
     * @param id of the commune to delete
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to delete commune
     */
    @DeleteMapping("/communes/{id}")
    public boolean deleteCommune(@PathVariable("id") Integer id) throws OHServiceException {
        LOGGER.info("Delete commune code: {}", id);
		Commune communeToDelete = communeManager.getCommune(id);

		if (communeToDelete == null) {
			throw new OHAPIException(new OHExceptionMessage("Commune not found."));
		}

        try {
            communeManager.deleteCommune(communeToDelete);
            return true;
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Commune not deleted."));
        }
    }
}
