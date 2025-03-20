package org.example.koreandatatest.repository;

import java.util.List;
import org.example.koreandatatest.domain.MockData;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData, Long> {

  @Cacheable("mockData")
  List<MockData> findByMockDataType(MockDataType mockDataType);

}
