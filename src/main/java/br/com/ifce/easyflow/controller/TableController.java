package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.table.LabTableUpdateRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.SearchTablesAvailableRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.TablePostRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.service.TableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @ApiOperation(value = "Returns a list of tables",tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping
    public ResponseEntity<List<LabTable>> findAll() {
        return ResponseEntity.ok(tableService.findAll());
    }

    @ApiOperation(value = "Returns a table by id",tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Table not found in database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<LabTable> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    @ApiOperation(value = "Returns a list of available tables on the specified day and shift.",
            notes = "Submit the day of the week and shift to return tables that are not tied to a time reservation.",
            tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/available")
    public ResponseEntity<List<LabTable>> findAvailableTables(@RequestBody @Valid SearchTablesAvailableRequestDTO requestDTO) {
        return ResponseEntity.ok(tableService.tablesAvailable(requestDTO));
    }

    @ApiOperation(value = "Save a table",
            tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful request"),
            @ApiResponse(code = 409, message = "A table has already been registered with that number."),
    })
    @PostMapping
    public ResponseEntity<LabTable> save(@RequestBody @Valid TablePostRequestDTO requestDTO, UriComponentsBuilder uriBuilder) {

        LabTable newTable = LabTable.builder()
                .number(requestDTO.number())
                .build();

        LabTable table = tableService.save(newTable);
        URI uri = uriBuilder.path("/tables/{id}").buildAndExpand(table.getId()).toUri();
        return ResponseEntity.created(uri).body(table);
    }

    @ApiOperation(value = "Update table",
            tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Table not found in database"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<LabTable> update(@PathVariable Long id, @RequestBody @Valid LabTableUpdateRequestDTO requestDTO) {
        LabTable table = tableService.update(id, requestDTO);
        return ResponseEntity.ok(table);
    }

    @ApiOperation(value = "Delete table by id",
            tags = {"Tables"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful request"),
            @ApiResponse(code = 409, message = "The table cannot be excluded because it is linked to times already reserved."),
            @ApiResponse(code = 404, message = "Table not found in database"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
