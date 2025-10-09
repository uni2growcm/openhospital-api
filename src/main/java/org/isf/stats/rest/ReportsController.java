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
package org.isf.stats.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Locale;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.apache.poi.util.IOUtils;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.generaldata.GeneralData;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.stats.rest.model.EnumOption;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.nio.file.Files;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Reports")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportsController {

	private final JasperReportsManager reportsManager;
	private final MedicalBrowsingManager medicalBrowsingManager;
	private final WardBrowserManager wardBrowserManager;

	private static final String PHARMACEUTICAL_STOCK_CARD_REPORT = "ProductLedger";
	private static final String PHARMACEUTICAL_STOCK_WARD_REPORT = "PharmaceuticalStockWard";

	public ReportsController(JasperReportsManager reportsManager, MedicalBrowsingManager medicalBrowsingManager, WardBrowserManager wardBrowserManager) {
		this.reportsManager = reportsManager;
		this.medicalBrowsingManager = medicalBrowsingManager;
		this.wardBrowserManager = wardBrowserManager;
	}

	@GetMapping("/reports/exams-list")
	public ResponseEntity<byte[]> printExamsListPdf(HttpServletRequest request) throws OHServiceException, IOException {
		return getReport(reportsManager.getExamsListPdf(), request);
	}

	@GetMapping("/reports/diseases-list")
	public ResponseEntity<byte[]> printDiseasesListPdf(HttpServletRequest request) throws OHServiceException, IOException {
		return getReport(reportsManager.getDiseasesListPdf(), request);
	}

	@GetMapping("/reports/pharmaceuticalStockCard")
	public ResponseEntity<byte[]> printPharmaceuticalStockCardPdf(
		@RequestParam String exportFileName,
		@RequestParam LocalDateTime dateFrom,
		@RequestParam LocalDateTime dateTo,
		@RequestParam Integer medicalCode,
		@RequestParam String wardCode,
		HttpServletRequest request
	) throws  OHServiceException, IOException {
		Medical medical = medicalBrowsingManager.getMedical(medicalCode);
		if (medical == null) {
			throw new OHAPIException(new OHExceptionMessage("Medical not found."), HttpStatus.NOT_FOUND);
		}

		Ward ward = wardBrowserManager.findWard(wardCode);
		if (ward == null) {
			throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
		}

		return getReport(reportsManager.getGenericReportPharmaceuticalStockCardPdf(PHARMACEUTICAL_STOCK_CARD_REPORT, exportFileName, dateFrom, dateTo, medical, ward, request.getLocale()), request);
	}

	@GetMapping(value = "/reports/pharmaceuticalStock", produces = {
		MediaType.APPLICATION_PDF_VALUE,
		MediaType.APPLICATION_OCTET_STREAM_VALUE
	})
	public ResponseEntity<?> printPharmaceuticalStockReport(
		HttpServletRequest request,
		@RequestParam EnumOption option,
		@RequestParam LocalDateTime date,
		@RequestParam(name = "groupBy", defaultValue = "") String groupBy,
		@RequestParam(name = "sortBy", defaultValue = "") String sortBy,
		@RequestParam(name = "filter", defaultValue = "") String filter,
		@RequestParam(name = "toExcel", defaultValue = "false") boolean toExcel
	) throws OHServiceException, IOException {

		if (groupBy.isEmpty()) groupBy = null;
		if (sortBy.isEmpty()) sortBy = null;
		if (filter.isEmpty()) filter = null;

		Locale locale = request.getLocale();

		String jasperFileName = (option == EnumOption.ONLY_QUANTITY)
			? GeneralData.PHARMACEUTICALSTOCK
			: GeneralData.PHARMACEUTICALSTOCKLOT;

		if (toExcel) {
			File tempFile = File.createTempFile("pharmaStock", ".xlsx");
			String exportPath = tempFile.getAbsolutePath();

			reportsManager.getGenericReportPharmaceuticalStockExcel(
				date,
				jasperFileName,
				exportPath,
				filter,
				groupBy,
				sortBy,
				locale
			);

			byte[] fileContent = Files.readAllBytes(tempFile.toPath());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "pharmaceutical_stock.xlsx");

			return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

		} else {
			return getReport(
				reportsManager.getGenericReportPharmaceuticalStockPdf(
					date,
					jasperFileName,
					filter,
					groupBy,
					sortBy,
					locale
				),
				request
			);
		}
	}

	@GetMapping("/reports/pharmaceuticalOrder")
	public ResponseEntity<byte[]> printPharmaceuticalOrderPdf(HttpServletRequest request) throws OHServiceException, JRException {
		JasperReportResultDto result = reportsManager.getGenericReportPharmaceuticalOrder2Pdf("PharmaceuticalOrder", request.getLocale());

		byte[] pdfBytes = JasperExportManager.exportReportToPdf(result.getJasperPrint());

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PharmaceuticalOrder.pdf")
			.contentType(MediaType.APPLICATION_PDF)
			.body(pdfBytes);
	}

	@GetMapping("/reports/pharmaceuticalStockWard")
	public ResponseEntity<byte[]> printPharmaceuticalStockWardPdf(
		@RequestParam LocalDateTime date,
		@RequestParam String wardCode,
		HttpServletRequest request
	) throws OHServiceException, IOException {
		Ward ward = wardBrowserManager.findWard(wardCode);
		if (ward == null) {
			throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
		}

		return getReport(reportsManager.getGenericReportPharmaceuticalStockWardPdf(date, PHARMACEUTICAL_STOCK_WARD_REPORT, ward, request.getLocale()), request);
	}

	private ResponseEntity<byte[]> getReport(
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

		String contentType;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new OHAPIException(new OHExceptionMessage("Failed to load the file's type."));
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		byte[] out = IOUtils.toByteArray(resource.getInputStream());

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resource.getFilename() + '"')
			.body(out);
	}
}
