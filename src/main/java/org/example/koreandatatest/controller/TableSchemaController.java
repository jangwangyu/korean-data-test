package org.example.koreandatatest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.koreandatatest.DTO.request.TableSchemaExportRequest;
import org.example.koreandatatest.DTO.request.TableSchemaRequest;
import org.example.koreandatatest.DTO.response.SchemaFieldResponse;
import org.example.koreandatatest.DTO.response.TableSchemaResponse;
import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

  private final ObjectMapper mapper;


  @GetMapping("/table-schema")
  public String tableSchema(Model model) {
    var tableSchema = defaultTableSchema();
    model.addAttribute("tableSchema", tableSchema);
    model.addAttribute("mockDataTypes", MockDataType.toObjects());
    model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());
    return "table-schema";
  }



  @PostMapping("/table-schema")
  public String createOrUpdateTableSchema(
      TableSchemaRequest tableSchemaRequest,
      RedirectAttributes redirectAttr
  ) {
    redirectAttr.addFlashAttribute("tableSchemaRequest", tableSchemaRequest);

    return "redirect:/table-schema";
  }

  @GetMapping("/table-schema/my-schemas")
  public String mySchemas() {
    return "my-schemas";
  }

  @PostMapping("/table-schema/my-schemas/{schemaName}")
  public String deleteMySchema(
      @PathVariable String schemaName,
      RedirectAttributes redirectAttr) {
    return "redirect:/my-schemas";
  }

  @GetMapping("/table-schema/export")
  public ResponseEntity<String> exportTableSchema(TableSchemaExportRequest tableSchemaExportRequest) {

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
        .body(json(tableSchemaExportRequest));
  }

  private String json(Object object){
    try{
      return mapper.writeValueAsString(object);
    }catch (JsonProcessingException e){
      throw new RuntimeException(e);
    }
  }


  private TableSchemaResponse defaultTableSchema() {
    return new TableSchemaResponse(
        "schema_name",
        "test_user",
        List.of(
            new SchemaFieldResponse("fieldName1", MockDataType.STRING, 1, 0, null, null),
            new SchemaFieldResponse("fieldName2", MockDataType.NUMBER, 2, 10, null, null),
            new SchemaFieldResponse("fieldName3", MockDataType.NAME, 3, 20, null, null)
        )
    );
  }

}
