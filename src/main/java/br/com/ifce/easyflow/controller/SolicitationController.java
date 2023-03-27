package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.solicitation.ApprovedSolicitationDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.model.Solicitation;
import br.com.ifce.easyflow.service.SolicitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/solicitations")
@RequiredArgsConstructor
public class SolicitationController {

    private final SolicitationService solicitationService;

    @GetMapping
    public ResponseEntity<List<Solicitation>> findAll(Pageable pageable) {
        return ResponseEntity.ok(solicitationService.findAll(pageable).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(solicitationService.findById(id));
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<List<Solicitation>> findByPersonId(@PathVariable(name = "id") Long personId,
                                                             Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByPersonId(personId, pageable).getContent());
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<List<Solicitation>> findByEquipmentId(@PathVariable(name = "id") Long equipmentId,
                                                                Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByEquipmentId(equipmentId, pageable).getContent());
    }

    @GetMapping("/find/start-date")
    public ResponseEntity<List<Solicitation>> findByStartDate(@RequestParam(name = "date") String startDate,
                                                              Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByStartDate(startDate, pageable).getContent());
    }

    @GetMapping("/find/end-date")
    public ResponseEntity<List<Solicitation>> findByEndDate(@RequestParam(name = "date") String endDate,
                                                            Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByEndDate(endDate, pageable).getContent());
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

    @PatchMapping("/approve-request/{id}")
    ResponseEntity<Solicitation> approveRequest(@PathVariable Long id,
                                                @RequestBody ApprovedSolicitationDTO requestDTO) {
        return ResponseEntity.ok(solicitationService.approvedSolicitation(id, requestDTO));

    }

    @PatchMapping("/deny-request/{id}")
    ResponseEntity<Void> denyRequest(@PathVariable Long id) {
        solicitationService.denySolicitation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
