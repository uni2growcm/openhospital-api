/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2025 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.apache.poi.util.IOUtils;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.generaldata.GeneralData;
import org.isf.medicalstock.manager.MovBrowserManager;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medicalstock.manager.MovBrowserManager;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.serviceprinting.manager.PrintManager;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.stats.rest.model.EnumOption;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.sql.rowset.serial.SerialBlob;

@RestController
@Tag(name = "Reports")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportsController {

	private final JasperReportsManager reportsManager;
	private final MedicalBrowsingManager medicalBrowsingManager;
	private final WardBrowserManager wardBrowserManager;
	private final PrintManager printManager;
	private final MovWardBrowserManager movWardBrowserManager;
	private final MovBrowserManager movBrowserManager;

	private static final String WARD_STOCK_CARD_REPORT = "ProductLedgerWard";
	private static final String PHARMACEUTICAL_STOCK_CARD_REPORT = "ProductLedger";
	private static final String PHARMACEUTICAL_AMC_REPORT = "PharmaceuticalAMC";
	private static final String PHARMACEUTICAL_STOCK_WARD_REPORT = "PharmaceuticalStockWard";
	private static final String WARD_PHARMACY_INCOMES = "WardPharmacyIncomes";
	private static final String WARD_PHARMACY_OUTCOMES = "WardPharmacyOutcomes";

	public ReportsController(
		JasperReportsManager reportsManager,
		MedicalBrowsingManager medicalBrowsingManager,
		WardBrowserManager wardBrowserManager,
		PrintManager printManager,
		MovWardBrowserManager movWardBrowserManager,
		MovBrowserManager movBrowserManager
	) {
		this.reportsManager = reportsManager;
		this.medicalBrowsingManager = medicalBrowsingManager;
		this.wardBrowserManager = wardBrowserManager;
		this.movBrowserManager = movBrowserManager;
		this.movWardBrowserManager = movWardBrowserManager;
		this.printManager = printManager;
	}

	@GetMapping("/reports/exams-list")
	public ResponseEntity<Resource> printExamsListPdf(HttpServletRequest request) throws OHServiceException, IOException {
		return getReport(reportsManager.getExamsListPdf(), request);
	}

	@GetMapping("/reports/diseases-list")
	public ResponseEntity<Resource> printDiseasesListPdf(HttpServletRequest request) throws OHServiceException, IOException {
		return getReport(reportsManager.getDiseasesListPdf(), request);
	}

	@GetMapping("/reports/pharmaceuticalStockCard")
	public ResponseEntity<Resource> printPharmaceuticalStockCardPdf(
		@RequestParam String exportFileName,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateFrom,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateTo,
		@RequestParam Integer medicalCode,
		@RequestParam(required = false) String wardCode,
		HttpServletRequest request
	) throws  OHServiceException, IOException {
		Medical medical = medicalBrowsingManager.getMedical(medicalCode);
		if (medical == null) {
			throw new OHAPIException(new OHExceptionMessage("Medical not found."), HttpStatus.NOT_FOUND);
		}

		String jasperReport = PHARMACEUTICAL_STOCK_CARD_REPORT;
		Ward ward = null;
		if (wardCode != null) {
			jasperReport = WARD_STOCK_CARD_REPORT;
			ward = wardBrowserManager.findWard(wardCode);
			if (ward == null) {
				throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
			}
		}
		return getReport(reportsManager.getGenericReportPharmaceuticalStockCardPdf(jasperReport, exportFileName, dateFrom, dateTo, medical, ward, request.getLocale()), request);
	}

	@GetMapping(value = "/reports/pharmaceuticalStock", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> printPharmaceuticalStockPdf(
		HttpServletRequest request,
		@RequestParam String option,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime date,
		@RequestParam(name = "groupBy", defaultValue = "") String groupBy,
		@RequestParam(name="sortBy", defaultValue = "") String sortBy,
		@RequestParam(name = "filter", defaultValue = "") String filter
	) throws OHServiceException, IOException {
		if (groupBy.isEmpty()) {
			groupBy = null;
		}
		if (sortBy.isEmpty()) {
			sortBy = null;
		}
		if (filter.isEmpty()) {
			filter = null;
		}
		Locale locale = request.getLocale();
		if (EnumOption.ONLY_QUANTITY.toString().equalsIgnoreCase(option)) {
			return getReport(reportsManager.getGenericReportPharmaceuticalStockPdf(
				date, GeneralData.PHARMACEUTICALSTOCK, filter, groupBy, sortBy,locale
			), request);
		} else {
			return getReport(reportsManager.getGenericReportPharmaceuticalStockPdf(
				date, GeneralData.PHARMACEUTICALSTOCKLOT, filter, groupBy, sortBy,locale
			), request);
		}
	}

	@GetMapping("/reports/pharmaceuticalAMC")
	public ResponseEntity<Resource> printPharmaceuticalAMC(
		HttpServletRequest request,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime date
	) throws OHServiceException, IOException {
		return getReport(reportsManager.GenericReportPharmaceuticalAMCPdf(date, PHARMACEUTICAL_AMC_REPORT, request.getLocale()), request);
	}

	@GetMapping("/reports/pharmaceuticalOrder")
	public ResponseEntity<Resource> printPharmaceuticalOrderPdf(HttpServletRequest request) throws OHServiceException, JRException {
		JasperReportResultDto result = reportsManager.getGenericReportPharmaceuticalOrder2Pdf("PharmaceuticalOrder", request.getLocale());

		byte[] pdfBytes = JasperExportManager.exportReportToPdf(result.getJasperPrint());

		ByteArrayResource resource = new ByteArrayResource(pdfBytes);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PharmaceuticalOrder.pdf")
			.contentType(MediaType.APPLICATION_PDF)
			.body(resource);
	}

	@GetMapping("/reports/pharmaceuticalExpiration")
	public ResponseEntity<Resource> printPharmaceuticalExpirationPdf(
		@RequestParam LocalDate fromDate,
		@RequestParam LocalDate toDate,
		HttpServletRequest request) throws OHServiceException, IOException {
		return getReport(reportsManager.getGenericReportFromDateToDate2Pdf(fromDate, toDate, "PharmaceuticalExpiration", request.getLocale()), request);
	}

	@GetMapping("/reports/pharmaceuticalStockWard")
	public ResponseEntity<Resource> printPharmaceuticalStockWardPdf(
		@RequestParam String wardCode,
		@RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateFrom,
		@RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateTo,
		@RequestParam(value = "stockWardReportModel", required = false, defaultValue = "OUTCOMING") StockWardReportModel stockWardReportModel,
		HttpServletRequest request
	) throws OHServiceException, IOException {
		Ward ward = wardBrowserManager.findWard(wardCode);
		if (ward == null) {
			throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
		}

		if (dateFrom == null) {
			dateFrom = LocalDateTime.now();
		}

		if (dateTo == null) {
			dateTo = LocalDateTime.now();
		}

		if (stockWardReportModel == StockWardReportModel.OUTCOMING) {
			List<MovementWard> movementWardList = movWardBrowserManager.getMovementWard(wardCode, dateFrom, dateTo);
			return getReport(reportsManager.getIncomesOrOutComesStockWardPdf(WARD_PHARMACY_OUTCOMES, movWardBrowserManager.convertMovementWardForPrint(movementWardList)), request); //$NON-NLS-1$
		} else if (stockWardReportModel == StockWardReportModel.INCOMING) {
			List<Movement> movementList = getIncomesMov(ward, dateFrom, dateTo);
			return getReport(reportsManager.getIncomesOrOutComesStockWardPdf(WARD_PHARMACY_INCOMES, movWardBrowserManager.convertMovementForPrint(movementList)), request);
		}

		return getReport(reportsManager.getGenericReportPharmaceuticalStockWardPdf(dateFrom, PHARMACEUTICAL_STOCK_WARD_REPORT, ward, request.getLocale()), request);
	}

	@GetMapping("/reports/pharmaceuticalStockWardExcel")
	public ResponseEntity<Resource> printPharmaceuticalStockWardExcel(
		@RequestParam String wardCode,
		@RequestParam(value = "medicalCode", required = false) Integer medicalCode,
		@RequestParam(value = "medicalTypeCode", required = false) String medicalTypeCode,
		@RequestParam(value = "sex", required = false, defaultValue = "A") char sex,
		@RequestParam(value = "ageFrom", required = false, defaultValue = "0") int ageFrom,
		@RequestParam(value = "ageTo", required = false, defaultValue = "0") int ageTo,
		@RequestParam(value = "weightFrom", required = false, defaultValue = "0") float weightFrom,
		@RequestParam(value = "weightTo", required = false, defaultValue = "0") float weightTo,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateFrom,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDateTime dateTo,
		@RequestParam(value = "index", required = false, defaultValue = "0") int index
	) throws OHServiceException, IOException, SQLException {
		Ward ward = wardBrowserManager.findWard(wardCode);
		if (ward == null) {
			throw new OHAPIException(new OHExceptionMessage("Ward not found."), HttpStatus.NOT_FOUND);
		}

		File tempFile = File.createTempFile(PHARMACEUTICAL_STOCK_WARD_REPORT, ".xlsx");
		String exportPath = tempFile.getAbsolutePath();

		reportsManager.getGenericReportPharmaceuticalStockWardExcel(
			exportPath,
			ward,
			medicalCode,
			medicalTypeCode,
			sex,
			ageFrom,
			ageTo,
			weightFrom,
			weightTo,
			dateFrom,
			dateTo,
			index
		);

		byte[] fileContent = Files.readAllBytes(tempFile.toPath());
		Blob blob = new SerialBlob(fileContent);
		ByteArrayResource resource = new ByteArrayResource(blob.getBinaryStream().readAllBytes());

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resource.getFilename() + '"')
			.body(resource);
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

	private List<Movement> getIncomesMov(Ward ward, LocalDateTime dateFrom, LocalDateTime dateTo) throws OHAPIException {

		List<Movement> wardIncomes = new ArrayList<>();
		try {
			List<Movement> listMovementCentral = movBrowserManager.getMovements(ward.getCode(), dateFrom, dateTo);

			for (Movement mov : listMovementCentral) {
				if (mov.getWard().getDescription() != null) {
					if (mov.getWard().equals(ward)) {
						wardIncomes.add(mov);
					}
				}
			}

			// List movements from other wards
			for (MovementWard wMvnt : movWardBrowserManager.getWardMovementsToWard(ward.getCode(), dateFrom, dateTo)) {
				if (wMvnt.getWardTo().getDescription() != null) {
					if (wMvnt.getWardTo().equals(ward)) {
						MovementType typeCharge = new MovementType("fromward", wMvnt.getWard().getDescription(), "*", "*");
						wardIncomes.add(new Movement(
							wMvnt.getMedical(),
							typeCharge,
							ward,
							wMvnt.getLot(),
							wMvnt.getDate(),
							wMvnt.getQuantity().intValue(),
							null,
							null));
					}
				}
			}
		} catch (OHServiceException ohServiceException) {
			throw new OHAPIException(new OHExceptionMessage(ohServiceException.getMessage()));
		}
		return wardIncomes;
	}
}