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
package org.isf.town.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.town.dto.TownDTO;
import org.isf.town.manager.TownManager;
import org.isf.town.mapper.TownMapper;
import org.isf.town.model.Town;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Tag(name = "Towns")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TownController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TownController.class);

    private final TownManager townManager;

    private final TownMapper mapper;

    public TownController(TownManager townManager, TownMapper townMapper) {
        this.townManager = townManager;
        this.mapper = townMapper;
    }

    /**
     * Get all the towns.
     *
     * @return List of towns.
     * @throws OHServiceException When failed to get towns
     */
    @GetMapping("/towns")
    public List<TownDTO> getTowns() throws OHServiceException {
        LOGGER.info("Get towns");

        return mapper.map2DTOList(townManager.getAll());
    }

    /**
     * Get town related to a town id.
     *
     * @param id the id of the town
     * @return The town related to the supplied town id
     * @throws OHServiceException When failed to get town
     */
    @GetMapping("/towns/{id}")
    public TownDTO getTownById(@PathVariable Integer id) throws OHServiceException {
        LOGGER.info("Get town by id: {}", id);

		try {
        	return mapper.map2DTO(townManager.getById(id));
		} catch (OHServiceException serviceException) {
			throw new OHAPIException(new OHExceptionMessage("Town not found."));
		}
    }

    /**
     * Create a new town.
     *
     * @param town town payload
     * @return an error message if there is a problem, ok otherwise.
     * @throws OHServiceException When failed to create the town
     */
    @PostMapping("/towns")
    @ResponseStatus(HttpStatus.CREATED)
    public TownDTO newTown(@RequestBody TownDTO town) throws OHServiceException {
        LOGGER.info("Create town: {}", town);
		Town newTown = new Town();
		newTown.setName(mapper.map2Model(town).getName());
        try {
            return mapper.map2DTO(townManager.create(newTown));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Town not created."));
        }
    }

    /**
     * Update a town.
     *
	 * @param id the id of the town
     * @param updatedTown town payload
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to update town
     */
    @PutMapping("/towns/{id}")
    public TownDTO updateTown(@PathVariable Integer id, @RequestBody TownDTO updatedTown) throws OHServiceException {
        LOGGER.info("Update town: {}", updatedTown);

        Town town = mapper.map2Model(updatedTown);
		Town townFound = townManager.getById(id);

		if (!Objects.equals(townFound.getId(), updatedTown.getId())) {
			throw new OHAPIException(new OHExceptionMessage("Town not updated."));
		}

        try {
            return mapper.map2DTO(townManager.update(id, town));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Town not updated."));
        }
    }

    /**
     * Delete a town.
     *
     * @param id the id of the town to delete
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to delete town
     */
    @DeleteMapping("/towns/{id}")
    public boolean deleteTown(@PathVariable("id") Integer id) throws OHServiceException {
        LOGGER.info("Delete town code: {}", id);
		Town townToDelete = townManager.getById(id);

		if (townToDelete == null) {
			throw new OHAPIException(new OHExceptionMessage("Town not found."));
		}

		townManager.delete(townToDelete.getId());
		return true;
    }
}
