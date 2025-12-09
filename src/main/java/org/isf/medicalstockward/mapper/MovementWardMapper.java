/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.medicalstockward.mapper;

import jakarta.annotation.PostConstruct;

import org.isf.medicalstockward.dto.MovementWardDTO;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.shared.GenericMapper;
import org.isf.utils.exception.OHServiceException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MovementWardMapper extends GenericMapper<MovementWard, MovementWardDTO> {
	final PatientBrowserManager patientBrowserManager;

	public MovementWardMapper(PatientBrowserManager patientBrowserManager) {
		super(MovementWard.class, MovementWardDTO.class);
		this.patientBrowserManager = patientBrowserManager;
	}

	@PostConstruct
	public void setup() {
		modelMapper.typeMap(MovementWard.class, MovementWardDTO.class)
			.addMappings(mapper -> {
				mapper.map(MovementWard::isPatient, MovementWardDTO::setIsPatient);
			});

		modelMapper.typeMap(MovementWardDTO.class, MovementWard.class)
			.addMappings(mapper -> {
				mapper.map(MovementWardDTO::getPatientId, MovementWard::setIsPatient);
			});
	}

	@Override
	public MovementWardDTO map2DTO(MovementWard model) {
		var dto = super.map2DTO(model);
		if(!Objects.isNull(dto) && !Objects.isNull(model)) {
			dto.setPatientId(model.getCode());
		}
		return dto;
	}

	@Override
	public MovementWard map2Model(MovementWardDTO dto) {
		var model = super.map2Model(dto);
		if(!Objects.isNull(model) && !Objects.isNull(dto) && !Objects.isNull(dto.getPatientId())) {
			var patient = new Patient();
			patient.setCode(dto.getPatientId());
			model.setPatient(patient);
		}
		return  model;
	}
}
