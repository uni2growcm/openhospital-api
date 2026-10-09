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
package org.isf.reductionplan.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.isf.reductionplan.dto.ReductionPlanDTO;
import org.isf.reductionplan.manager.ReductionPlanManager;
import org.isf.reductionplan.mapper.ReductionPlanMapper;
import org.isf.utils.exception.OHServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "ReductionPlans")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ReductionPlanController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReductionPlanController.class);

	private final ReductionPlanManager reductionPlanManager;

	private final ReductionPlanMapper mapper;

	public ReductionPlanController(ReductionPlanManager reductionPlanManager, ReductionPlanMapper reductionPlanMapper) {
		this.reductionPlanManager = reductionPlanManager;
		this.mapper = reductionPlanMapper;
	}

	/**
	 * Get all the reduction plans stored
	 *
	 * @return the list of reduction plans found
	 * @throws OHServiceException When failed to get reduction plans
	 */
	@GetMapping(value = "/reductionplans")
	public List<ReductionPlanDTO> getReductionPlans() throws OHServiceException {
		LOGGER.info("Get reduction plans");

		return mapper.map2DTOList(reductionPlanManager.getAll());
	}
}
