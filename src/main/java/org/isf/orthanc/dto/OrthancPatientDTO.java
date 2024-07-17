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
package org.isf.orthanc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class OrthancPatientDTO {

	private int id;
	
	@NotNull
	@Schema(description = "The oh patient id", example = "4")
	private Integer ohPatienId;
	
	@NotNull
	@Schema(description = "The orthanc patient id", example = "000000185")
	private String orthancPatientID;

	public OrthancPatientDTO() {}

	
	public OrthancPatientDTO(Integer ohPatienId, String orthancPatientID) {
		this.ohPatienId = ohPatienId;
		this.orthancPatientID = orthancPatientID;
	}


	public OrthancPatientDTO(int id, Integer ohPatienId, String orthancPatientID) {
		this.id = id;
		this.ohPatienId = ohPatienId;
		this.orthancPatientID = orthancPatientID;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Integer getOhPatienId() {
		return ohPatienId;
	}


	public void setOhPatienId(Integer ohPatienId) {
		this.ohPatienId = ohPatienId;
	}


	public String getOrthancPatientID() {
		return orthancPatientID;
	}


	public void setOrthancPatientID(String orthancPatientID) {
		this.orthancPatientID = orthancPatientID;
	}
	
}
