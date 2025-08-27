package org.isf.conditioning.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ConditioningDTO {

	@NotNull
	@Schema(description = "Encounter ID", example = "101")
	public Long encounterId;

	@Schema(description = "If aspiration was done", example = "true")
	public Boolean aspiration;

	@Schema(description = "Duration of MCE in minutes", example = "15")
	public Integer mceDuree;

	@Schema(description = "Duration of ventilation in minutes", example = "30")
	public Integer ventilationDuree;

	@Schema(description = "Oxygen flow rate", example = "2.5")
	public Double oxygeneDebit;

	@Schema(description = "SG volume", example = "50.0")
	public Double sgVolume;

	@Schema(description = "Diazepam dose", example = "5.0")
	public Double diazepamDose;

	@Schema(description = "Bolus SS volume", example = "10.0")
	public Double bolusSsVolume;

	@Schema(description = "SNG number", example = "SNG123")
	public String sngNumero;

	@Schema(description = "Other notes", example = "No complications")
	public String others;

	@Schema(description = "ID of the user who performed the procedure", example = "12")
	public Long performById;

	@Schema(description = "Date and time of the procedure", type = "string", example = "2025-08-27T14:30:00")
	public LocalDateTime performAt;
}
