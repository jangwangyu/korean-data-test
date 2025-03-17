package org.example.koreandatatest.service.exporter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.koreandatatest.DTO.TableSchemaDto;
import org.example.koreandatatest.domain.constant.ExportFileType;
import org.springframework.stereotype.Service;

@Service
public class MockDataFileExporterContext {

  private final Map<ExportFileType, MockDataFileExporter> mockDataFileExporterMap;


  public MockDataFileExporterContext(
      List<MockDataFileExporter> mockDataFileExporters) {
    this.mockDataFileExporterMap = mockDataFileExporters.stream()
        .collect(Collectors.toMap(MockDataFileExporter::getType, Function.identity()));
  }

  public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
    MockDataFileExporter fileExporter = mockDataFileExporterMap.get(fileType);

    return fileExporter.export(dto, rowCount);
  }
}
