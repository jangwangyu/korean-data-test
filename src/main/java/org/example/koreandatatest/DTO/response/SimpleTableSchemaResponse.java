package org.example.koreandatatest.DTO.response;

import org.example.koreandatatest.DTO.TableSchemaDto;

public record SimpleTableSchemaResponse(
    String schemaName,
    String userId
) {

  public static SimpleTableSchemaResponse fromDto(TableSchemaDto dto) {
    return new SimpleTableSchemaResponse(dto.schemaName(), dto.userId());
  }


}
