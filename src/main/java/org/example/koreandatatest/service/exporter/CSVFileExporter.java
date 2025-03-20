package org.example.koreandatatest.service.exporter;

import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;


@Component
public class CSVFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter{

  public CSVFileExporter(
      MockDataGeneratorContext mockDataGeneratorContext) {
    super(mockDataGeneratorContext);
  }

  @Override
  public String getDelimiter() {
    return ",";
  }

  @Override
  public ExportFileType getType() {
    return ExportFileType.CSV;
  }

}
