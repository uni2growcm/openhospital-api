/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2024 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.orthanc.rest;

import org.isf.orthanc.dto.OrthancPatientDTO;
import org.isf.orthanc.dto.OrthancUserDTO;
import org.isf.orthanc.manager.OrthancBrowserManager;
import org.isf.orthanc.mapper.OrthancPatientMapper;
import org.isf.orthanc.mapper.OrthancUserMapper;
import org.isf.orthanc.model.OrthancPatient;
import org.isf.orthanc.model.OrthancUser;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.patient.rest.PatientController;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Orthanc")
@RequestMapping("/orthancs")
public class OrthancController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
	
    @Autowired
    private OrthancBrowserManager orthancManager;
    
    @Autowired
    private OrthancUserMapper orthancUserMapper;
    
    @Autowired
    private OrthancPatientMapper orthancPatientMapper;
    
    @Autowired
	protected PatientBrowserManager patientManager;
    
    public OrthancController(OrthancBrowserManager orthancManager, OrthancUserMapper orthancUserMapper,
			OrthancPatientMapper orthancPatientMapper, PatientBrowserManager patientManager) {
		this.orthancManager = orthancManager;
		this.orthancUserMapper = orthancUserMapper;
		this.orthancPatientMapper = orthancPatientMapper;
		this.patientManager = patientManager;
	}

    /**
	 * Create new OrthancUser.
	 * 
	 * @param orthancUserDTO - the {@link OrthancUserDTO} to insert
	 * @return the {@link OrthancUserDTO} that has been inserted, {@code null} otherwise.
	 * @throws OHServiceException 
	 */
	@PostMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancUserDTO> newOrthancUser(@RequestBody OrthancUserDTO orthancUserDTO) throws OHServiceException {
    	LOGGER.info("Create orthanc user");
    	String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	String ohUserId = orthancUserDTO.getOhUserId();
    	if (!currentUser.equals(ohUserId)) {
    		throw new OHAPIException(new OHExceptionMessage("User not found."));
    	}
    	OrthancUser orthancUser = orthancUserMapper.map2Model(orthancUserDTO);
    	orthancUser = orthancManager.newOrthancUser(orthancUser);
    	if (orthancUser == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc user not created."));
		}
    	return ResponseEntity.status(HttpStatus.CREATED).body(orthancUserMapper.map2DTO(orthancUser));
    }
	
	/**
	 * Create an OrthancPatient.
	 * 
	 * @param orthancPatientDTO - the {@link OrthancPatientDTO} to insert
	 * @return the {@link OrthancPatientDTO} that has been created, {@code null} otherwise.
	 * @throws OHServiceException 
	 */
	@PostMapping(value="/patients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancPatientDTO> newOrthancPatient(@RequestBody OrthancPatientDTO orthancPatientDTO) throws OHServiceException {
    	LOGGER.info("Create orthanc patient");
    	int code = orthancPatientDTO.getOhPatienId();
    	Patient patientRead = patientManager.getPatientById(code);
		if (patientRead == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient not found."));
		}
    	OrthancPatient orthancPat = orthancPatientMapper.map2Model(orthancPatientDTO);
    	orthancPat = orthancManager.newOrthancPatient(orthancPat);
    	if (orthancPat == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not created."));
		}
    	return ResponseEntity.status(HttpStatus.CREATED).body(orthancPatientMapper.map2DTO(orthancPat));
    }

	/**
	 * Update an OrthancUser.
	 * 
	 * @param id - the id of orthanc user
	 * @param orthancUserDTO - the {@link OrthancUserDTO} to update
	 * @return the {@link OrthancUserDTO} that has been updated, {@code null} otherwise.
	 * @throws OHServiceException 
	 */
	@PutMapping(value="/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancUserDTO> updateOrthancUser(@PathVariable int id, @RequestBody OrthancUserDTO orthancUserDTO) throws OHServiceException {
    	LOGGER.info("update orthanc user by id : {}", id);
    	if (orthancUserDTO.getId() != id) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc user id mismatch."));
		}
    	String ohUserId = orthancUserDTO.getOhUserId();
    	String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	if (!currentUser.equals(ohUserId)) {
    		throw new OHAPIException(new OHExceptionMessage("User not found."));
    	}
    	OrthancUser orthUser = orthancManager.getOrtancUserByOhUserId(ohUserId);
    	if (orthUser == null || orthUser.getId() != id) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc user not found."));
		}
    	OrthancUser orthancUser = orthancUserMapper.map2Model(orthancUserDTO);
    	orthancUser = orthancManager.updateOrthancConfig(orthancUser);
    	if (orthancUser == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc user not updated."));
		}
    	
    	return ResponseEntity.ok().body(orthancUserMapper.map2DTO(orthancUser));
    }
	
	/**
	 * Update an OrthancPatient.
	 * 
	 * @param id - the orthan patient id
	 * @param orthancPatientDTO - the {@link OrthancPatientDTO} to update
	 * @return {@link OrthancPatientDTO}. It could be {@code null}.
	 * @throws OHServiceException 
	 */
	@PutMapping(value="/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancPatientDTO> updateOrthancPatient(@PathVariable int id, @RequestBody OrthancPatientDTO orthancPatientDTO) throws OHServiceException {
    	LOGGER.info("Update orthanc patient by id: {}", id);
    	if (orthancPatientDTO.getId() != id) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient id mismatch."));
		}
    	OrthancPatient orthancPatient = orthancManager.getOrthancPatientById(id);
    	if (orthancPatient == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not found."));
		}
    	int code = orthancPatientDTO.getOhPatienId();
    	Patient patientRead = patientManager.getPatientById(code);
		if (patientRead == null) {
			throw new OHAPIException(new OHExceptionMessage("Patient not found."));
		}
    	OrthancPatient orthancPat = orthancPatientMapper.map2Model(orthancPatientDTO);
    	orthancPat = orthancManager.updateOrthancPatient(orthancPat);
    	if (orthancPat == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not updated."));
		}
    	return ResponseEntity.ok().body(orthancPatientMapper.map2DTO(orthancPat));
    }
	
	/**
	 * Return {@link OrthancPatientDTO}
	 * 
	 * @param patId - the patient id
	 * @return {@link OrthancPatientDTO}. It could be {@code null}.
	 * @throws OHServiceException 
	 */
	@GetMapping(value="/patient/{patId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancPatientDTO> getOrthancPatientByPatientId(@PathVariable int patId) throws OHServiceException {
		LOGGER.info("Get orthanc patient by patId: {}", patId);
		OrthancPatient orthancPat = orthancManager.getOrthancPatientByPatientId(patId);
    	if (orthancPat == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not found."));
		}
    	return ResponseEntity.ok().body(orthancPatientMapper.map2DTO(orthancPat));
	}
	
	/**
	 * Return {@link OrthancUserDTO}
	 * 
	 * @param ohUserId - the oh user name
	 * @return {@link OrthancUser}. It could be {@code null}.
	 * @throws OHServiceException 
	 */
	@GetMapping(value="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancUserDTO> getOrthancUser() throws OHServiceException {
		LOGGER.info("Get orthanc user");
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		OrthancUser orthancUser = orthancManager.getOrtancUserByOhUserId(currentUser);
    	if (orthancUser == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not found."));
		}
    	return ResponseEntity.ok().body(orthancUserMapper.map2DTO(orthancUser));
	}
	
	@GetMapping("/studies")
    public ResponseEntity<String> getStudies() {
        return ResponseEntity.ok(orthancManager.getStudies());
    }
}