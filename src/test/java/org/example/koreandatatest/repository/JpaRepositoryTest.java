package org.example.koreandatatest.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.example.koreandatatest.domain.MockData;
import org.example.koreandatatest.domain.SchemaField;
import org.example.koreandatatest.domain.TableSchema;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("[Repository]Jpa 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class JpaRepositoryTest {

  private static final String TEST_AUDITOR = "test_user";

  @Autowired private MockDataRepository mockDataRepository;
  @Autowired private SchemaFieldRepository schemaFieldRepository;
  @Autowired private TableSchemaRepository tableSchemaRepository;

  @Autowired private TestEntityManager entityManager;
  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void selectTest() {
    // Given

    // When
    List<MockData> mockDataList = mockDataRepository.findAll();
    List<SchemaField> schemaFields= schemaFieldRepository.findAll();
    List<TableSchema> tableSchemas = tableSchemaRepository.findAll();
    // Then
    assertThat(mockDataList).hasSize(100); // mock data 100개
    assertThat(schemaFields)
        .hasSize(4) // schema field 4개
        .first()
        .extracting("tableSchema")
        .isEqualTo(tableSchemas.getFirst()); // 첫번째 schema field의 table schema는 첫번째 table schema와 일치해야함
    assertThat(tableSchemas)
        .hasSize(1) // table schema 1개
        .first() // 첫번째 table schema를 object로 변환
        .hasFieldOrPropertyWithValue("schemaName", "test_schema")
        .hasFieldOrPropertyWithValue("userId", "djkeh") // object의 필드를 검사
        .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION) // 한번 더 추출해서 schemaFields의 정보를 컬렉션으로 불러옴
        .hasSize(4);
  }

  @Test
  void insertTest() {
    // Given
    TableSchema tableSchema = TableSchema.of("test_schema", "user");
    tableSchema.addSchemaFields(List.of(
        SchemaField.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
        SchemaField.of("name", MockDataType.NAME, 2, 10, null, null),
        SchemaField.of("age", MockDataType.NUMBER, 3, 20, null, null)
    ));
    // When
    TableSchema saved = tableSchemaRepository.save(tableSchema);
    // Then
    entityManager.clear();
    TableSchema newTableSchema = tableSchemaRepository.findById(saved.getId()).orElseThrow();
    assertThat(newTableSchema)
        .hasFieldOrPropertyWithValue("schemaName", "test_schema")
        .hasFieldOrPropertyWithValue("userId", "user")
        .hasFieldOrPropertyWithValue("createdBy", TEST_AUDITOR)
        .hasFieldOrPropertyWithValue("modifiedBy", TEST_AUDITOR)
        .hasFieldOrProperty("createdAt")
        .hasFieldOrProperty("modifiedAt")
        .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
        .hasSize(3)
        .extracting("fieldOrder", Integer.class)
        .containsExactly(1, 2, 3);
    assertThat(newTableSchema.getCreatedAt()).isEqualTo(newTableSchema.getModifiedAt()); // 새로 만들때 생성일과 수정일이 같아야함
  }

  @Test
  void updateTest() {
    // Given
    TableSchema tableSchema = tableSchemaRepository.findAll().getFirst();
    tableSchema.setSchemaName("test_modified");
    tableSchema.clearSchemaFields();
    tableSchema.addSchemaField(
        SchemaField.of("age", MockDataType.NUMBER, 3, 20, json(Map.of("min", 1, "max", 10)), null)
    );
    // When
    TableSchema updated = tableSchemaRepository.saveAndFlush(tableSchema);

    // Then
    assertThat(updated)
        .hasFieldOrPropertyWithValue("schemaName", "test_modified")
        .hasFieldOrPropertyWithValue("createdBy", "asd")
        .hasFieldOrPropertyWithValue("modifiedBy", TEST_AUDITOR)
        .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
        .hasSize(1);
    assertThat(updated.getCreatedAt()).isBefore(updated.getModifiedAt());
  }

  @Test
  void deleteTest() {
    // Given
    TableSchema tableSchema = tableSchemaRepository.findAll().getFirst();
    // When
    tableSchemaRepository.delete(tableSchema);
    // Then
    assertThat(tableSchemaRepository.count()).isEqualTo(0);
    assertThat(schemaFieldRepository.count()).isEqualTo(0);
  }

  @Test
  void insertUKConstraintTest() {
    // Given
    MockData mockData1 = MockData.of(MockDataType.CAR, "BMW");
    MockData mockData2 = MockData.of(MockDataType.CAR, "BMW");
    // When
    Throwable t = catchThrowable(() -> mockDataRepository.saveAll(List.of(mockData1, mockData2)));
    // Then
    assertThat(t)
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class)
        .hasRootCauseInstanceOf(SQLIntegrityConstraintViolationException.class)
        .rootCause()
        .message()
        .contains("Unique index or primary key violation"); // H2 매시지
  }

  private String json(Object obj) {
    try{
      return mapper.writeValueAsString(obj);
    }catch (JsonProcessingException e){
      throw new RuntimeException("JSON 반환 테스트 중 오류 발생", e);
    }
  }

  @EnableJpaAuditing
  @TestConfiguration
  static class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of(TEST_AUDITOR);
    }
  }
}
