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
package org.isf.staff.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.isf.homevisit.manager.StaffBrowserManager;
import org.isf.homevisit.model.Staff;
import org.isf.shared.exceptions.OHAPIException;
import org.isf.shared.pagination.Page;
import org.isf.staff.dto.StaffDTO;
import org.isf.staff.mapper.StaffMapper;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.pagination.PagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Staff")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StaffController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffController.class);

	protected static final String DEFAULT_PAGE_SIZE = "10";

	private final StaffBrowserManager staffManager;

	private final StaffMapper mapper;

	public StaffController(StaffBrowserManager staffManager, StaffMapper staffMapper) {
		this.staffManager = staffManager;
		this.mapper = staffMapper;
	}

	/**
	 * Get all the {@link Staff} members.
	 *
	 * @return NO_CONTENT if there aren't staff members, {@code List<StaffDTO>} otherwise
	 * @throws OHServiceException When failed to get staff members
	 */
	@GetMapping(value = "/staff")
	public List<StaffDTO> getStaff() throws OHServiceException {
		LOGGER.info("Get staff");

		return mapper.map2DTOList(staffManager.getStaff());
	}

	/**
	 * Get a {@link Staff} member by code.
	 *
	 * @param code Staff code
	 * @return the staff member
	 * @throws OHServiceException When failed to get the staff member
	 */
	@GetMapping(value = "/staff/{code}")
	public StaffDTO getStaff(@PathVariable Integer code) throws OHServiceException {
		LOGGER.info("Get staff with code: {}", code);

		try {
			return mapper.map2DTO(staffManager.getStaff(code));
		} catch (EntityNotFoundException e) {
			throw new OHAPIException(new OHExceptionMessage("Staff not found."), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get {@link Staff} members page by page, optionally filtered by keyword (name, profession, position, phone).
	 *
	 * @param page Zero-based page index
	 * @param size Page size
	 * @param keyword Optional search keyword
	 * @return the requested page of staff members
	 */
	@GetMapping(value = "/staff/paged")
	public Page<StaffDTO> getStaffPaged(
		@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		@RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
		@RequestParam(value = "keyword", required = false) String keyword
	) throws OHServiceException {
		LOGGER.info("Get staff page: {} size: {} keyword: {}", page, size, keyword);
		PagedResponse<Staff> staffPaged = staffManager.getStaff(keyword, page, size);

		Page<StaffDTO> result = new Page<>();
		result.setData(mapper.map2DTOList(staffPaged.getData()));
		result.setPageInfo(mapper.setParameterPageInfo(staffPaged.getPageInfo()));
		return result;
	}

	/**
	 * Search {@link Staff} members by keyword (name, profession, position, phone).
	 *
	 * @param keyword search keyword
	 * @return {@code List<StaffDTO>} matching the keyword
	 * @throws OHServiceException When failed to search staff members
	 */
	@GetMapping(value = "/staff/search")
	public List<StaffDTO> searchStaff(@RequestParam(required = false) String keyword) throws OHServiceException {
		LOGGER.info("Search staff with keyword: {}", keyword);

		return mapper.map2DTOList(staffManager.searchStaff(keyword));
	}

	/**
	 * Create a new {@link Staff} member.
	 *
	 * @param newStaff Staff payload
	 * @return an error message if there are some problem, the created staff member otherwise
	 * @throws OHServiceException When failed to create the staff member
	 */
	@PostMapping(value = "/staff")
	@ResponseStatus(HttpStatus.CREATED)
	public StaffDTO newStaff(@RequestBody StaffDTO newStaff) throws OHServiceException {
		LOGGER.info("Create Staff: {}", newStaff);
		Staff staffCreated = staffManager.saveStaff(mapper.map2Model(newStaff));
		if (staffCreated == null) {
			throw new OHAPIException(new OHExceptionMessage("Staff not created."));
		}

		return mapper.map2DTO(staffCreated);
	}

	/**
	 * Update a specified {@link Staff} member.
	 *
	 * @param updateStaff Staff payload
	 * @return an error message if there are some problem, the updated staff member otherwise
	 * @throws OHServiceException When failed to update the staff member
	 */
	@PutMapping(value = "/staff")
	public StaffDTO updateStaff(@RequestBody StaffDTO updateStaff) throws OHServiceException {
		LOGGER.info("Update staff with code: {}", updateStaff.getCode());
		Staff staff = mapper.map2Model(updateStaff);
		staff.setLock(updateStaff.getLock());
		Staff staffUpdated = staffManager.saveStaff(staff);
		if (staffUpdated == null) {
			throw new OHAPIException(new OHExceptionMessage("Staff not updated."));
		}

		return mapper.map2DTO(staffUpdated);
	}

	/**
	 * Delete a {@link Staff} member.
	 *
	 * @param code the staff member to delete
	 * @return an error message if there are some problem, ok otherwise
	 * @throws OHServiceException When failed to delete the staff member
	 */
	@DeleteMapping(value = "/staff/{code}")
	public boolean deleteStaff(@PathVariable Integer code) throws OHServiceException {
		LOGGER.info("Delete Staff with code: {}", code);
		try {
			staffManager.getStaff(code);
		} catch (EntityNotFoundException e) {
			throw new OHAPIException(new OHExceptionMessage("Staff not found."), HttpStatus.NOT_FOUND);
		}

		staffManager.deleteStaff(code);
		return true;
	}
}
