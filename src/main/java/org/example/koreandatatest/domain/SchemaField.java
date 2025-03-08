package org.example.koreandatatest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.koreandatatest.domain.constant.MockDataType;

/**
 * 특정 {@link TableSchema}의 단위 필드 정보. 이 필드들이 모여서 테이블 스키마를 구성한다.
 *
 * @author dkfdj
 */

@Getter
@Entity
@ToString(callSuper = true)
public class SchemaField extends AuditingFields{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;

  @Setter
  @ManyToOne(optional = false)
  private TableSchema tableSchema;

  @Setter @Column(nullable = false) @Enumerated(EnumType.STRING) private MockDataType mockDataType; // enum을 다룰 수 있게끔
  @Setter @Column(nullable = false) private Integer fieldOrder;
  @Setter @Column(nullable = false) private Integer blankPercent; // 빈칸 비율
  @Setter @Column(nullable = false) private String fieldName;

  private String typeOptionJson; // JSON 형태로 저장 {min: 1, max: 10, ...}
  private String forceValue; // 강제로 넣을 값

  protected SchemaField() {
  }

  private SchemaField(String fieldName, MockDataType mockDataType, Integer fieldOrder,
      Integer blankPercent, String typeOptionJson, String forceValue) {
    this.fieldName = fieldName;
    this.mockDataType = mockDataType;
    this.fieldOrder = fieldOrder;
    this.blankPercent = blankPercent;
    this.typeOptionJson = typeOptionJson;
    this.forceValue = forceValue;
  }

  public static SchemaField of(String fieldName, MockDataType mockDataType, Integer fieldOrder,
      Integer blankPercent, String typeOptionJson, String forceValue) {
    return new SchemaField(fieldName, mockDataType, fieldOrder, blankPercent, typeOptionJson,
        forceValue);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SchemaField that)) {
      return false;
    }
    if (that.getId() == null) {
      return Objects.equals(this.getTableSchema().getId(), that.getTableSchema().getId()) &&
              Objects.equals(this.getMockDataType(), that.getMockDataType()) &&
              Objects.equals(this.getFieldName(), that.getFieldName()) &&
              Objects.equals(this.getFieldOrder(), that.getFieldOrder()) &&
              Objects.equals(this.getBlankPercent(), that.getBlankPercent()) &&
              Objects.equals(this.getTypeOptionJson(), that.getTypeOptionJson()) &&
              Objects.equals(this.getForceValue(), that.getForceValue());
    }
    return Objects.equals(this.getId(), that.getId());
  }
  
  @Override
  public int hashCode() {
    if(getId() == null) {
      return Objects.hash(getTableSchema().getId(), getMockDataType(),getFieldName(),getFieldOrder(),getBlankPercent(),getTypeOptionJson(),getForceValue());
    }
    return Objects.hash(getId());
  }
}
