package org.example.koreandatatest.service.exporter;

import org.example.koreandatatest.domain.constant.ExportFileType;
import org.springframework.stereotype.Component;

@Component
public class TSVFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter{

  @Override
  public String getDelimiter() {
    return "\t";
  }

  @Override
  public ExportFileType getType() {
    return ExportFileType.TSV;
  }

}
