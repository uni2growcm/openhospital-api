/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.accounting.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.isf.accounting.dto.BillPaymentsDTO;
import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillPayments;
import org.isf.patient.model.Patient;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentsMapper extends GenericMapper<BillPayments, BillPaymentsDTO> {
	public BillPaymentsMapper() {
		super(BillPayments.class, BillPaymentsDTO.class);
	}

	@Override
	public BillPaymentsDTO map2DTO(BillPayments fromObj) {
		BillPaymentsDTO dto = super.map2DTO(fromObj);
		Bill bill = fromObj.getBill();
		if (bill != null) {
			dto.setPatName(bill.getPatName());
			Patient patient = bill.getBillPatient();
			dto.setPatientCode(patient != null ? patient.getCode() : null);
		}
		return dto;
	}

	@Override
	public List<BillPaymentsDTO> map2DTOList(List<BillPayments> list) {
		return list.stream().map(this::map2DTO).collect(Collectors.toList());
	}
}