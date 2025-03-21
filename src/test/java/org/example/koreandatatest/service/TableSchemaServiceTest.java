package org.example.koreandatatest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.koreandatatest.DTO.TableSchemaDto;
import org.example.koreandatatest.domain.TableSchema;
import org.example.koreandatatest.repository.TableSchemaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("[Service] 테이블 스키마 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableSchemaServiceTest {

  @InjectMocks
  private TableSchemaService sut;

  @Mock
  private TableSchemaRepository tableSchemaRepository;

  @DisplayName("사용자 ID가 주어지면, 테이블 스키마 목록을 반환한다.")
  @Test
  void givenUserId_whenLoadingMySchemas_thenReturnsTableSchemaList() {
    // given
    String userId = "userId";
    given(tableSchemaRepository.findByUserId(userId, Pageable.unpaged())).willReturn(new PageImpl<>(List.of(
        TableSchema.of("table1", userId),
        TableSchema.of("table2", userId),
        TableSchema.of("table3", userId)
    )));
    // when
    List<TableSchemaDto> result = sut.loadMySchemas(userId);
    // then
    assertThat(result)
        .hasSize(3)
        .extracting("schemaName")
        .containsExactly("table1", "table2", "table3");
    then(tableSchemaRepository).should().findByUserId(userId, Pageable.unpaged());
  }

  @DisplayName("사용자 ID와 스키마 이름이 주어지면, 테이블 스키마를 반환한다.")
  @Test
  void givenUserIdAndSchemaName_whenLoadingMySchema_thenReturnsTableSchemaList() {
    // given
    String userId = "userId";
    String schemaName = "table1";
    TableSchema tableSchema = TableSchema.of(schemaName, userId);
    given(tableSchemaRepository.findByUserIdAndSchemaName(userId, schemaName)).willReturn(Optional.of(tableSchema));
    // when
    TableSchemaDto result = sut.loadMySchema(userId, schemaName);
    // then
    assertThat(result)
        .hasFieldOrPropertyWithValue("schemaName", schemaName)
        .hasFieldOrPropertyWithValue("userId", userId);
    then(tableSchemaRepository).should().findByUserIdAndSchemaName(userId, schemaName);
  }

  @DisplayName("사용자 ID와 스키마 이름에 대응하는 테이블 스키마가 없으면, 예외를 던진다")
  @Test
  void givenUserIdAndSchemaName_whenLoadingNonexistenMySchema_thenThrowsException() {
    // given
    String userId = "userId";
    String schemaName = "table1";
    given(tableSchemaRepository.findByUserIdAndSchemaName(userId, schemaName)).willReturn(Optional.empty());
    // when
    Throwable t = catchThrowable(() -> sut.loadMySchema(userId, schemaName));
    // then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("테이블 스키마가 없습니다 - UserId:"  + userId);
    then(tableSchemaRepository).should().findByUserIdAndSchemaName(userId, schemaName);
  }

  @DisplayName("테이블 스키마 정보가 주어지면, 테이블 스키마를 추가한다.")
  @Test
  void givenTableSchema_whenInserting_thenCreatesTableSchema() {
    // given
    TableSchemaDto dto = TableSchemaDto.of("table1", "userId",null, Set.of());
    given(tableSchemaRepository.save(dto.createEntity())).willReturn(null);
    // when
    sut.upsertTableSchema(dto);
    // then
    then(tableSchemaRepository).should().save(dto.createEntity());
  }

  @DisplayName("존재하지 않는 테이블 스키마 정보가 주어지면, 테이블 스키마를 추가한다.")
  @Test
  void givenNonexistentTableSchema_whenUpserting_thenCreatesTableSchema() {
    // given
    TableSchemaDto dto = TableSchemaDto.of("table1", "userId",null, Set.of());
    given(tableSchemaRepository.findByUserIdAndSchemaName(dto.userId(),dto.schemaName())).willReturn(Optional.empty());
    given(tableSchemaRepository.save(dto.createEntity())).willReturn(null);
    // when
    sut.upsertTableSchema(dto);
    // then
    then(tableSchemaRepository).should().findByUserIdAndSchemaName(dto.userId(),dto.schemaName());
    then(tableSchemaRepository).should().save(dto.createEntity());
  }

  @DisplayName("존재하는 테이블 스키마 정보가 주어지면, 테이블 스키마를 추가한다.")
  @Test
  void givenExistentTableSchema_whenUpserting_thenUpdatesTableSchema() {
    // given
    TableSchemaDto dto = TableSchemaDto.of("table1", "userId",null, Set.of());
    TableSchema existingTableSchema = TableSchema.of(dto.schemaName(), dto.userId());
    given(tableSchemaRepository.findByUserIdAndSchemaName(dto.userId(),dto.schemaName())).willReturn(Optional.of(existingTableSchema));
    given(tableSchemaRepository.save(dto.createEntity())).willReturn(null);
    // when
    sut.upsertTableSchema(dto);
    // then
    then(tableSchemaRepository).should().findByUserIdAndSchemaName(dto.userId(),dto.schemaName());
    then(tableSchemaRepository).should().save(dto.createEntity());
  }

  @DisplayName("사용자 ID와 스키마 이름이 주어지면, 테이블 스키마를 삭제한다.")
  @Test
  void givenUserIdAndSchemaName_whenDeleting_thenDeletesTableSchema() {
    // given
    String userId = "userId";
    String schemaName = "table1";
    willDoNothing().given(tableSchemaRepository).deleteByUserIdAndSchemaName(userId, schemaName);
    // when
    sut.deleteTableSchema(userId, schemaName);
    // then
    then(tableSchemaRepository).should().deleteByUserIdAndSchemaName(userId, schemaName);
  }
}