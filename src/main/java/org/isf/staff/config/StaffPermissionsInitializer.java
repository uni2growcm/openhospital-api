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
package org.isf.staff.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.isf.menu.manager.UserGroupManager;
import org.isf.menu.model.UserGroup;
import org.isf.permissions.manager.GroupPermissionManager;
import org.isf.permissions.manager.PermissionManager;
import org.isf.permissions.model.Permission;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The "staff" authorities checked by {@code SecurityConfig} (staff.create/read/update/delete) are not part of
 * the core database seed yet (OH_PERMISSIONS / OH_GROUPPERMISSION), so no group - including admin - can access
 * the /staff endpoints. Until the core seed is updated, create the missing permissions and grant them to the
 * admin group (plus staff.read to guest, mirroring the read-only convention used by the other modules).
 */
@Component
public class StaffPermissionsInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffPermissionsInitializer.class);

	private static final String ADMIN_GROUP_CODE = "admin";
	private static final String GUEST_GROUP_CODE = "guest";
	private static final String[] STAFF_PERMISSIONS = { "staff.create", "staff.read", "staff.update", "staff.delete" };

	private final PermissionManager permissionManager;
	private final GroupPermissionManager groupPermissionManager;
	private final UserGroupManager userGroupManager;

	public StaffPermissionsInitializer(
					PermissionManager permissionManager,
					GroupPermissionManager groupPermissionManager,
					UserGroupManager userGroupManager
	) {
		this.permissionManager = permissionManager;
		this.groupPermissionManager = groupPermissionManager;
		this.userGroupManager = userGroupManager;
	}

	@PostConstruct
	public void ensureStaffPermissions() {
		try {
			for (String name : STAFF_PERMISSIONS) {
				Permission permission = permissionManager.retrievePermissionByName(name);
				if (permission == null) {
					permission = new Permission();
					permission.setName(name);
					permission.setDescription("");
					permission = permissionManager.save(permission);
					LOGGER.info("Created missing permission '{}'.", name);
				}

				grantToGroup(ADMIN_GROUP_CODE, permission);
				if ("staff.read".equals(name)) {
					grantToGroup(GUEST_GROUP_CODE, permission);
				}
			}
		} catch (OHServiceException e) {
			LOGGER.error("Unable to initialize staff permissions.", e);
		}
	}

	private void grantToGroup(String groupCode, Permission permission) {
		List<UserGroup> groups = userGroupManager.findByIdIn(List.of(groupCode));
		if (groups.isEmpty()) {
			return;
		}

		try {
			groupPermissionManager.create(groups.get(0), permission);
			LOGGER.info("Granted permission '{}' to group '{}'.", permission.getName(), groupCode);
		} catch (OHDataValidationException e) {
			// the group already has this permission, nothing to do
		}
	}
}
