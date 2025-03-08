package org.example.koreandatatest.repository;

import org.example.koreandatatest.domain.MockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData, Long> {

}
