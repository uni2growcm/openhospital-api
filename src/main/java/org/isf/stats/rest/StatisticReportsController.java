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
package org.isf.stats.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.dto.StatisticReportDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.stats.dto.StatisticReportDTO;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Statistic reports (folders {@code rpt_stat} and {@code rpt_extra}), equivalent to the "Reports" launcher of the GUI.
 */
@RestController
@Tag(name = "Reports")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/reports/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticReportsController {

	private final JasperReportsManager reportsManager;

	public StatisticReportsController(JasperReportsManager reportsManager) {
		this.reportsManager = reportsManager;
	}

	@GetMapping
	@Operation(summary = "List the available statistic reports with their translated title and expected parameters")
	public List<StatisticReportDTO> getStatisticReports(
		@Parameter(description = "Language of the titles (e.g. en, fr); defaults to the application language")
		@RequestParam(name = "lang", required = false) String language) {
		return reportsManager.getStatisticReports(language).stream()
			.map(StatisticReportsController::toDTO)
			.toList();
	}

	@GetMapping(value = "/{folder}/{fileName}", produces = { MediaType.APPLICATION_PDF_VALUE, "application/vnd.ms-excel",
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Download a statistic report as PDF or Excel",
		description = "DATE_RANGE reports require fromDate and toDate, MONTH_YEAR reports require month and year.")
	public ResponseEntity<byte[]> downloadStatisticReport(
		@PathVariable("folder") String folder,
		@PathVariable("fileName") String fileName,
		@RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@RequestParam(name = "month", required = false) Integer month,
		@RequestParam(name = "year", required = false) Integer year,
		@Parameter(description = "pdf (default), xlsx or xls")
		@RequestParam(name = "format", defaultValue = "pdf") String format) throws OHServiceException {

		StatisticReportDto report = reportsManager.getStatisticReport(folder, fileName, null);
		if (report == null) {
			throw new OHAPIException(new OHExceptionMessage("Report not found."), HttpStatus.NOT_FOUND);
		}
		boolean excel = ReportResponses.isExcel(format);

		return switch (report.getParameterType()) {
			case DATE_RANGE -> downloadDateRangeReport(report, fromDate, toDate, excel, format);
			case MONTH_YEAR -> downloadMonthYearReport(report, month, year, excel, format);
			case NONE -> {
				LocalDate now = LocalDate.now();
				yield downloadMonthYearReport(report, now.getMonthValue(), now.getYear(), excel, format);
			}
		};
	}

	private ResponseEntity<byte[]> downloadDateRangeReport(StatisticReportDto report, LocalDate fromDate, LocalDate toDate, boolean excel, String format)
		throws OHServiceException {
		if (fromDate == null || toDate == null) {
			throw new OHAPIException(new OHExceptionMessage("Parameters fromDate and toDate (yyyy-MM-dd) are required for this report."));
		}
		if (fromDate.isAfter(toDate)) {
			throw new OHAPIException(new OHExceptionMessage("fromDate must be before or equal to toDate."));
		}
		String folder = report.getFolder();
		String fileName = report.getFileName();
		String downloadName = fileName + '_' + fromDate + '_' + toDate;

		if (excel) {
			return ReportResponses.excel(format, downloadName,
				exportFilename -> reportsManager.getGenericReportFromDateToDateExcel(fromDate, toDate, folder, fileName, exportFilename));
		}
		return ReportResponses.pdf(reportsManager.getGenericReportFromDateToDatePdf(fromDate, toDate, folder, fileName), downloadName);
	}

	private ResponseEntity<byte[]> downloadMonthYearReport(StatisticReportDto report, Integer month, Integer year, boolean excel, String format)
		throws OHServiceException {
		if (month == null || year == null) {
			throw new OHAPIException(new OHExceptionMessage("Parameters month (1-12) and year are required for this report."));
		}
		if (month < 1 || month > 12) {
			throw new OHAPIException(new OHExceptionMessage("Parameter month must be between 1 and 12."));
		}
		String folder = report.getFolder();
		String fileName = report.getFileName();
		String downloadName = fileName + '_' + year + '_' + month;

		if (excel) {
			return ReportResponses.excel(format, downloadName,
				exportFilename -> reportsManager.getGenericReportMYExcel(month, year, folder, fileName, exportFilename));
		}
		return ReportResponses.pdf(reportsManager.getGenericReportMYPdf(month, year, folder, fileName), downloadName);
	}

	private static StatisticReportDTO toDTO(StatisticReportDto report) {
		return new StatisticReportDTO(report.getFolder(), report.getFileName(), report.getTitle(), report.getParameterType().name());
	}
}
