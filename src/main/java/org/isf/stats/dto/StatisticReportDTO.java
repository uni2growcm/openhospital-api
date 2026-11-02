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
package org.isf.stats.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StatisticReportDTO {

	@Schema(description = "The folder containing the report", example = "rpt_stat")
	private String folder;

	@Schema(description = "The report file name, without extension", example = "OH001_RegisteredPatients")
	private String fileName;

	@Schema(description = "The translated title of the report")
	private String title;

	@Schema(description = "The parameters expected by the report: DATE_RANGE (fromDate/toDate), MONTH_YEAR (month/year) or NONE")
	private String parameterType;

	public StatisticReportDTO() {
	}

	public StatisticReportDTO(String folder, String fileName, String title, String parameterType) {
		this.folder = folder;
		this.fileName = fileName;
		this.title = title;
		this.parameterType = parameterType;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
}
