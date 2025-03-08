package org.example.koreandatatest.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.example.koreandatatest.domain.MockData;
import org.example.koreandatatest.domain.SchemaField;
import org.example.koreandatatest.domain.TableSchema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("[Repository]Jpa 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class JpaRepositoryTest {

  @Autowired private MockDataRepository mockDataRepository;
  @Autowired private SchemaFieldRepository schemaFieldRepository;
  @Autowired private TableSchemaRepository tableSchemaRepository;

  @Autowired private TestEntityManager entityManager;

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

}
