package org.example.koreandatatest.repository;

import org.example.koreandatatest.domain.TableSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {

}