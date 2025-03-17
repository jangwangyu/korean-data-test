package org.example.koreandatatest.service;

import lombok.RequiredArgsConstructor;
import org.example.koreandatatest.DTO.TableSchemaDto;
import org.example.koreandatatest.domain.TableSchema;
import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.repository.TableSchemaRepository;
import org.example.koreandatatest.service.exporter.MockDataFileExporterContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchemaExportService {

  private final MockDataFileExporterContext mockDataFileExporterContext;
  private final TableSchemaRepository tableSchemaRepository;

  public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
    if (dto.userId() != null) {
      tableSchemaRepository.findByUserIdAndSchemaName(dto.userId(), dto.schemaName())
          .ifPresent(TableSchema::markExported);
    }
    return mockDataFileExporterContext.export(fileType, dto, rowCount);
  }
}
