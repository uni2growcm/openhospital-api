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
package org.isf.typology.data;

import org.isf.typology.TestTypology;
import org.isf.typology.dto.TypologyDTO;
import org.isf.typology.model.Family;
import org.isf.typology.model.Typology;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TypologyHelper {

	public static Typology setup(int i) {
		TestTypology testTypology = new TestTypology();
		Typology typology = testTypology.setupVisitType();
		typology.setCode(typology.getCode() + i);
		return typology;
	}

	public static List<Typology> setupTypologyList(int size) {
		return IntStream.range(0, size)
			.mapToObj(TypologyHelper::setup)
			.collect(Collectors.toList());
	}

	public static TypologyDTO setupDTO(int i) {
		return new TypologyDTO("VT" + i, "Visit Type " + i, Family.VISITTYPE);
	}
}
