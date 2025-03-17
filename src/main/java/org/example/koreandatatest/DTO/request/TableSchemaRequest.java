package org.example.koreandatatest.DTO.request;

import java.util.List;
import java.util.stream.Collectors;
import org.example.koreandatatest.DTO.TableSchemaDto;

public record TableSchemaRequest(
    String schemaName,
    List<SchemaFieldRequest> schemaFields
) {
  public static TableSchemaRequest of(
      String schemaName,
      List<SchemaFieldRequest> schemaFields
  ) {
    return new TableSchemaRequest(schemaName, schemaFields);
  }

  public TableSchemaDto toDto(String userId) {
    return TableSchemaDto.of(
        schemaName,
        userId,
        null,
        schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(Collectors.toUnmodifiableSet())
    );
  }

}
