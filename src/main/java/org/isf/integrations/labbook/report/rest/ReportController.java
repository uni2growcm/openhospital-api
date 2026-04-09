package org.isf.integrations.labbook.report.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.integrations.labbook.models.ReportGroupedRequest;
import org.isf.integrations.labbook.services.ReportSyncService;
import org.isf.utils.exception.OHException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Labbook")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping
public class ReportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

	private final ReportSyncService reportSyncService;

	public ReportController(ReportSyncService reportSyncService) {
		this.reportSyncService = reportSyncService;
	}

	@PostMapping("/labbook/reports/grouped/download")
	public ResponseEntity<byte[]> generateReportGrouped(@RequestBody ReportGroupedRequest request) throws OHException {
		LOGGER.info("Received request for grouped report download");
		byte[] pdfContent = reportSyncService.generateReportGrouped(request);

		String filename = request.filename() != null ? request.filename() : "report.pdf";

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_PDF)
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
			.body(pdfContent);
	}
}