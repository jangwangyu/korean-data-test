package org.example.koreandatatest.DTO;

import org.example.koreandatatest.domain.MockData;
import org.example.koreandatatest.domain.constant.MockDataType;

public record MockDataDto(
    Long id,
    MockDataType mockDataType,
    String mockDataValue
) {
  public static MockDataDto fromEntity(MockData entity) {
    return new MockDataDto(
        entity.getId(),
        entity.getMockDataType(),
        entity.getMockDataValue()
    );
  }

}
