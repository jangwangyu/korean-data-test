package org.example.koreandatatest.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.koreandatatest.DTO.request.SchemaFieldRequest;
import org.example.koreandatatest.DTO.request.TableSchemaExportRequest;
import org.example.koreandatatest.DTO.request.TableSchemaRequest;
import org.example.koreandatatest.config.SecurityConfig;
import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.example.koreandatatest.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//@Disabled("#15 강의 내용에서 테스트만 다루므로 테스트를 먼저 작성함. 아직 구현이 없으므로 비활성화")
@DisplayName("[Controller] 테이블 스키마 컨트롤러 테스트")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest
public record TableSchemaControllerTest(
    @Autowired MockMvc mvc,
    @Autowired FormDataEncoder fromDataEncoder,
    @Autowired ObjectMapper mapper

) {

  @DisplayName("[GET] 테이블 스키마 페이지 -> 테이블 스키마 뷰 (정상)")
  @Test
  void givenNothing_whenRequesting_thenShowsTableSchemaView() throws Exception {
    // Given

    // When&Then
    mvc.perform(get("/table-schema"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("tableSchema"))
        .andExpect(model().attributeExists("mockDataTypes"))
        .andExpect(model().attributeExists("fileTypes"))
        .andExpect(view().name("table-schema"));
  }

  @DisplayName("[POST] 테이블 스키마 생성, 변경 (정상)")
  @Test
  void givenTableSchemaRequest_whenCreatingOrUpdating_thenRedirectsToTableSchemaView() throws Exception {
    // Given
    TableSchemaRequest request = TableSchemaRequest.of(
        "test_schema",
        "홍길동",
        List.of(
            SchemaFieldRequest.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
            SchemaFieldRequest.of("name", MockDataType.NAME, 2, 10, null, null),
            SchemaFieldRequest.of("age", MockDataType.NUMBER, 3, 20, null, null)
        )
    );
    // When&Then
    mvc.perform(
              post("/table-schema")
                  .content(fromDataEncoder.encode(request)) // 나중에 제대로 바꿔줘야 함
                  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  .with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("tableSchemaRequest", request))
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
    TableSchemaExportRequest request = TableSchemaExportRequest.of(
        "test_schema",
        77,
        ExportFileType.JSON,
        List.of(
            SchemaFieldRequest.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
            SchemaFieldRequest.of("name", MockDataType.NAME, 2, 10, "option", "well"),
            SchemaFieldRequest.of("age", MockDataType.NUMBER, 3, 20, null, null)
        )
    );
    String queryParam = fromDataEncoder.encode(request, false);
    // When&Then
    mvc.perform(get("/table-schema/export?" + queryParam))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt"))
        .andExpect(content().json(mapper.writeValueAsString(request))); // 나중에 바꿔야 함
  }
}
