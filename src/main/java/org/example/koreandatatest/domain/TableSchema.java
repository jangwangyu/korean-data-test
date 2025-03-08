package org.example.koreandatatest.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 단위 테이블 스키마 정보.
 * 식별자({@link #userId}) 로 특정할 수 있는 회원이 소유한다.
 *
 * @author dkfdj
 */

@Getter
@ToString(callSuper = true) // toString() 메서드 자동 생성
@Entity
public class TableSchema extends AuditingFields{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter private String schemaName;
  @Setter private String userId;
  @Setter private LocalDateTime exportedAt;

  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime modifiedAt;
  private String modifiedBy;

  @ToString.Exclude
  @OneToMany(mappedBy = "tableSchema", cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<SchemaField> schemaFields = new LinkedHashSet<>();


  protected TableSchema() {}

  private TableSchema(String schemaName, String userId) {
    this.schemaName = schemaName;
    this.userId = userId;
    this.exportedAt = null;
  }

  public static TableSchema of(String schemaName, String userId) {
    return new TableSchema(schemaName, userId);
  }

  public void markExported() { // export된 시간을 기록
    exportedAt = LocalDateTime.now();
  }

  public boolean isExported() { // export된 시간이 기록되어 있는지 확인
    return exportedAt != null;
  }

  // SchemaField를 컬렉션 레벨에서 제어할 수 있느 기능 추가
  public void addSchemaField(SchemaField schemaField) { // schemaField 추가
    schemaField.setTableSchema(this);
    schemaFields.add(schemaField);
  }

  public void addSchemaFields(Collection<SchemaField> schemaFields) { // schemaField 여러개 추가
    schemaFields.forEach(this::addSchemaField);
  }

  public void clearSchemaFields() { // schemaField 모두 삭제
    schemaFields.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TableSchema that)) {
      return false;
    }
    if(that.getId() == null) {
      return Objects.equals(getSchemaName(), that.getSchemaName()) &&
              Objects.equals(getUserId(), that.getUserId()) &&
              Objects.equals(getExportedAt(), that.getExportedAt()) &&
              Objects.equals(getSchemaFields(), that.getSchemaFields());
    }

    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    if (getId() == null){
      return Objects.hash(getSchemaName(), getUserId(), getExportedAt(), getSchemaFields());
    }

    return Objects.hash(getId());
  }
}
