package org.example.koreandatatest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.koreandatatest.domain.constant.MockDataType;

/**
 * 특정 {@link MockDataType}에 대응하는 가짜 데이터 알고리즘으로 생성하지 않는 {@link MockDataType}의 경우, 이 가짜 데이터를 랜덤으로 뽑아
 * 출력한다
 *
 * @author dkfdj
 */

@Getter
@ToString
@Table(uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"mockDataType", "mockDataValue"}) // UK
})
@Entity
public class MockData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MockDataType mockDataType;

  @Setter
  @Column(nullable = false)
  private String mockDataValue;

  protected MockData() {
  }

  private MockData(MockDataType mockDataType, String mockDataValue) {
    this.mockDataType = mockDataType;
    this.mockDataValue = mockDataValue;
  }

  public static MockData of(MockDataType mockDataType, String mockDataValue) {
    return new MockData(mockDataType, mockDataValue);
  }


  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MockData that)) { // o가 MockData의 인스턴스가 아닌 경우
      return false;
    }
    if (getId() == null) { // id가 없는 경우
      return Objects.equals(this.getMockDataType(), that.getMockDataType()) &&
          Objects.equals(this.getMockDataValue(), that.getMockDataValue());

    }
    return Objects.equals(this.getId(), that.getId()); // id가 있는 경우
  }

  @Override
  public int hashCode() {
    if(getId() == null) {
      return Objects.hash(getMockDataType(), getMockDataValue());
    }

    return Objects.hash(getId());
  }
}
