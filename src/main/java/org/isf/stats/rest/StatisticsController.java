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

	@GetMapping("/statistics/admittedPatientReport")
	public ResponseEntity<Resource> printAdmittedPatientPdf(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, HttpServletRequest request)
		throws OHServiceException, JRException, IOException {
		return getReport(reportsManager.getAdmittedPatientReportFromDateToDatePdf(fromDate, toDate, "AdmittedPatientReport", request.getLocale()), request);
	}

	@GetMapping("/statistics/pathologiesReport")
	public ResponseEntity<Resource> printPathologiesPdf(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate, HttpServletRequest request)
		throws OHServiceException, JRException, IOException {
		return getReport(reportsManager.getPathologiesByAgeGenderFromDateToDatePdf(fromDate, toDate, "PathologyByAgeGender", request.getLocale()), request);
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
