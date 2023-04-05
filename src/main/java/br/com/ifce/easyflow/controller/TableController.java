package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.table.SearchTablesAvailableRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.TablePostRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<List<LabTable>> findAll() {
        return ResponseEntity.ok(tableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTable> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<LabTable>> findAvailableTables(@RequestBody @Valid SearchTablesAvailableRequestDTO requestDTO) {
        return ResponseEntity.ok(tableService.tablesAvailable(requestDTO));
    }

    @PostMapping
    public ResponseEntity<LabTable> save(@RequestBody @Valid TablePostRequestDTO requestDTO, UriComponentsBuilder uriBuilder) {
        LabTable table = tableService.save(requestDTO);
        URI uri = uriBuilder.path("/tables/{id}").buildAndExpand(table.getId()).toUri();
        return ResponseEntity.created(uri).body(table);
    }

}
