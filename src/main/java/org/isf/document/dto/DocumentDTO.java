package org.isf.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DocumentDTO {

	@Schema(description = "Unique document ID", example = "123e4567-e89b-12d3-a456-426614174000")
	private String documentId;

	@Schema(description = "Full file name in storage", example = "20240610_153000_RX_P000123.jpeg")
	private String fileName;

	@Schema(description = "Unique person/client ID", example = "P000123")
	private String personId;

	@Schema(description = "Document type", example = "RX", allowableValues = {"DOC", "MED", "REF"})
	private String type;

	@Schema(description = "Document creation date and time", example = "2024-06-10T15:30:00")
	private String creationDate;

	@Schema(description = "Clinical date of the document", example = "2024-06-10")
	private String documentDate;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
}
