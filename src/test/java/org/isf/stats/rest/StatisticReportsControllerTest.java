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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.isf.stat.dto.StatisticReportDto;
import org.isf.stat.dto.StatisticReportDto.ParameterType;
import org.isf.stat.manager.JasperReportsManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class StatisticReportsControllerTest {

	@Mock
	protected JasperReportsManager jasperReportsManagerMock;

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new StatisticReportsController(jasperReportsManagerMock))
				.setControllerAdvice(new OHResponseEntityExceptionHandler())
				.build();
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	void testGetStatisticReports_200() throws Exception {
		when(jasperReportsManagerMock.getStatisticReports("fr")).thenReturn(List.of(
				new StatisticReportDto("rpt_stat", "OH001_RegisteredPatients", "OH001 - Patients enregistrés", ParameterType.DATE_RANGE)));

		this.mockMvc
				.perform(get("/reports/statistics").param("lang", "fr"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].folder").value("rpt_stat"))
				.andExpect(jsonPath("$[0].fileName").value("OH001_RegisteredPatients"))
				.andExpect(jsonPath("$[0].parameterType").value("DATE_RANGE"));
	}

	@Test
	void testDownloadStatisticReport_unknownReport_404() throws Exception {
		when(jasperReportsManagerMock.getStatisticReport(eq("rpt_stat"), eq("unknown"), isNull())).thenReturn(null);

		this.mockMvc
				.perform(get("/reports/statistics/rpt_stat/unknown"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDownloadStatisticReport_missingDates_400() throws Exception {
		when(jasperReportsManagerMock.getStatisticReport(eq("rpt_stat"), eq("OH001_RegisteredPatients"), isNull()))
				.thenReturn(new StatisticReportDto("rpt_stat", "OH001_RegisteredPatients", "OH001 - Registered Patients", ParameterType.DATE_RANGE));

		this.mockMvc
				.perform(get("/reports/statistics/rpt_stat/OH001_RegisteredPatients"))
				.andExpect(status().isBadRequest());

		verify(jasperReportsManagerMock, never()).getGenericReportFromDateToDatePdf(any(LocalDate.class), any(LocalDate.class), any(), any());
	}

	@Test
	void testDownloadStatisticReport_invalidMonth_400() throws Exception {
		when(jasperReportsManagerMock.getStatisticReport(eq("rpt_extra"), eq("hmis105_opd_referrals"), isNull()))
				.thenReturn(new StatisticReportDto("rpt_extra", "hmis105_opd_referrals", "HMIS 105", ParameterType.MONTH_YEAR));

		this.mockMvc
				.perform(get("/reports/statistics/rpt_extra/hmis105_opd_referrals").param("month", "13").param("year", "2026"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDownloadStatisticReport_unsupportedFormat_400() throws Exception {
		when(jasperReportsManagerMock.getStatisticReport(eq("rpt_extra"), eq("hmis105_opd_referrals"), isNull()))
				.thenReturn(new StatisticReportDto("rpt_extra", "hmis105_opd_referrals", "HMIS 105", ParameterType.MONTH_YEAR));

		this.mockMvc
				.perform(get("/reports/statistics/rpt_extra/hmis105_opd_referrals").param("month", "1").param("year", "2026").param("format", "doc"))
				.andExpect(status().isBadRequest());
	}
}
