package org.example.koreandatatest.DTO.response;

import org.example.koreandatatest.DTO.SchemaFieldDto;
import org.example.koreandatatest.domain.constant.MockDataType;

public record SchemaFieldResponse(
    String fieldName,
    MockDataType mockDataType,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue
) {
  public static SchemaFieldResponse fromDto(SchemaFieldDto dto) {
    return new SchemaFieldResponse(
        dto.fieldName(),
        dto.mockDataType(),
        dto.fieldOrder(),
        dto.blankPercent(),
        dto.typeOptionJson(),
        dto.forceValue()
    );
  }
}
