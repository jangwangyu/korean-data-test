package org.example.koreandatatest.DTO.request;

import java.util.List;
import java.util.stream.Collectors;
import org.example.koreandatatest.DTO.TableSchemaDto;

public record TableSchemaRequest(
    String schemaName,
    String userId,
    List<SchemaFieldRequest> schemaFields
) {
  public static TableSchemaRequest of(
      String schemaName,
      String userId,
      List<SchemaFieldRequest> schemaFields
  ) {
    return new TableSchemaRequest(schemaName, userId, schemaFields);
  }

  public TableSchemaDto toDto() {
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
