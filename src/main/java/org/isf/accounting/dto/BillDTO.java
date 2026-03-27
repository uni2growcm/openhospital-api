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
package org.isf.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.isf.patient.dto.PatientDTO;

import java.time.LocalDateTime;

@Schema(description = "Class representing a bill")
public class BillDTO {

	private Integer id;

	private PatientDTO patient;

	private Integer listId;

	@NotNull
	@Schema(description = "Date of bill creation", example = "2020-03-19T14:58:00.000Z", type = "string")
	private LocalDateTime date;

	@NotNull
	@Schema(description = "Date of bill updated", example = "2020-03-19T14:58:00.000Z", type = "string")
	private LocalDateTime update;

	@NotNull
	@Schema(description = "boolean which tells if a price list is applied", example = "true")
	private boolean isList;

	@NotNull
	@Schema(description = "Price list name", example = "Basic", maxLength = 50)
	private String listName;

	@NotNull
	@Schema(description = "Is bill belongs to a patient?", example = "true")
	private boolean patientTrue;

	@NotNull
	@Schema(description = "Patient name", example = "Mario Rossi", maxLength = 100)
	private String patName;

	@NotNull
	@Schema(description = "Bill status", example = "O")
	private String status;

	@NotNull
	@Schema(description = "Bill Amount", example = "1000")
	private Double amount;

	@NotNull
	@Schema(description = "Bill balance", example = "1500")
	private Double balance;

	@NotNull
	@Schema(description = "user name who create the bill", example = "admin")
	private String user;

	@Schema(description = "Lock", example = "0")
	private int lock;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PatientDTO getPatient() {
		return this.patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public Integer getListId() {
		return this.listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getUpdate() {
		return this.update;
	}

	public void setUpdate(LocalDateTime update) {
		this.update = update;
	}

	public boolean isList() {
		return this.isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public String getListName() {
		return this.listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public boolean isPatientTrue() {
		return this.patientTrue;
	}

	public void setPatientTrue(boolean isPatientBill) {
		this.patientTrue = isPatientBill;
	}

	public String getPatName() {
		return this.patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}
}
