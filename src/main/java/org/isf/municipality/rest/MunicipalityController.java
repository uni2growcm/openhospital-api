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
package org.isf.municipality.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.municipality.dto.MunicipalityDTO;
import org.isf.municipality.manager.MunicipalityManager;
import org.isf.municipality.mapper.MunicipalityMapper;
import org.isf.municipality.model.Municipality;
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
@Tag(name = "Municipalities")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MunicipalityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityController.class);

    private final MunicipalityManager municipalityManager;

    private final MunicipalityMapper mapper;

    public MunicipalityController(MunicipalityManager municipalityManager, MunicipalityMapper municipalityMapper) {
        this.municipalityManager = municipalityManager;
        this.mapper = municipalityMapper;
    }

    /**
     * Get all the municipalities.
     *
     * @return List of municipalities.
     * @throws OHServiceException When failed to get municipalities
     */
    @GetMapping("/municipalities")
    public List<MunicipalityDTO> getMunicipalities() throws OHServiceException {
        LOGGER.info("Get municipalities");

        return mapper.map2DTOList(municipalityManager.getAll());
    }

    /**
     * Get municipality related to a municipality id.
     *
     * @param id the id of the municipality
     * @return The municipality related to the supplied municipality id
     * @throws OHServiceException When failed to get municipality
     */
    @GetMapping("/municipalities/{id}")
    public MunicipalityDTO getMunicipalityById(@PathVariable Integer id) throws OHServiceException {
        LOGGER.info("Get municipality by id: {}", id);

		try {
        	return mapper.map2DTO(municipalityManager.getById(id));
		} catch (OHServiceException serviceException) {
			throw new OHAPIException(new OHExceptionMessage("Municipality not found."));
		}
    }

    /**
     * Create a new municipality.
     *
     * @param municipality municipality payload
     * @return an error message if there is a problem, ok otherwise.
     * @throws OHServiceException When failed to create the municipality
     */
    @PostMapping("/municipalities")
    @ResponseStatus(HttpStatus.CREATED)
    public MunicipalityDTO newMunicipality(@RequestBody MunicipalityDTO municipality) throws OHServiceException {
        LOGGER.info("Create commune: {}", municipality);
		Municipality newMunicipality = new Municipality();
		newMunicipality.setName(mapper.map2Model(municipality).getName());
        try {
            return mapper.map2DTO(municipalityManager.create(newMunicipality));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Municipality not created."));
        }
    }

    /**
     * Update a municipality.
     *
	 * @param id the id of the municipality
     * @param updatedMunicipality municipality payload
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to update municipality
     */
    @PutMapping("/municipalities/{id}")
    public MunicipalityDTO updateMunicipality(@PathVariable Integer id, @RequestBody MunicipalityDTO updatedMunicipality) throws OHServiceException {
        LOGGER.info("Update municipality: {}", updatedMunicipality);

        Municipality municipality = mapper.map2Model(updatedMunicipality);
		Municipality municipalityFound = municipalityManager.getById(id);

		if (!Objects.equals(municipalityFound.getId(), updatedMunicipality.getId())) {
			throw new OHAPIException(new OHExceptionMessage("Municipality not updated."));
		}

        try {
            return mapper.map2DTO(municipalityManager.update(id, municipality));
        } catch (OHServiceException serviceException) {
            throw new OHAPIException(new OHExceptionMessage("Municipality not updated."));
        }
    }

    /**
     * Delete a municipality.
     *
     * @param id the id of the municipality to delete
     * @return an error message if there are some problems, ok otherwise.
     * @throws OHServiceException When failed to delete municipality
     */
    @DeleteMapping("/municipalities/{id}")
    public boolean deleteMunicipality(@PathVariable("id") Integer id) throws OHServiceException {
        LOGGER.info("Delete municipality code: {}", id);
		Municipality municipalityToDelete = municipalityManager.getById(id);

		if (municipalityToDelete == null) {
			throw new OHAPIException(new OHExceptionMessage("Municipality not found."));
		}

		municipalityManager.delete(municipalityToDelete.getId());
		return true;
    }
}
