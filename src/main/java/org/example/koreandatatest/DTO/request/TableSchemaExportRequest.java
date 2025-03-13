package org.example.koreandatatest.DTO.request;

import java.util.List;
import java.util.stream.Collectors;
import org.example.koreandatatest.DTO.TableSchemaDto;
import org.example.koreandatatest.domain.constant.ExportFileType;

public record TableSchemaExportRequest(
    String schemaName,
    Integer lowCount,
    ExportFileType fileType,
    List<SchemaFieldRequest> schemaFields
) {
  public static TableSchemaExportRequest of(
      String schemaName,
      Integer lowCount,
      ExportFileType fileType,
      List<SchemaFieldRequest> schemaFields
  ) {
    return new TableSchemaExportRequest(schemaName, lowCount,fileType, schemaFields);
  }

  public TableSchemaDto toDto(String userId) {
    return TableSchemaDto.of(
        schemaName,
        userId,
        null,
        schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(Collectors.toUnmodifiableSet())
    );
  }

}
