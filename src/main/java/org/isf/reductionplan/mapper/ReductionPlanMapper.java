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

package org.isf.reductionplan.mapper;

import org.isf.reductionplan.dto.ReductionPlanDTO;
import org.isf.reductionplan.model.ReductionPlan;
import org.isf.shared.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ReductionPlanMapper extends GenericMapper<ReductionPlan, ReductionPlanDTO> {

	public ReductionPlanMapper() {
		super(ReductionPlan.class, ReductionPlanDTO.class);
	}

	@Override
	public ReductionPlan map2Model(ReductionPlanDTO dto) {
		ReductionPlan model = super.map2Model(dto);
		if (model != null) {
			if (model.getExamReductions() != null) {
				model.getExamReductions().forEach(item -> item.setReductionPlan(model));
			}
			if (model.getMedicalReductions() != null) {
				model.getMedicalReductions().forEach(item -> item.setReductionPlan(model));
			}
			if (model.getOperationReductions() != null) {
				model.getOperationReductions().forEach(item -> item.setReductionPlan(model));
			}
			if (model.getPriceOtherReductions() != null) {
				model.getPriceOtherReductions().forEach(item -> item.setReductionPlan(model));
			}
		}
		return model;
	}
}
