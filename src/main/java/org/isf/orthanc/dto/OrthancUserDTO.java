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

public class OrthancUserDTO {
	
	@Schema(description = "The orthanc user id")
	private Integer id; 
	
	@NotNull
	@Schema(description = "The oh user name", example = "admin")
	private String ohUserId;
	
	@NotNull
	@Schema(description = "The orthanc user name", example = "u2g")
	private String orthancUserName;
	
	@NotNull
	@Schema(description = "The orthanc password", example = "u2g123")
	private String orthancPassword;

	public OrthancUserDTO() {}

	public OrthancUserDTO(String ohUserId, String orthancUserName, String orthancPassword) {
		this.ohUserId = ohUserId;
		this.orthancUserName = orthancUserName;
		this.orthancPassword = orthancPassword;
	}
	
	public OrthancUserDTO(Integer id, String ohUserId, String orthancUserName, String orthancPassword) {
		this.id = id;
		this.ohUserId = ohUserId;
		this.orthancUserName = orthancUserName;
		this.orthancPassword = orthancPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOhUserId() {
		return ohUserId;
	}

	public void setOhUserId(String ohUserId) {
		this.ohUserId = ohUserId;
	}

	public String getOrthancUserName() {
		return orthancUserName;
	}

	public void setOrthancUserName(String orthancUserName) {
		this.orthancUserName = orthancUserName;
	}

	public String getOrthancPassword() {
		return orthancPassword;
	}

	public void setOrthancPassword(String orthancPassword) {
		this.orthancPassword = orthancPassword;
	}
}
