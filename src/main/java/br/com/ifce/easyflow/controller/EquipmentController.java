package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.Equipment.EquipmentPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.Equipment.EquipmentPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.Equipment.EquipmentStatusPatchRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.service.EquipmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @ApiOperation(value = "Returns a list of equipments",tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping
    public ResponseEntity<Page<Equipment>> findAll(Pageable pageable) {
        Page<Equipment> equipment = equipmentService.findAll(pageable);
        return ResponseEntity.ok(equipment);
    }

    @ApiOperation(value = "Returns a equipment by id",tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Equipment not found in database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> findById(@PathVariable Long id) {
        Equipment equipment = equipmentService.findById(id);
        return ResponseEntity.ok(equipment);
    }

    @ApiOperation(value = "Save a equipment",tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @PostMapping
    public ResponseEntity<Equipment> save(@RequestBody @Valid EquipmentPostRequestDTO requestDTO) {
        return ResponseEntity.ok(equipmentService.save(requestDTO));
    }

    @ApiOperation(value = "Update a equipment",tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Equipment not found in database"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Equipment> update(@PathVariable Long id, @RequestBody @Valid EquipmentPutRequestDTO requestDTO) {
        Equipment equipment = equipmentService.update(id, requestDTO);
        return ResponseEntity.ok(equipment);
    }

    @ApiOperation(value = "Update a equipment status",
            notes = "Submit one of these statuses: BUSY, AVAILABLE, MAINTENANCE",
            tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Equipment not found in database"),
    })
    @PatchMapping("/update-status/{id}")
    ResponseEntity<Equipment> updateStatus(@PathVariable Long id, @RequestBody @Valid EquipmentStatusPatchRequestDTO requestDTO) {
        Equipment equipment =equipmentService.updateEquipmentStatus(id, requestDTO);
        return ResponseEntity.ok(equipment);
    }

    @ApiOperation(value = "Delete a equipment by id",
            tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Equipment not found in database"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
