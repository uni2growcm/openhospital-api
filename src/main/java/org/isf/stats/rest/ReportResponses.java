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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.isf.shared.exceptions.OHAPIException;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

/**
 * Helpers to send generated reports (PDF or Excel) as downloadable files.
 */
final class ReportResponses {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportResponses.class);

	static final MediaType XLSX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	static final MediaType XLS = MediaType.parseMediaType("application/vnd.ms-excel");

	/**
	 * Generates an Excel file into the provided temporary file.
	 */
	@FunctionalInterface
	interface ExcelGenerator {

		void generate(String exportFilename) throws OHServiceException;
	}

	private ReportResponses() {
	}

	static ResponseEntity<byte[]> pdf(JasperReportResultDto result, String downloadName) throws OHServiceException {
		byte[] content;
		try {
			content = JasperExportManager.exportReportToPdf(result.getJasperPrint());
		} catch (JRException e) {
			LOGGER.error("Jasper reporting error", e);
			throw new OHAPIException(new OHExceptionMessage("Failed to generate the report."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return file(content, MediaType.APPLICATION_PDF, downloadName + ".pdf");
	}

	static ResponseEntity<byte[]> excel(String format, String downloadName, ExcelGenerator generator) throws OHServiceException {
		String extension = excelExtension(format);
		Path exportFile = null;
		try {
			exportFile = Files.createTempFile("oh-report-", '.' + extension);
			generator.generate(exportFile.toString());
			byte[] content = Files.readAllBytes(exportFile);
			return file(content, "xls".equals(extension) ? XLS : XLSX, downloadName + '.' + extension);
		} catch (IOException e) {
			LOGGER.error("Unable to export the report to Excel", e);
			throw new OHAPIException(new OHExceptionMessage("Failed to generate the report."), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			deleteQuietly(exportFile);
		}
	}

	/**
	 * @return {@code true} if the requested format is an Excel one ({@code xlsx} or {@code xls})
	 */
	static boolean isExcel(String format) throws OHAPIException {
		String normalized = format == null ? "pdf" : format.toLowerCase(Locale.ROOT);
		return switch (normalized) {
			case "pdf" -> false;
			case "xlsx", "xls", "excel" -> true;
			default -> throw new OHAPIException(new OHExceptionMessage("Unsupported format '" + format + "': use pdf, xlsx or xls."));
		};
	}

	private static String excelExtension(String format) {
		return "xls".equalsIgnoreCase(format) ? "xls" : "xlsx";
	}

	private static ResponseEntity<byte[]> file(byte[] content, MediaType mediaType, String filename) {
		return ResponseEntity.ok()
			.contentType(mediaType)
			.header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(filename).build().toString())
			.contentLength(content.length)
			.body(content);
	}

	private static void deleteQuietly(Path file) {
		if (file == null) {
			return;
		}
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			LOGGER.warn("Unable to delete temporary file {}", file);
		}
	}
}
