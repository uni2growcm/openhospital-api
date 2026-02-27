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
package org.isf.occupation.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.ethnic.manager.EthnicManager;
import org.isf.ethnic.model.Ethnic;
import org.isf.occupation.dto.OccupationDTO;
import org.isf.occupation.manager.OccupationManager;
import org.isf.occupation.mapper.OccupationMapper;
import org.isf.occupation.model.Occupation;
import org.isf.shared.exceptions.OHAPIException;
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
@Tag(name = "Occupations")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class OccupationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OccupationController.class);

    private final OccupationManager occupationManager;

    private final OccupationMapper mapper;

    public OccupationController(OccupationManager occupationManager, OccupationMapper occupationMapper) {
        this.occupationManager = occupationManager;
        this.mapper = occupationMapper;
    }

    /**
     * Get all the occupations.
     *
     * @return List of occupations.
     * @throws OHServiceException When failed to get occupations
     */
    @GetMapping("/occupations")
    public List<OccupationDTO> getOccupations() throws OHServiceException {
        LOGGER.info("Get occupations");

        return mapper.map2DTOList(occupationManager.getAll());
    }

    /**
     * Get occupation related to a occupation id.
     *
     * @param id of the occupation
     * @return The occupation related to the supplied occupation id
     * @throws OHServiceException When failed to get occupation
     */
    @GetMapping("/occupations/{id}")
    public OccupationDTO getOccupationById(@PathVariable Integer id) throws OHServiceException {
        LOGGER.info("Get occupation by id: {}", id);

		try {
        	return mapper.map2DTO(occupationManager.getById(id));
		} catch (OHServiceException serviceException) {
			throw new OHAPIException(new OHExceptionMessage("Occupation not found."));
		}
    }

    /**
     * Create a new occupation.
     *
     * @param occupation occupation payload
     * @return an error message if there is a problem, ok otherwise.
     * @throws OHServiceException When failed to create the occupation
     */
    @PostMapping("/occupations")
    @ResponseStatus(HttpStatus.CREATED)
    public OccupationDTO newOccupation(@RequestBody OccupationDTO occupation) throws OHServiceException {
        LOGGER.info("Create occupation: {}", occupation);
		Occupation newOccupation = new Occupation();
		newOccupation.setName(mapper.map2Model(occupation).getName());
        try {
            return mapper.map2DTO(occupationManager.create(newOccupation));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Occupation not created."));
        }
    }

    /**
     * Update a occupation.
     *
     * @param updatedOccupation occupation payload
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to update occupation
     */
    @PutMapping("/occupations/{id}")
    public OccupationDTO updateOccupation(@RequestParam Integer id, @RequestBody OccupationDTO updatedOccupation) throws OHServiceException {
        LOGGER.info("Update occupation: {}", updatedOccupation);

        Occupation occupation = mapper.map2Model(updatedOccupation);
		Occupation occupationFound = occupationManager.getById(id);

		if (!Objects.equals(occupationFound.getId(), updatedOccupation.getId())) {
			throw new OHAPIException(new OHExceptionMessage("Occupation not updated."));
		}

        try {
            return mapper.map2DTO(occupationManager.update(id, occupation));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Occupation not updated."));
        }
    }

    /**
     * Delete a occupation.
     *
     * @param id of the occupation to delete
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to delete occupation
     */
    @DeleteMapping("/occupations/{id}")
    public boolean deleteOccupation(@PathVariable("id") Integer id) throws OHServiceException {
        LOGGER.info("Delete occupation code: {}", id);
		Occupation occupationToDelete = occupationManager.getById(id);

		if (occupationToDelete == null) {
			throw new OHAPIException(new OHExceptionMessage("Occupation not found."));
		}

		occupationManager.delete(occupationToDelete.getId());
		return true;
    }
}
