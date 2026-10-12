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
package org.isf.staff.manager;

import org.isf.homevisit.model.Staff;
import org.isf.staff.repository.StaffPagedRepository;
import org.isf.utils.pagination.PageInfo;
import org.isf.utils.pagination.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class StaffPagedManager {

	private final StaffPagedRepository repository;

	public StaffPagedManager(StaffPagedRepository repository) {
		this.repository = repository;
	}

	public PagedResponse<Staff> getStaffPageable(int page, int size, String keyword) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Staff> result = (keyword == null || keyword.isBlank())
						? repository.findByActive(1, pageable)
						: repository.searchActive(keyword, pageable);

		PagedResponse<Staff> response = new PagedResponse<>();
		response.setData(result.getContent());
		response.setPageInfo(PageInfo.from(result));
		return response;
	}
}
