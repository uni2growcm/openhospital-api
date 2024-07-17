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

public class OrthancConfigDTO {
	
	@Schema(description = "The orthanc config id")
	private Integer id; 
	
	@NotNull
	@Schema(description = "The oh user name", example = "admin")
	private String userName;
	
	@NotNull
	@Schema(description = "The orthanc config user name", example = "u2g")
	private String orthancUserName;
	
	@NotNull
	@Schema(description = "The orthanc config password", example = "u2g123")
	private String orthancPassword;

	public OrthancConfigDTO() {}

	public OrthancConfigDTO(String userName, String orthancUserName, String orthancPassword) {
		this.userName = userName;
		this.orthancUserName = orthancUserName;
		this.orthancPassword = orthancPassword;
	}
	
	public OrthancConfigDTO(Integer id, String userName, String orthancUserName, String orthancPassword) {
		this.id = id;
		this.userName = userName;
		this.orthancUserName = orthancUserName;
		this.orthancPassword = orthancPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
