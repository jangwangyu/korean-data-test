package org.example.koreandatatest.domain.constant;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor // 생성자를 자동으로 생성해주는 어노테이션
public enum MockDataType { // enum 클래스
  STRING(Set.of("minLength", "maxLength", "pattern"), null), // 기본 타입은 null
  DATETIME(Set.of("from", "to"), null),
  NUMBER(Set.of("min", "max", "decimal"), null),
  ENUM(Set.of("elements"), null),
  BOOLEAN(Set.of(), null),

  SENTENCE(Set.of("minSentences", "maxSentences"), STRING),
  PARAGRAPH(Set.of("minParagraph", "maxParagraph"), STRING),
  UUID(Set.of(), STRING),
  EMAIL(Set.of(), STRING),
  CAR(Set.of(), STRING),
  ROW_NUMBER(Set.of("start", "step"), NUMBER),
  NAME(Set.of(), STRING)
  ;

  private final Set<String> requiredOptions; // 필수 옵션
  private final MockDataType baseType; // 기본 타입

  public boolean isBaseType() {return baseType == null;} // 기본 타입인지 확인하는 메서드

  public MockDataTypeObject toObject() {
    return new MockDataTypeObject(
        this.name(),
        this.requiredOptions,
        this.baseType == null ? null : this.baseType.name() // baseType이 null이 아니면 baseType 이름을 리턴
    );
  }

  public record MockDataTypeObject(
      String name,
      Set<String> requiredOptions,
      String baseType
  ) {}

}
