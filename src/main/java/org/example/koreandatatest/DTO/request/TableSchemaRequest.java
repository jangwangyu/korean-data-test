package org.example.koreandatatest.DTO.request;

import java.util.List;
import java.util.stream.Collectors;
import org.example.koreandatatest.DTO.TableSchemaDto;

public record TableSchemaRequest(
    String tableName,
    String userId,
    List<SchemaFieldRequest> schemaFields
) {

  public TableSchemaDto toDto() {
    return TableSchemaDto.of(
        tableName,
        userId,
        null,
        schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(Collectors.toUnmodifiableSet())
    );
  }

}
