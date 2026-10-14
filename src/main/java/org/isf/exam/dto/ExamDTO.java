/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
package org.isf.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.isf.articlefamily.dto.ArticleFamilyDTO;
import org.isf.exatype.dto.ExamTypeDTO;

public class ExamDTO {

	@Schema(description = "Exam Code", example = "01.01", maxLength = 10)
	private String code;

	@Schema(description = "Exam Description", example = "1.1 HB", maxLength = 100)
	private String description;

	@Schema(description = "Exam Procedure", example = "1")
	private Integer procedure;

	@Schema(description = "Exam Default Result", example = ">=12 (NORMAL)", maxLength = 50)
	private String defaultResult;

	@Schema(description = "Exam Type")
	private ExamTypeDTO examtype;

	@Schema(description = "Article Family")
	private ArticleFamilyDTO articleFamily;

	@Schema(description = "Lock", example = "0")
	private Integer lock;

	public ExamDTO() {
	}

	public ExamDTO(String code, String description, Integer procedure, String defaultResult, ExamTypeDTO examtype) {
		this.code = code;
		this.description = description;
		this.procedure = procedure;
		this.defaultResult = defaultResult;
		this.examtype = examtype;
	}

	public ExamDTO(String code, String description, Integer procedure, String defaultResult, ExamTypeDTO examtype, ArticleFamilyDTO articleFamily) {
		this.code = code;
		this.description = description;
		this.procedure = procedure;
		this.defaultResult = defaultResult;
		this.examtype = examtype;
		this.articleFamily = articleFamily;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProcedure() {
		return this.procedure;
	}

	public void setProcedure(Integer procedure) {
		this.procedure = procedure;
	}

	public String getDefaultResult() {
		return this.defaultResult;
	}

	public void setDefaultResult(String defaultResult) {
		this.defaultResult = defaultResult;
	}

	public ExamTypeDTO getExamtype() {
		return this.examtype;
	}

	public void setExamtype(ExamTypeDTO examtype) {
		this.examtype = examtype;
	}

	public ArticleFamilyDTO getArticleFamily() {
		return this.articleFamily;
	}

	public void setArticleFamily(ArticleFamilyDTO articleFamily) {
		this.articleFamily = articleFamily;
	}

	public Integer getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	@Override
	public String toString() {
		return "ExamDTO{" + "code='" + code + '\'' + ", description='" + description + '\'' + ", procedure=" + procedure
			+ ", defaultResult='" + defaultResult + '\'' + ", examtype=" + examtype + ", articleFamily=" + articleFamily + '}';
	}

}