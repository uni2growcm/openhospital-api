package org.isf.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class DocumentListResponseDTO {

	@Schema(description = "Total number of documents matching the filter.")
	private int totalItems;

	@Schema(description = "Current page size.")
	private int pageSize;

	@Schema(description = "Current page number.")
	private int pageNumber;

	@Schema(description = "List of documents in the current page.")
	private List<DocumentDTO> documents;

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}
}
