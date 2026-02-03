package org.isf.medicalstockward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.isf.medical.dto.MedicalDTO;
import org.isf.ward.dto.WardDTO;

public class MedicalWardQuantityDTO {

	@NotNull
	@Schema(description = "The ward")
	private WardDTO ward;

	@NotNull
	@Schema(description = "The medical")
	private MedicalDTO medical;

	@NotNull
	@Schema(description = "The medical quantity in the ward")
	private int quantity;

	public WardDTO getWard() {
		return ward;
	}

	public void setWard(WardDTO ward) {
		this.ward = ward;
	}

	public MedicalDTO getMedical() {
		return medical;
	}

	public void setMedical(MedicalDTO medical) {
		this.medical = medical;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}