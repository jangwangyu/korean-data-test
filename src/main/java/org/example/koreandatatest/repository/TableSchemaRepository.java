package org.example.koreandatatest.repository;


import java.util.Optional;
import org.example.koreandatatest.domain.TableSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {

  Page<TableSchema> findByUserId(String userId, Pageable pageable); // 목록 조회
  Optional<TableSchema> findByUserIdAndSchemaName(String userId, String schemaName); // 특정 스키마 조회
  void deleteByUserIdAndSchemaName(String userId, String schemaName);
}