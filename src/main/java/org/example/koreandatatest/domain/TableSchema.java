package org.example.koreandatatest.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString // toString() 메서드 자동 생성
public class TableSchema {

  private String schemaName;
  private String userId;
  private LocalDateTime exportedAt;

}
