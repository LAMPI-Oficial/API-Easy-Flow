package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.permission.PermissionRequestDTO;
import br.com.ifce.easyflow.controller.dto.permission.PermissionResponseDTO;
import br.com.ifce.easyflow.model.Permission;
import br.com.ifce.easyflow.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permissions")
@Api("Permission")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    private PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @ApiOperation(value = "Returns a list of permission", tags = {"Permission"})
    @GetMapping
    public List<PermissionResponseDTO> search() {
        return this.permissionService
                .search()
                .stream()
                .map(PermissionResponseDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Returns a permission by id", tags = {"Permission"})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Permission not found in database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> searchById(@PathVariable Long id) {
        Permission permission = this.permissionService.searchByID(id);

        return ResponseEntity.ok(new PermissionResponseDTO(permission));
    }

    @ApiOperation(value = "Save a permission", tags = {"Permission"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Permission created"),
    })
    @PostMapping
    public ResponseEntity<PermissionResponseDTO> save(@RequestBody @Valid PermissionRequestDTO permissionRequestDTO, UriComponentsBuilder uriBuilder) {
        Permission permission = permissionRequestDTO.toPermission();
        this.permissionService.save(permission);

        URI uri = uriBuilder.path("/groups/{id}").buildAndExpand(permission.getId()).toUri();
        return ResponseEntity.created(uri).body(new PermissionResponseDTO(permission));
    }

    @ApiOperation(value = "Update a permission by id", tags = {"Permission"})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Permission not found in database"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PermissionRequestDTO permissionRequestDTO) {
        Permission permission = this.permissionService.update(id, permissionRequestDTO.toPermission(id));

        return ResponseEntity.ok(new PermissionResponseDTO(permission));
    }

    @ApiOperation(value = "Delete a permission by id", tags = {"Permission"})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Permission not found in database"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean removed = this.permissionService.delete(id);

        return removed
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();

    }
}
