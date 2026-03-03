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
package org.isf.ethnic.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.ethnic.dto.EthnicDTO;
import org.isf.ethnic.manager.EthnicManager;
import org.isf.ethnic.mapper.EthnicMapper;
import org.isf.ethnic.model.Ethnic;
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
@Tag(name = "Ethnics")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class EthnicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EthnicController.class);

    private final EthnicManager ethnicManager;

    private final EthnicMapper mapper;

    public EthnicController(EthnicManager ethnicManager, EthnicMapper ethnicMapper) {
        this.ethnicManager = ethnicManager;
        this.mapper = ethnicMapper;
    }

    /**
     * Get all the ethnics.
     *
     * @return List of ethnics.
     * @throws OHServiceException When failed to get ethnics
     */
    @GetMapping("/ethnics")
    public List<EthnicDTO> getEthnics() throws OHServiceException {
        LOGGER.info("Get ethnics");

        return mapper.map2DTOList(ethnicManager.getAll());
    }

    /**
     * Get ethnic related to a ethnic id.
     *
     * @param id the id of the ethnic
     * @return The ethnic related to the supplied ethnic id
     * @throws OHServiceException When failed to get ethnic
     */
    @GetMapping("/ethnics/{id}")
    public EthnicDTO getEthnicById(@PathVariable Integer id) throws OHServiceException {
        LOGGER.info("Get ethnic by id: {}", id);

		try {
        	return mapper.map2DTO(ethnicManager.getById(id));
		} catch (OHServiceException serviceException) {
			throw new OHAPIException(new OHExceptionMessage("Ethnic not found."));
		}
    }

    /**
     * Create a new ethnic.
     *
     * @param ethnic ethnic payload
     * @return an error message if there is a problem, ok otherwise.
     * @throws OHServiceException When failed to create the ethnic
     */
    @PostMapping("/ethnics")
    @ResponseStatus(HttpStatus.CREATED)
    public EthnicDTO newEthnic(@RequestBody EthnicDTO ethnic) throws OHServiceException {
        LOGGER.info("Create ethnic: {}", ethnic);
		Ethnic newEthnic = new Ethnic();
		newEthnic.setName(mapper.map2Model(ethnic).getName());
        try {
            return mapper.map2DTO(ethnicManager.create(newEthnic));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Ethnic not created."));
        }
    }

    /**
     * Update a ethnic.
     *
	 * @param id the id of the ethnic
     * @param updatedEthnic ethnic payload
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to update ethnic
     */
    @PutMapping("/ethnics/{id}")
    public EthnicDTO updateEthnic(@RequestParam Integer id, @RequestBody EthnicDTO updatedEthnic) throws OHServiceException {
        LOGGER.info("Update ethnic: {}", updatedEthnic);

        Ethnic ethnic = mapper.map2Model(updatedEthnic);
		Ethnic ethnicFound = ethnicManager.getById(id);

		if (!Objects.equals(ethnicFound.getId(), updatedEthnic.getId())) {
			throw new OHAPIException(new OHExceptionMessage("Ethnic not updated."));
		}

        try {
            return mapper.map2DTO(ethnicManager.update(id, ethnic));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Ethnic not updated."));
        }
    }

    /**
     * Delete a ethnic.
     *
     * @param id the id of the ethnic to delete
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to delete ethnic
     */
    @DeleteMapping("/ethnics/{id}")
    public boolean deleteEthnic(@PathVariable("id") Integer id) throws OHServiceException {
        LOGGER.info("Delete ethnic code: {}", id);
		Ethnic ethnicToDelete = ethnicManager.getById(id);

		if (ethnicToDelete == null) {
			throw new OHAPIException(new OHExceptionMessage("Ethnic not found."));
		}

		ethnicManager.delete(ethnicToDelete.getId());
		return true;
    }
}
