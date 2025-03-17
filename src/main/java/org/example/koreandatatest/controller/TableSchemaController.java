package org.example.koreandatatest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.koreandatatest.DTO.request.TableSchemaExportRequest;
import org.example.koreandatatest.DTO.request.TableSchemaRequest;
import org.example.koreandatatest.DTO.response.SchemaFieldResponse;
import org.example.koreandatatest.DTO.response.SimpleTableSchemaResponse;
import org.example.koreandatatest.DTO.response.TableSchemaResponse;
import org.example.koreandatatest.DTO.security.GithubUser;
import org.example.koreandatatest.domain.constant.ExportFileType;
import org.example.koreandatatest.domain.constant.MockDataType;
import org.example.koreandatatest.service.SchemaExportService;
import org.example.koreandatatest.service.TableSchemaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

  private final ObjectMapper mapper;
  private final TableSchemaService tableSchemaService;
  private final SchemaExportService schemaExportService;

  @GetMapping("/table-schema")
  public String tableSchema(Model model, @RequestParam(required = false) String schemaName,
      @AuthenticationPrincipal GithubUser githubUser) {
    TableSchemaResponse tableSchema = (githubUser != null && schemaName != null) ?
        TableSchemaResponse.fromDto(tableSchemaService.loadMySchema(githubUser.id(), schemaName)) :
        defaultTableSchema(schemaName);

    model.addAttribute("tableSchema", tableSchema);
    model.addAttribute("mockDataTypes", MockDataType.toObjects());
    model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());

    return "table-schema";
  }



  @PostMapping("/table-schema")
  public String createOrUpdateTableSchema(
      @AuthenticationPrincipal GithubUser githubUser,
      TableSchemaRequest tableSchemaRequest,
      RedirectAttributes redirectAttr
  ) {
    tableSchemaService.upsertTableSchema(tableSchemaRequest.toDto(githubUser.id()));

    redirectAttr.addAttribute("schemaName", tableSchemaRequest.schemaName());

    return "redirect:/table-schema";
  }

  @GetMapping("/table-schema/my-schemas")
  public String mySchemas(Model model,@AuthenticationPrincipal GithubUser githubUser) {

    List<SimpleTableSchemaResponse> tableSchemas = tableSchemaService.loadMySchemas(githubUser.id())
            .stream().map(SimpleTableSchemaResponse::fromDto).toList();

    model.addAttribute("tableSchemas", tableSchemas);

    return "my-schemas";
  }



  @PostMapping("/table-schema/my-schemas/{schemaName}")
  public String deleteMySchema(
      @AuthenticationPrincipal GithubUser githubUser,
      @PathVariable String schemaName,
      RedirectAttributes redirectAttr) {
    tableSchemaService.deleteTableSchema(githubUser.id(), schemaName);
    return "redirect:/table-schema/my-schemas";
  }

  @GetMapping("/table-schema/export")
  public ResponseEntity<String> exportTableSchema(TableSchemaExportRequest tableSchemaExportRequest, @AuthenticationPrincipal GithubUser githubUser) {

    String body = schemaExportService.export(tableSchemaExportRequest.fileType(),
        tableSchemaExportRequest.toDto(githubUser != null ? githubUser.id() : null), tableSchemaExportRequest.rowCount());
    String filename= tableSchemaExportRequest.schemaName() + "." + tableSchemaExportRequest;

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
        .body(body);
  }

  private TableSchemaResponse defaultTableSchema(String schemaName) {
    return new TableSchemaResponse(
        schemaName != null ? schemaName : "schema_name",
        "test_user",
        List.of(
            new SchemaFieldResponse("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
            new SchemaFieldResponse("name", MockDataType.NAME, 2, 10, null, null),
            new SchemaFieldResponse("age", MockDataType.NUMBER, 3, 20, null, null),
            new SchemaFieldResponse("my_car", MockDataType.CAR, 4, 50, null, null)
        )
    );
  }

}
