package org.example.koreandatatest.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.example.koreandatatest.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@Disabled("#15 강의 내용에서 테스트만 다루므로 테스트를 먼저 작성함. 아직 구현이 없으므로 비활성화")
@DisplayName("[Controller] 테이블 스키마 컨트롤러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest
public record TableSchemaControllerTest(@Autowired MockMvc mvc) {

  @DisplayName("[GET] 테이블 스키마 페이지 -> 테이블 스키마 뷰 (정상)")
  @Test
  void givenNothing_whenRequesting_thenShowsTableSchemaView() throws Exception {
    // Given

    // When&Then
    mvc.perform(get("/table-schema"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("table-schema"));
  }

  @DisplayName("[POST] 테이블 스키마 생성, 변경 (정상)")
  @Test
  void givenTableSchemaRequest_whenCreatingOrUpdating_thenRedirectsToTableSchemaView() throws Exception {
    // Given

    // When&Then
    mvc.perform(
              post("/table-schema")
                  .content("sample data") // 나중에 제대로 바꿔줘야 함
                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  .with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/table-schema"));
  }

  @DisplayName("[GET] 내 스키마 목록 페이지 -> 내 스키마 목록 뷰 (정상)")
  @Test
  void givenAuthenticated_whenRequesting_thenShowsMySchemaView() throws Exception {
    // Given

    // When&Then
    mvc.perform(get("/table-schema/my-schemas"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("my-schemas"));
  }

  @DisplayName("[POST] 내 스키마 삭제 (정상)")
  @Test
  void givenAuthenticatedUserAndSchemaName_whenDeleting_thenRedirectsToTableSchemaView() throws Exception {
    // Given
    String schemaName = "test_schema";
    // When&Then
    mvc.perform(
              post("/table-schema/my-schemas/{schemaName}", schemaName)
                  .with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/my-schemas"));
  }

  @DisplayName("[GET] 테이블 스키마 파일 다운로드 -> 테이블 스키마 파일 (정상)")
  @Test
  void givenTableSchema_whenDownloading_thenReturnsFile() throws Exception {
    // Given

    // When&Then
    mvc.perform(get("/table-schema/export"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(header().string("Content-Disposition", "attachment; filename=table-schema.sql"))
        .andExpect(content().string("download complete!")); // 나중에 바꿔야 함
  }
}
