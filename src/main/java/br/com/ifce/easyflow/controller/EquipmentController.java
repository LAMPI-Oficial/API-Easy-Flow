package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentStatusPatchRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<Page<Equipment>> findAll(Pageable pageable) {
        Page<Equipment> equipment = equipmentService.findAll(pageable);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> findById(@PathVariable Long id) {
        Equipment equipment = equipmentService.findById(id);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    public ResponseEntity<Equipment> save(@RequestBody EquipmentPostRequestDTO requestDTO) {
        return ResponseEntity.ok(equipmentService.save(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> update(@PathVariable Long id, @RequestBody EquipmentPutRequestDTO requestDTO) {
        Equipment equipment = equipmentService.update(id, requestDTO);
        return ResponseEntity.ok(equipment);
    }

    @PatchMapping("/update-status/{id}")
    ResponseEntity<Equipment> updateStatus(@PathVariable Long id, @RequestBody EquipmentStatusPatchRequestDTO requestDTO) {
        Equipment equipment =equipmentService.updateEquipmentStatus(id, requestDTO);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
