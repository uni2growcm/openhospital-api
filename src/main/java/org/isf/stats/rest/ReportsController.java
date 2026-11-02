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
import jakarta.servlet.http.HttpServletRequest;
import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.examination.manager.ExaminationBrowserManager;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.GeneralData;
import org.isf.medicalinventory.manager.MedicalInventoryManager;
import org.isf.medicalinventory.model.MedicalInventory;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Reports of the {@code rpt_base} folder, launched from the different modules (bills, OPD, admissions, pharmacy, ...).
 * Statistic reports are served by {@link StatisticReportsController}.
 */
@RestController
@Tag(name = "Reports")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/reports", produces = { MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class ReportsController {

	private static final String EXCEL_PRODUCES = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	private static final String XLS_PRODUCES = "application/vnd.ms-excel";

	private static final DateTimeFormatter USER_IN_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final List<String> CLINICAL_SHEET_SECTIONS = List.of("All", "Drugs", "Examination", "Admission", "Opd", "Laboratory", "Operations");

	private static final String PHARMACEUTICAL_STOCK_WARD = "PharmaceuticalStockWard";

	private static final String INVENTORY = "Inventory";

	private static final String PATIENT_BILL_PAYMENTS = "PatientBillPayments";

	private final JasperReportsManager reportsManager;
	private final ExaminationBrowserManager examinationBrowserManager;
	private final PatientBrowserManager patientBrowserManager;
	private final BillBrowserManager billBrowserManager;
	private final OpdBrowserManager opdBrowserManager;
	private final AdmissionBrowserManager admissionBrowserManager;
	private final WardBrowserManager wardBrowserManager;
	private final MedicalBrowsingManager medicalBrowsingManager;
	private final MedicalInventoryManager medicalInventoryManager;

	public ReportsController(JasperReportsManager reportsManager, ExaminationBrowserManager examinationBrowserManager,
		PatientBrowserManager patientBrowserManager, BillBrowserManager billBrowserManager, OpdBrowserManager opdBrowserManager,
		AdmissionBrowserManager admissionBrowserManager, WardBrowserManager wardBrowserManager, MedicalBrowsingManager medicalBrowsingManager,
		MedicalInventoryManager medicalInventoryManager) {
		this.reportsManager = reportsManager;
		this.examinationBrowserManager = examinationBrowserManager;
		this.patientBrowserManager = patientBrowserManager;
		this.billBrowserManager = billBrowserManager;
		this.opdBrowserManager = opdBrowserManager;
		this.admissionBrowserManager = admissionBrowserManager;
		this.wardBrowserManager = wardBrowserManager;
		this.medicalBrowsingManager = medicalBrowsingManager;
		this.medicalInventoryManager = medicalInventoryManager;
	}

	/*
	 * Lists
	 */

	@GetMapping("/exams-list")
	@Operation(summary = "Download the list of exams")
	public ResponseEntity<byte[]> printExamsListPdf() throws OHServiceException {
		return ReportResponses.pdf(reportsManager.getExamsListPdf(), "examslist");
	}

	@GetMapping("/diseases-list")
	@Operation(summary = "Download the list of diseases")
	public ResponseEntity<byte[]> printDiseasesListPdf() throws OHServiceException {
		return ReportResponses.pdf(reportsManager.getDiseasesListPdf(), "diseaseslist");
	}

	@GetMapping("/operations-list")
	@Operation(summary = "Download the list of operations")
	public ResponseEntity<byte[]> printOperationsListPdf() throws OHServiceException {
		return ReportResponses.pdf(reportsManager.getOperationsListPdf(), "operationslist");
	}

	/*
	 * Patient
	 */

	@GetMapping("/patientexamination/{examinationId}")
	@Operation(summary = "Download a patient examination")
	public ResponseEntity<byte[]> printPatientExaminationPdf(@PathVariable("examinationId") int examinationId, HttpServletRequest request)
		throws OHServiceException {
		PatientExamination patientExamination = examinationBrowserManager.getByID(examinationId);
		if (patientExamination == null) {
			throw notFound("Patient examination not found.");
		}
		int patId = patientExamination.getPatient().getCode();
		return ReportResponses.pdf(reportsManager.getGenericReportPatientExaminationPdf(patId, examinationId, request.getLocale()),
			"examination_" + examinationId);
	}

	@GetMapping("/patientexamrequest/{patientId}")
	@Operation(summary = "Download the exam requests of a patient")
	public ResponseEntity<byte[]> printPatientExamRequestPdf(@PathVariable("patientId") int patientId, HttpServletRequest request)
		throws OHServiceException {
		requirePatient(patientId);
		return ReportResponses.pdf(reportsManager.getGenericReportPatientExamRequestPdf(patientId, request.getLocale()), "exam_request_" + patientId);
	}

	@GetMapping("/patients/{patientId}/clinical-sheet")
	@Operation(summary = "Download the clinical sheet (patient folder) of a patient")
	public ResponseEntity<byte[]> printPatientClinicalSheetPdf(
		@PathVariable("patientId") int patientId,
		@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@Parameter(description = "Sections to print: All, Drugs, Examination, Admission, Opd, Laboratory, Operations")
		@RequestParam(name = "sections", defaultValue = "All") List<String> sections) throws OHServiceException {
		requirePatient(patientId);
		checkDateRange(fromDate, toDate);
		for (String section : sections) {
			if (!CLINICAL_SHEET_SECTIONS.contains(section)) {
				throw new OHAPIException(new OHExceptionMessage("Unknown section '" + section + "': allowed values are " + CLINICAL_SHEET_SECTIONS + '.'));
			}
		}
		return ReportResponses.pdf(reportsManager.getGenericReportPatientVersion2Pdf(patientId, String.join(",", sections), fromDate.atStartOfDay(),
			toDate.atStartOfDay(), GeneralData.PATIENTSHEET), GeneralData.PATIENTSHEET + '_' + patientId);
	}

	/*
	 * OPD and admissions
	 */

	@GetMapping("/opds/{opdCode}")
	@Operation(summary = "Download the OPD chart of a visit")
	public ResponseEntity<byte[]> printOpdPdf(@PathVariable("opdCode") int opdCode) throws OHServiceException {
		Opd opd = opdBrowserManager.getOpdById(opdCode).orElseThrow(() -> notFound("OPD not found."));
		return ReportResponses.pdf(reportsManager.getGenericReportOpdPdf(opdCode, opd.getPatient().getCode(), GeneralData.OPDCHART),
			GeneralData.OPDCHART + '_' + opdCode);
	}

	@GetMapping("/admissions/{admissionId}")
	@Operation(summary = "Download the admission chart of an admission")
	public ResponseEntity<byte[]> printAdmissionPdf(@PathVariable("admissionId") int admissionId) throws OHServiceException {
		Admission admission = requireAdmission(admissionId);
		return ReportResponses.pdf(reportsManager.getGenericReportAdmissionPdf(admissionId, admission.getPatient().getCode(), GeneralData.ADMCHART),
			GeneralData.ADMCHART + '_' + admissionId);
	}

	@GetMapping("/admissions/{admissionId}/discharge")
	@Operation(summary = "Download the discharge chart of an admission")
	public ResponseEntity<byte[]> printDischargePdf(@PathVariable("admissionId") int admissionId) throws OHServiceException {
		Admission admission = requireAdmission(admissionId);
		return ReportResponses.pdf(reportsManager.getGenericReportDischargePdf(admissionId, admission.getPatient().getCode(), GeneralData.DISCHART),
			GeneralData.DISCHART + '_' + admissionId);
	}

	@GetMapping("/wards/{wardCode}/visits")
	@Operation(summary = "Download the ward visits of a day")
	public ResponseEntity<byte[]> printWardVisitsPdf(
		@PathVariable("wardCode") String wardCode,
		@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws OHServiceException {
		Ward ward = requireWard(wardCode);
		LocalDate day = date != null ? date : LocalDate.now();
		return ReportResponses.pdf(reportsManager.getGenericReportWardVisitPdf(ward.getCode(), day.atStartOfDay(), GeneralData.VISITSHEET),
			GeneralData.VISITSHEET + '_' + ward.getCode() + '_' + day);
	}

	/*
	 * Accounting
	 */

	@GetMapping("/bills/{billId}")
	@Operation(summary = "Download a patient bill")
	public ResponseEntity<byte[]> printBillPdf(@PathVariable("billId") int billId) throws OHServiceException {
		requireBill(billId);
		return ReportResponses.pdf(reportsManager.getGenericReportBillPdf(billId, GeneralData.PATIENTBILL, false, false),
			GeneralData.PATIENTBILL + '_' + billId);
	}

	@GetMapping("/bills/{billId}/payments")
	@Operation(summary = "Download a patient bill with its payments")
	public ResponseEntity<byte[]> printBillPaymentsPdf(@PathVariable("billId") int billId) throws OHServiceException {
		requireBill(billId);
		return ReportResponses.pdf(reportsManager.getGenericReportBillPdf(billId, PATIENT_BILL_PAYMENTS, false, false), PATIENT_BILL_PAYMENTS + '_' + billId);
	}

	@GetMapping("/bills/grouped")
	@Operation(summary = "Download several bills grouped in one report")
	public ResponseEntity<byte[]> printBillsGroupedPdf(@RequestParam(name = "billIds") List<Integer> billIds) throws OHServiceException {
		if (billIds.isEmpty()) {
			throw new OHAPIException(new OHExceptionMessage("At least one bill id is required."));
		}
		for (Integer billId : billIds) {
			requireBill(billId);
		}
		Integer firstBillId = billIds.get(0);
		return ReportResponses.pdf(reportsManager.getGenericReportBillGroupedPdf(firstBillId, GeneralData.PATIENTBILLGROUPED, null, billIds, null, null,
			false, false), GeneralData.PATIENTBILLGROUPED + '_' + firstBillId);
	}

	@GetMapping("/patients/{patientId}/bill-statement")
	@Operation(summary = "Download the bill statement of a patient")
	public ResponseEntity<byte[]> printPatientBillStatementPdf(@PathVariable("patientId") int patientId) throws OHServiceException {
		requirePatient(patientId);
		return ReportResponses.pdf(reportsManager.getGenericReportPatientPdf(patientId, GeneralData.PATIENTBILLSTATEMENT),
			GeneralData.PATIENTBILLSTATEMENT + '_' + patientId);
	}

	@GetMapping("/bills/user-in-date")
	@Operation(summary = "Download the bills report of a user over a period")
	public ResponseEntity<byte[]> printBillsUserInDatePdf(
		@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@RequestParam(name = "user") String user) throws OHServiceException {
		checkDateRange(fromDate, toDate);
		String jasperFileName = "BillsReportUserInDate";
		return ReportResponses.pdf(reportsManager.getGenericReportUserInDatePdf(fromDate.atStartOfDay().format(USER_IN_DATE_FORMAT),
			toDate.atTime(LocalTime.MAX).format(USER_IN_DATE_FORMAT), user, jasperFileName), jasperFileName + '_' + user);
	}

	/*
	 * Pharmacy
	 */

	@GetMapping("/pharmacy/order")
	@Operation(summary = "Download the pharmaceutical order")
	public ResponseEntity<byte[]> printPharmaceuticalOrderPdf() throws OHServiceException {
		return ReportResponses.pdf(reportsManager.getGenericReportPharmaceuticalOrderPdf(GeneralData.PHARMACEUTICALORDER), GeneralData.PHARMACEUTICALORDER);
	}

	@GetMapping(value = "/pharmacy/stock", produces = { MediaType.APPLICATION_PDF_VALUE, EXCEL_PRODUCES, XLS_PRODUCES, MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Download the pharmaceutical stock at a date as PDF or Excel")
	public ResponseEntity<byte[]> printPharmaceuticalStock(
		@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@RequestParam(name = "filter", required = false) String filter,
		@RequestParam(name = "groupBy", required = false) String groupBy,
		@RequestParam(name = "sortBy", required = false) String sortBy,
		@Parameter(description = "pdf (default), xlsx or xls")
		@RequestParam(name = "format", defaultValue = "pdf") String format) throws OHServiceException {
		LocalDateTime dateTime = endOfDay(date);
		String jasperFileName = GeneralData.PHARMACEUTICALSTOCK;
		String downloadName = jasperFileName + '_' + dateTime.toLocalDate();
		if (ReportResponses.isExcel(format)) {
			return ReportResponses.excel(format, downloadName, exportFilename -> reportsManager
				.getGenericReportPharmaceuticalStockExcel(dateTime, jasperFileName, exportFilename, filter, groupBy, sortBy));
		}
		return ReportResponses.pdf(reportsManager.getGenericReportPharmaceuticalStockPdf(dateTime, jasperFileName, filter, groupBy, sortBy), downloadName);
	}

	@GetMapping(value = "/pharmacy/amc", produces = { MediaType.APPLICATION_PDF_VALUE, EXCEL_PRODUCES, XLS_PRODUCES, MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Download the average monthly consumption (AMC) as PDF or Excel")
	public ResponseEntity<byte[]> printPharmaceuticalAMC(
		@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@Parameter(description = "pdf (default), xlsx or xls")
		@RequestParam(name = "format", defaultValue = "pdf") String format) throws OHServiceException {
		LocalDateTime dateTime = endOfDay(date);
		String jasperFileName = GeneralData.PHARMACEUTICALAMC;
		String downloadName = jasperFileName + '_' + dateTime.toLocalDate();
		if (ReportResponses.isExcel(format)) {
			return ReportResponses.excel(format, downloadName,
				exportFilename -> reportsManager.getGenericReportPharmaceuticalAMCExcel(dateTime, jasperFileName, exportFilename));
		}
		return ReportResponses.pdf(reportsManager.GenericReportPharmaceuticalAMCPdf(dateTime, jasperFileName), downloadName);
	}

	@GetMapping(value = "/pharmacy/stock-card", produces = { MediaType.APPLICATION_PDF_VALUE, EXCEL_PRODUCES, XLS_PRODUCES, MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Download the stock card (product ledger) as PDF or Excel",
		description = "With medicalCode: ledger of one product, otherwise ledger of all products (PDF only). With wardCode: ward pharmacy, otherwise main store.")
	public ResponseEntity<byte[]> printPharmaceuticalStockCard(
		@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@RequestParam(name = "medicalCode", required = false) Integer medicalCode,
		@RequestParam(name = "wardCode", required = false) String wardCode,
		@Parameter(description = "pdf (default), xlsx or xls")
		@RequestParam(name = "format", defaultValue = "pdf") String format) throws OHServiceException {
		checkDateRange(fromDate, toDate);
		Medical medical = null;
		if (medicalCode != null) {
			medical = medicalBrowsingManager.getMedical(medicalCode);
			if (medical == null) {
				throw notFound("Medical not found.");
			}
		}
		Ward ward = wardCode != null ? requireWard(wardCode) : null;
		boolean excel = ReportResponses.isExcel(format);
		if (excel && medical == null) {
			throw new OHAPIException(new OHExceptionMessage("The Excel export of the stock card requires a medicalCode."));
		}

		String jasperFileName = (ward != null ? "ProductLedgerWard" : "ProductLedger") + (medical == null ? "_multi" : "");
		LocalDateTime dateFrom = fromDate.atStartOfDay();
		LocalDateTime dateTo = toDate.atTime(LocalTime.MAX);
		String downloadName = jasperFileName + '_' + fromDate + '_' + toDate;
		Medical selectedMedical = medical;
		if (excel) {
			return ReportResponses.excel(format, downloadName, exportFilename -> reportsManager
				.getGenericReportPharmaceuticalStockCardExcel(jasperFileName, exportFilename, dateFrom, dateTo, selectedMedical, ward));
		}
		return ReportResponses.pdf(reportsManager.getGenericReportPharmaceuticalStockCardPdf(jasperFileName, null, dateFrom, dateTo, medical, ward),
			downloadName);
	}

	@GetMapping("/wards/{wardCode}/pharmacy-stock")
	@Operation(summary = "Download the stock of a ward pharmacy at a date")
	public ResponseEntity<byte[]> printPharmaceuticalStockWardPdf(
		@PathVariable("wardCode") String wardCode,
		@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws OHServiceException {
		Ward ward = requireWard(wardCode);
		LocalDateTime dateTime = endOfDay(date);
		return ReportResponses.pdf(reportsManager.getGenericReportPharmaceuticalStockWardPdf(dateTime, PHARMACEUTICAL_STOCK_WARD, ward),
			PHARMACEUTICAL_STOCK_WARD + '_' + ward.getCode() + '_' + dateTime.toLocalDate());
	}

	@GetMapping("/inventories/{inventoryId}")
	@Operation(summary = "Download an inventory (main store or ward)")
	public ResponseEntity<byte[]> printInventoryPdf(
		@PathVariable("inventoryId") int inventoryId,
		@Parameter(description = "1 to print the real quantities, 0 to print an empty column to be filled")
		@RequestParam(name = "printRealQty", defaultValue = "1") int printRealQty) throws OHServiceException {
		MedicalInventory inventory = medicalInventoryManager.getInventoryById(inventoryId);
		if (inventory == null) {
			throw notFound("Inventory not found.");
		}
		return ReportResponses.pdf(reportsManager.getInventoryReportPdf(inventory, INVENTORY, printRealQty), INVENTORY + '_' + inventoryId);
	}

	private void requirePatient(int patientId) throws OHServiceException {
		Patient patient = patientBrowserManager.getPatientById(patientId);
		if (patient == null) {
			throw notFound("Patient not found.");
		}
	}

	private void requireBill(int billId) throws OHServiceException {
		Bill bill = billBrowserManager.getBill(billId);
		if (bill == null) {
			throw notFound("Bill " + billId + " not found.");
		}
	}

	private Admission requireAdmission(int admissionId) throws OHServiceException {
		Admission admission = admissionBrowserManager.getAdmission(admissionId);
		if (admission == null) {
			throw notFound("Admission not found.");
		}
		return admission;
	}

	private Ward requireWard(String wardCode) throws OHServiceException {
		Ward ward = wardBrowserManager.findWard(wardCode);
		if (ward == null) {
			throw notFound("Ward not found.");
		}
		return ward;
	}

	private static void checkDateRange(LocalDate fromDate, LocalDate toDate) throws OHAPIException {
		if (fromDate.isAfter(toDate)) {
			throw new OHAPIException(new OHExceptionMessage("fromDate must be before or equal to toDate."));
		}
	}

	private static LocalDateTime endOfDay(LocalDate date) {
		return (date != null ? date : LocalDate.now()).atTime(LocalTime.MAX);
	}

	private static OHAPIException notFound(String message) {
		return new OHAPIException(new OHExceptionMessage(message), HttpStatus.NOT_FOUND);
	}
}
