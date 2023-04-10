package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.solicitation.ApprovedSolicitationDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.model.Solicitation;
import br.com.ifce.easyflow.service.SolicitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Returns a list of solicitations",tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping
    public ResponseEntity<List<Solicitation>> findAll(Pageable pageable) {
        return ResponseEntity.ok(solicitationService.findAll(pageable).getContent());
    }

    @ApiOperation(value = "Returns a solicitation by id", tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Solicitation not found in database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Solicitation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(solicitationService.findById(id));
    }

    @ApiOperation(value = "Returns a list of solicitation by person id", tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Person not found in database"),
    })
    @GetMapping("/person/{id}")
    public ResponseEntity<List<Solicitation>> findByPersonId(@PathVariable(name = "id") Long personId,
                                                             Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByPersonId(personId, pageable).getContent());
    }

    @ApiOperation(value = "Returns a list of solicitation by equipment id", tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Equipment not found in database"),
    })
    @GetMapping("/equipment/{id}")
    public ResponseEntity<List<Solicitation>> findByEquipmentId(@PathVariable(name = "id") Long equipmentId,
                                                                Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findByEquipmentId(equipmentId, pageable).getContent());
    }

    @ApiOperation(value = "Returns a list of solicitation by start date",
            notes = "Standard set by ISO - 8601: yyyy-MM-dd",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/find/start-date")
    public ResponseEntity<List<Solicitation>> findByStartDate(@RequestParam(name = "date") String startDate,
                                                              Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByStartDate(startDate, pageable).getContent());
    }

    @ApiOperation(value = "Returns a list of solicitation by end date",
            notes = "Standard set by ISO - 8601: yyyy-MM-dd",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/find/end-date")
    public ResponseEntity<List<Solicitation>> findByEndDate(@RequestParam(name = "date") String endDate,
                                                            Pageable pageable) {

        return ResponseEntity.ok(solicitationService.findAllByEndDate(endDate, pageable).getContent());
    }

    @ApiOperation(value = "Save a solicitation",
            notes = "Standard set by ISO - 8601: yyyy-MM-dd",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Person not found in database"),
    })
    @PostMapping
    public ResponseEntity<Solicitation> save(@RequestBody SolicitationPostRequestDTO requestDTO) {
        URI uri = URI.create("/solicitations");
        return ResponseEntity.created(uri).body(solicitationService.save(requestDTO));
    }

    @ApiOperation(value = "Update a solicitation",
            notes = "Standard set by ISO - 8601: yyyy-MM-dd",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Solicitation not found in database"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Solicitation> update(@PathVariable Long id, @RequestBody SolicitationPutRequestDTO requestDTO) {
        return ResponseEntity.ok(solicitationService.update(id, requestDTO));

    }

    @ApiOperation(value = "Approve a solicitation by id",
            notes = "The request must have the status equal to pending, and the equipment must be available.",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Solicitation or equipment not found in database"),
    })
    @PatchMapping("/approve/{id}")
    ResponseEntity<Solicitation> approveRequest(@PathVariable Long id,
                                                @RequestBody ApprovedSolicitationDTO requestDTO) {
        return ResponseEntity.ok(solicitationService.approvedSolicitation(id, requestDTO));

    }

    @ApiOperation(value = "Deny a solicitation by id",
            notes = "The request must have the status equal to pending",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Solicitation not found in database"),
    })
    @PatchMapping("/deny/{id}")
    ResponseEntity<Void> denyRequest(@PathVariable Long id) {
        solicitationService.denySolicitation(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete a solicitation by id",
            tags = {"Solicitation"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Solicitation or equipment not found in database"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
