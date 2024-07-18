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

import org.isf.orthanc.dto.OrthancConfigDTO;
import org.isf.orthanc.dto.OrthancPatientDTO;
import org.isf.orthanc.manager.OrthancBrowserManager;
import org.isf.orthanc.mapper.OrthancConfigMapper;
import org.isf.orthanc.mapper.OrthancPatientMapper;
import org.isf.orthanc.model.OrthancConfig;
import org.isf.orthanc.model.OrthancPatient;
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
@RequestMapping("/orthanc")
public class OrthancController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
	
    @Autowired
    private OrthancBrowserManager orthancManager;
    
    @Autowired
    private OrthancConfigMapper orthancConfigMapper;
    
    @Autowired
    private OrthancPatientMapper orthancPatientMapper;
    
    @Autowired
	protected PatientBrowserManager patientManager;
    
    public OrthancController(OrthancBrowserManager orthancManager, OrthancConfigMapper orthancConfigMapper,
			OrthancPatientMapper orthancPatientMapper, PatientBrowserManager patientManager) {
		this.orthancManager = orthancManager;
		this.orthancConfigMapper = orthancConfigMapper;
		this.orthancPatientMapper = orthancPatientMapper;
		this.patientManager = patientManager;
	}

	@PostMapping(value="/config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancConfigDTO> newOrthancConfig(@RequestBody OrthancConfigDTO orthancConfigDTO) throws OHServiceException {
    	LOGGER.info("Create orthanc config");
    	OrthancConfig orthancConf = orthancConfigMapper.map2Model(orthancConfigDTO);
    	orthancConf = orthancManager.newOrthancConfig(orthancConf);
    	if (orthancConf == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc config not created."));
		}
    	return ResponseEntity.status(HttpStatus.CREATED).body(orthancConfigMapper.map2DTO(orthancConf));
    }
	
	@PostMapping(value="/patient", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@PutMapping(value="/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancConfigDTO> updateOrthancConfig(@PathVariable int id, @RequestBody OrthancConfigDTO orthancConfigDTO) throws OHServiceException {
    	LOGGER.info("update orthanc config");
    	if (orthancConfigDTO.getId() != id) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc config id mismatch."));
		}
    	String user = orthancConfigDTO.getUserName();
    	OrthancConfig orthconfig = orthancManager.getOrtancConfigByUserName(user);
    	if (orthconfig == null || orthconfig.getId() != id) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc config not found."));
		}
    	OrthancConfig orthancConf = orthancConfigMapper.map2Model(orthancConfigDTO);
    	orthancConf = orthancManager.updateOrthancConfig(orthancConf);
    	if (orthancConf == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc config not updated."));
		}
    	
    	return ResponseEntity.ok().body(orthancConfigMapper.map2DTO(orthancConf));
    }
	
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
	
	@GetMapping(value="/patient/{patId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancPatientDTO> getOrthancPatientByPatientId(@PathVariable int patId) throws OHServiceException {
		LOGGER.info("Get orthanc patient by patId: {}", patId);
		OrthancPatient orthancPat = orthancManager.getOrthancPatientByPatientId(patId);
    	if (orthancPat == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not found."));
		}
    	return ResponseEntity.ok().body(orthancPatientMapper.map2DTO(orthancPat));
	}
	
	@GetMapping(value="/config/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrthancConfigDTO> getOrthancConfigByUserName(@PathVariable String userName) throws OHServiceException {
		LOGGER.info("Get orthanc config by user name: {}", userName);
		OrthancConfig orthancConfig = orthancManager.getOrtancConfigByUserName(userName);
    	if (orthancConfig == null) {
			throw new OHAPIException(new OHExceptionMessage("Orthanc patient not found."));
		}
    	return ResponseEntity.ok().body(orthancConfigMapper.map2DTO(orthancConfig));
	}
	
	@GetMapping("/patients/{id}")
    public ResponseEntity<Object> getPatientById(@PathVariable String id) throws OHServiceException {
		LOGGER.info("Get Patient by id: {}", id);
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orthancManager.getPatientById(id, currentUser));
    }
	
	@GetMapping("/studies")
    public ResponseEntity<String> getStudies() {
        return ResponseEntity.ok(orthancManager.getStudies());
    }
}