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

package org.isf.articlefamily.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.isf.articlefamily.dto.ArticleFamilyDTO;
import org.isf.articlefamily.manager.ArticleFamilyBrowserManager;
import org.isf.articlefamily.mapper.ArticleFamilyMapper;
import org.isf.articlefamily.model.ArticleFamily;
import org.isf.shared.exceptions.OHResponseEntityExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleFamilyControllerTest {

	@Mock
	private ArticleFamilyBrowserManager articleFamilyBrowserManagerMock;

	private final ArticleFamilyMapper articleFamilyMapper = new ArticleFamilyMapper();

	private MockMvc mockMvc;

	private AutoCloseable closeable;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		ModelMapper modelMapper = new ModelMapper();
		ReflectionTestUtils.setField(articleFamilyMapper, "modelMapper", modelMapper);

		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new ArticleFamilyController(articleFamilyBrowserManagerMock, articleFamilyMapper))
			.setControllerAdvice(new OHResponseEntityExceptionHandler())
			.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	private ArticleFamily createArticleFamily(int id, String code, String description) {
		ArticleFamily af = new ArticleFamily(id, code, description, null);
		af.setLock(0);
		return af;
	}

	@Test
	void testGetAllArticleFamilies_200() throws Exception {
		List<ArticleFamily> families = List.of(
			createArticleFamily(1, "MED", "Medicines"),
			createArticleFamily(2, "SUR", "Surgical")
		);

		when(articleFamilyBrowserManagerMock.getArticleFamilies()).thenReturn(families);

		mockMvc.perform(get("/article-families"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].code", is("MED")))
			.andExpect(jsonPath("$[0].description", is("Medicines")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].code", is("SUR")))
			.andExpect(jsonPath("$[1].description", is("Surgical")));

		verify(articleFamilyBrowserManagerMock, times(1)).getArticleFamilies();
	}

	@Test
	void testSearchArticleFamilies_200() throws Exception {
		List<ArticleFamily> families = List.of(createArticleFamily(1, "MED", "Medicines"));

		when(articleFamilyBrowserManagerMock.searchArticleFamilies("med")).thenReturn(families);

		mockMvc.perform(get("/article-families/search").param("q", "med"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].code", is("MED")));

		verify(articleFamilyBrowserManagerMock, times(1)).searchArticleFamilies("med");
	}

	@Test
	void testGetArticleFamilyById_Success_200() throws Exception {
		ArticleFamily af = createArticleFamily(1, "MED", "Medicines");

		when(articleFamilyBrowserManagerMock.getArticleFamily(1)).thenReturn(af);

		mockMvc.perform(get("/article-families/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.code", is("MED")))
			.andExpect(jsonPath("$.description", is("Medicines")));

		verify(articleFamilyBrowserManagerMock, times(1)).getArticleFamily(1);
	}

	@Test
	void testGetArticleFamilyById_NotFound_404() throws Exception {
		when(articleFamilyBrowserManagerMock.getArticleFamily(999))
			.thenThrow(new EntityNotFoundException("angal.articlefamily.notfound.msg"));

		mockMvc.perform(get("/article-families/999"))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.articlefamily.not_found")));

		verify(articleFamilyBrowserManagerMock, times(1)).getArticleFamily(999);
	}

	@Test
	void testCreateArticleFamily_Success_201() throws Exception {
		ArticleFamilyDTO dto = new ArticleFamilyDTO(null, "MED", "Medicines", 0);
		ArticleFamily saved = createArticleFamily(1, "MED", "Medicines");

		when(articleFamilyBrowserManagerMock.getArchivedArticleFamilyByCode("MED")).thenReturn(java.util.Optional.empty());
		when(articleFamilyBrowserManagerMock.isCodeUnique(eq("MED"), isNull())).thenReturn(true);
		when(articleFamilyBrowserManagerMock.saveOrReactivateArticleFamily(any(ArticleFamily.class))).thenReturn(saved);

		mockMvc.perform(post("/article-families")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.code", is("MED")))
			.andExpect(jsonPath("$.description", is("Medicines")));

		verify(articleFamilyBrowserManagerMock, times(1)).saveOrReactivateArticleFamily(any(ArticleFamily.class));
	}

	@Test
	void testCreateArticleFamily_ReactivateArchived_201() throws Exception {
		ArticleFamilyDTO dto = new ArticleFamilyDTO(null, "MED", "Medicines", 0);
		ArticleFamily archived = createArticleFamily(5, "MED", "Old Medicines");
		ArticleFamily reactivated = createArticleFamily(5, "MED", "Medicines");

		when(articleFamilyBrowserManagerMock.getArchivedArticleFamilyByCode("MED")).thenReturn(java.util.Optional.of(archived));
		when(articleFamilyBrowserManagerMock.isCodeUnique(eq("MED"), eq(5))).thenReturn(true);
		when(articleFamilyBrowserManagerMock.saveOrReactivateArticleFamily(any(ArticleFamily.class))).thenReturn(reactivated);

		mockMvc.perform(post("/article-families")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(5)))
			.andExpect(jsonPath("$.code", is("MED")))
			.andExpect(jsonPath("$.description", is("Medicines")));

		verify(articleFamilyBrowserManagerMock, times(1)).saveOrReactivateArticleFamily(any(ArticleFamily.class));
	}

	@Test
	void testCreateArticleFamily_DuplicateCode_409() throws Exception {
		ArticleFamilyDTO dto = new ArticleFamilyDTO(null, "MED", "Medicines", 0);

		when(articleFamilyBrowserManagerMock.getArchivedArticleFamilyByCode("MED")).thenReturn(java.util.Optional.empty());
		when(articleFamilyBrowserManagerMock.isCodeUnique(eq("MED"), isNull())).thenReturn(false);

		mockMvc.perform(post("/article-families")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isConflict())
			.andExpect(content().string(containsString("errors.articlefamily.code_exists")));

		verify(articleFamilyBrowserManagerMock, never()).saveOrReactivateArticleFamily(any());
	}

	@Test
	void testUpdateArticleFamily_Success_200() throws Exception {
		ArticleFamily existing = createArticleFamily(1, "MED", "Medicines");
		ArticleFamilyDTO updateDto = new ArticleFamilyDTO(1, "MED", "Medicines Updated", 0);
		ArticleFamily updated = createArticleFamily(1, "MED", "Medicines Updated");

		when(articleFamilyBrowserManagerMock.getArticleFamily(1)).thenReturn(existing);
		when(articleFamilyBrowserManagerMock.isCodeUnique(eq("MED"), eq(1))).thenReturn(true);
		when(articleFamilyBrowserManagerMock.updateArticleFamily(any(ArticleFamily.class))).thenReturn(updated);

		mockMvc.perform(put("/article-families/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.description", is("Medicines Updated")));

		verify(articleFamilyBrowserManagerMock, times(1)).updateArticleFamily(any(ArticleFamily.class));
	}

	@Test
	void testUpdateArticleFamily_NotFound_404() throws Exception {
		ArticleFamilyDTO updateDto = new ArticleFamilyDTO(999, "MED", "Medicines", 0);
		when(articleFamilyBrowserManagerMock.getArticleFamily(999))
			.thenThrow(new EntityNotFoundException("angal.articlefamily.notfound.msg"));

		mockMvc.perform(put("/article-families/999")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.articlefamily.not_found")));

		verify(articleFamilyBrowserManagerMock, never()).updateArticleFamily(any());
	}

	@Test
	void testDeleteArticleFamily_Success_204() throws Exception {
		ArticleFamily existing = createArticleFamily(1, "MED", "Medicines");
		when(articleFamilyBrowserManagerMock.getArticleFamily(1)).thenReturn(existing);
		doNothing().when(articleFamilyBrowserManagerMock).deleteArticleFamily(1);

		mockMvc.perform(delete("/article-families/1"))
			.andExpect(status().isNoContent());

		verify(articleFamilyBrowserManagerMock, times(1)).deleteArticleFamily(1);
	}

	@Test
	void testDeleteArticleFamily_NotFound_404() throws Exception {
		when(articleFamilyBrowserManagerMock.getArticleFamily(999))
			.thenThrow(new EntityNotFoundException("angal.articlefamily.notfound.msg"));

		mockMvc.perform(delete("/article-families/999"))
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("errors.articlefamily.not_found")));

		verify(articleFamilyBrowserManagerMock, never()).deleteArticleFamily(999);
	}
}
