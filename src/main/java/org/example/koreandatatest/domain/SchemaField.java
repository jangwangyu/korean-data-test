package org.example.koreandatatest.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.koreandatatest.domain.constant.MockDataType;

@Getter
@Setter
@ToString
public class SchemaField {

  private String fieldName;
  private MockDataType mockDataType; // enum을 다룰 수 있게끔
  private Integer fieldOrder;
  private Integer blackPercent;
  private String typeOptionJson; // JSON 형태로 저장 {min: 1, max: 10, ...}
  private String forceValue; // 강제로 넣을 값

}
