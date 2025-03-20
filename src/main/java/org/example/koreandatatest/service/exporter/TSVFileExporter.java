package org.example.koreandatatest.service.exporter;

import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

@Component
public class TSVFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter{

  public TSVFileExporter(
      MockDataGeneratorContext mockDataGeneratorContext) {
    super(mockDataGeneratorContext);
  }

  @Override
  public String getDelimiter() {
    return "\t";
  }

  @Override
  public ExportFileType getType() {
    return ExportFileType.TSV;
  }

}
