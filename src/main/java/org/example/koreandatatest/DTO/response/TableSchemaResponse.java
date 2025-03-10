package org.example.koreandatatest.DTO.response;

import java.util.List;
import org.example.koreandatatest.DTO.TableSchemaDto;

public record TableSchemaResponse(
    String schemaName,
    String userId,
    List<SchemaFieldResponse> schemaField
) {
  public static TableSchemaResponse fromDto(TableSchemaDto dto) {
    return new TableSchemaResponse(
        dto.schemaName(),
        dto.userId(),
        dto.schemaFields().stream().map(SchemaFieldResponse::fromDto).toList()
    );
  }

}
