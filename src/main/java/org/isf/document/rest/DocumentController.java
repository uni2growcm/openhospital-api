package org.isf.document.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.document.dto.DocumentDTO;
import org.isf.document.dto.DocumentListResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@Tag(name = "Documents")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

	@GetMapping(value = "/documents")
	public ResponseEntity<DocumentListResponseDTO> listDocuments(
		@RequestParam String personId,
		@RequestParam String type,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(required = false) LocalDate fromDate,
		@RequestParam(required = false) LocalDate toDate) {
		return ResponseEntity.ok(new DocumentListResponseDTO());
	}

	@PostMapping(value = "/documents")
	public ResponseEntity<DocumentDTO> uploadDocument(
		@RequestParam String personId,
		@RequestParam String type,
		@RequestParam LocalDate documentDate,
		@RequestPart("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new DocumentDTO());
	}

	@GetMapping(value = "/documents/{documentId}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable String documentId) {
		return ResponseEntity.ok()
							 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.jpg\"")
							 .contentType(MediaType.IMAGE_JPEG)
							 .body(null);
	}
}
