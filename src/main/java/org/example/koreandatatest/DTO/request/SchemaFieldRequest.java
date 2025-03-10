package org.example.koreandatatest.DTO.request;

import org.example.koreandatatest.DTO.SchemaFieldDto;
import org.example.koreandatatest.domain.constant.MockDataType;

public record SchemaFieldRequest(
    String fieldName,
    MockDataType mockDataType,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue
) {
  public static SchemaFieldRequest of(
      String fieldName,
      MockDataType mockDataType,
      Integer fieldOrder,
      Integer blankPercent,
      String typeOptionJson,
      String forceValue
  ) {
    return new SchemaFieldRequest(
        fieldName,
        mockDataType,
        fieldOrder,
        blankPercent,
        typeOptionJson,
        forceValue
    );
  }

  public SchemaFieldDto toDto() {
    return SchemaFieldDto.of(
        fieldName,
        mockDataType,
        fieldOrder,
        blankPercent,
        typeOptionJson,
        forceValue
    );
  }
}
