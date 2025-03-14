package org.example.koreandatatest.DTO.response;

import java.time.LocalDateTime;
import org.example.koreandatatest.DTO.TableSchemaDto;

public record SimpleTableSchemaResponse(
    String schemaName,
    String userId,
    LocalDateTime modifiedAt
) {

  public static SimpleTableSchemaResponse fromDto(TableSchemaDto dto) {
    return new SimpleTableSchemaResponse(dto.schemaName(), dto.userId(), dto.modifiedAt());
  }


}
