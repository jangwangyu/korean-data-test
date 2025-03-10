package org.example.koreandatatest.DTO.response;

import org.example.koreandatatest.DTO.SchemaFieldDto;
import org.example.koreandatatest.domain.constant.MockDataType;

public record SchemaFieldResponse(
    MockDataType mockDataType,
    String fieldName,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue
) {
  public static SchemaFieldResponse fromDto(SchemaFieldDto dto) {
    return new SchemaFieldResponse(
        dto.mockDataType(),
        dto.fieldName(),
        dto.fieldOrder(),
        dto.blankPercent(),
        dto.typeOptionJson(),
        dto.forceValue()
    );
  }
}
