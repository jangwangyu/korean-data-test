package org.example.koreandatatest.service.generator;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.koreandatatest.domain.constant.MockDataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("")
@SpringBootTest
record MockDataGeneratorContextTest(@Autowired MockDataGeneratorContext sut) {

  @DisplayName("문자열 가짜 데이터 타입이 주어지면, 랜덤 문자열을 반환한다.")
  @Test
  void givenStringType_whenGeneratingMockData_thenReturnsRandomString() {
    // Given

    // When
    String result = sut.generate(MockDataType.STRING,0, null, null);
    // Then
    System.out.println(result);
    assertThat(result).containsPattern("^[가-힣]{1,10}$");
  }

}