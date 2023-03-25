package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.UpdateSolicitationStatusDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.addDeviceToSolicitationDTO;
import br.com.ifce.easyflow.model.Solicitation;
import br.com.ifce.easyflow.service.SolicitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/solicitations")
@RequiredArgsConstructor
public class SolicitationController {

    private final SolicitationService solicitationService;

    @GetMapping
    public ResponseEntity<Page<Solicitation>> findAll(Pageable pageable) {
        return ResponseEntity.ok(solicitationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(solicitationService.findById(id));
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Page<Solicitation>> findByPersonId(@PathVariable(name = "id") Long personId,
                                                             Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByPersonId(personId, pageable));
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<Page<Solicitation>> findByEquipmentId(@PathVariable(name = "id") Long equipmentId,
                                                                Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByEquipmentId(equipmentId, pageable));
    }

    @GetMapping("/find")
    public ResponseEntity<Page<Solicitation>> findByStartDate(@RequestParam(name = "start-date") String startDate,
                                                              Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByStartDate(startDate, pageable));
    }

    @GetMapping("/find")
    public ResponseEntity<Page<Solicitation>> findByEndDate(@RequestParam(name = "end-date") String endDate,
                                                              Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByEndDate(endDate, pageable));
    }

    @PostMapping
    public ResponseEntity<Solicitation> save(@RequestBody SolicitationPostRequestDTO requestDTO) {
        URI uri = URI.create("/solicitations");
        return ResponseEntity.created(uri).body(solicitationService.save(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitation> update(@PathVariable Long id, @RequestBody SolicitationPutRequestDTO requestDTO) {
        return ResponseEntity.ok(solicitationService.update(id, requestDTO));

    }

    @PatchMapping("update-status/{id}")
    public ResponseEntity<Solicitation> updateStatus(@PathVariable Long id,
                                                     @RequestBody UpdateSolicitationStatusDTO requestDTO) {

        return ResponseEntity.ok(solicitationService.updateStatus(id, requestDTO));

    }

    @PatchMapping("/add-device/{solicitationId}")
    public ResponseEntity<Solicitation> addDeviceToSolicitation(@PathVariable Long solicitationId,
                                                                @RequestBody addDeviceToSolicitationDTO requestDTO) {
        return ResponseEntity.ok(solicitationService.addDevice(solicitationId, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
