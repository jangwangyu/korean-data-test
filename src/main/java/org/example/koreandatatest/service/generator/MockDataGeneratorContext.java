package org.example.koreandatatest.service.generator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.springframework.stereotype.Service;

@Service
public class MockDataGeneratorContext {

  private final Map<MockDataType, MockDataGenerator> mockDataGeneratorMap;


  public MockDataGeneratorContext(
      List<MockDataGenerator> MockDataGenerators) {
    this.mockDataGeneratorMap = MockDataGenerators.stream()
        .collect(Collectors.toMap(MockDataGenerator::getType, Function.identity()));
  }

  public String generate(MockDataType mockDataType,Integer blankPercent,String typeOptionJson, String forceValue ) {
    MockDataGenerator generator = mockDataGeneratorMap.get(mockDataType);

    // TODO: 다양한 가짜 데이터 생성기가 도입되면, 이 기본값 강제 설정 코드를 삭제함
    if(generator == null) {
      generator = mockDataGeneratorMap.get(mockDataType.STRING);
    }

    return generator.generate(blankPercent,typeOptionJson,forceValue);
  }
}
