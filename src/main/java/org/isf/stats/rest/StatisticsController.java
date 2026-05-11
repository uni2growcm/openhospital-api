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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@RestController
@Tag(name = "Statistics")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
public class StatisticsController {

	private final JasperReportsManager reportsManager;

	public StatisticsController(JasperReportsManager reportsManager) {
		this.reportsManager = reportsManager;
	}

	@GetMapping("/statistics/pathologiesbyagegender")
	public ResponseEntity<Resource> printPathologiesByAgeGenderPdf(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, HttpServletRequest request)
		throws OHServiceException, JRException, IOException {
		return getReport(reportsManager.getStatisticsReportPdf(fromDate, toDate, "pathology_by_age_gender", request.getLocale()), request);
	}

	@GetMapping("/statistics/pathologies")
	public ResponseEntity<Resource> printPathologiesPdf(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, HttpServletRequest request)
		throws OHServiceException, JRException, IOException {
		return getReport(reportsManager.getStatisticsReportPdf(fromDate, toDate, "pathology_report", request.getLocale()), request);
	}

	@GetMapping("/statistics/dischargesstatistics")
	public ResponseEntity<Resource> printDischargesPdf(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, HttpServletRequest request)
		throws OHServiceException, JRException, IOException {
		return getReport(reportsManager.getStatisticsReportPdf(fromDate, toDate, "discharge_statistics_report", request.getLocale()), request);
	}

	private ResponseEntity<Resource> getReport(
		JasperReportResultDto resultDto, HttpServletRequest request
	) throws OHServiceException, IOException {
		Path report = Paths.get(resultDto.getFilename()).normalize();
		Resource resource;
		try {
			resource = new UrlResource(report.toUri());
			if (!resource.exists()) {
				throw new OHAPIException(new OHExceptionMessage("File not found."));
			}
		} catch (MalformedURLException e) {
			throw new OHAPIException(new OHExceptionMessage("File not found."));
		}

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resource.getFilename() + '"')
			.body(resource);
	}
}
