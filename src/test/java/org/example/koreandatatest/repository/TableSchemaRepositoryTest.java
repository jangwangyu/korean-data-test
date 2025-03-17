package org.example.koreandatatest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.example.koreandatatest.domain.TableSchema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("[Repository] 테이블 스키마 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TableSchemaRepositoryTest {

  @Autowired private TableSchemaRepository sut;

  @DisplayName("사용자별 테이블 스키마 조회하면, 페이징된 테이블 스키마를 반환한다")
  @Test
  void givenUserId_whenSelectingTableSchemas_thenReturnsPagedTableSchema() {
    // Given
    var userId = "jang";
    // When
    Page<TableSchema> result = sut.findByUserId(userId, Pageable.ofSize(5));

    // Then
    assertThat(result.getContent())
        .hasSize(1)
        .extracting("userId", String.class)
        .containsOnly(userId);
    assertThat(result.getPageable())
        .hasFieldOrPropertyWithValue("pageSize", 5)
        .hasFieldOrPropertyWithValue("pageNumber", 0);
  }

  @DisplayName("사용자의 테이블 스키마 이름을 조회하면, 테이블 스키마를 반환한다")
  @Test
  void givenUserIdAndSchemaName_whenSelectingTableSchemas_thenReturnsTableSchema() {
    // Given
    var userId = "jang";
    var schemaName = "test_schema";

    // When
    Optional<TableSchema> result = sut.findByUserIdAndSchemaName(userId, schemaName);

    // Then
    assertThat(result)
        .get()
        .hasFieldOrPropertyWithValue("userId", userId)
        .hasFieldOrPropertyWithValue("schemaName", schemaName);
  }

  @DisplayName("사용자의 테이블 스키마 이름이 주어지면, 테이블 스키마를 삭제한다")
  @Test
  void givenUserIdAndSchemaName_whenDeletingTableSchemas_thenDeletes() {
    // Given
    var userId = "jang";
    var schemaName = "test_schema";

    // When
    sut.deleteByUserIdAndSchemaName(userId,schemaName);

    // Then
    assertThat(sut.findByUserIdAndSchemaName(userId,schemaName).isEmpty());

  }
}