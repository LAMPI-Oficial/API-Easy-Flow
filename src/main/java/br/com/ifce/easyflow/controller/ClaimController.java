package br.com.ifce.easyflow.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.ifce.easyflow.controller.dto.claim.ClaimRequestDTO;
import br.com.ifce.easyflow.controller.dto.claim.ClaimResponseDTO;
import br.com.ifce.easyflow.controller.dto.claim.ClaimUpdateDTO;
import br.com.ifce.easyflow.model.Claim;
import br.com.ifce.easyflow.service.ClaimService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/claims")
public class ClaimController {

        private final ClaimService claimService;

        @Autowired
        private ClaimController(ClaimService claimService) {
                this.claimService = claimService;
        }

        @ApiOperation(value = "Returns a list of Claims", tags = { "Claim" })
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Successful request"),
                        @ApiResponse(code = 403, message = "Permission denied to access thisresource"),
                        @ApiResponse(code = 500, message = "Internal exception"),

        })

        @GetMapping
        public List<ClaimResponseDTO> search() {
                return this.claimService
                                .search()
                                .stream()
                                .map(ClaimResponseDTO::new)
                                .collect(Collectors.toList());
        }

        @ApiOperation(value = "Update a Claim by id", tags = { "Claim" })
        @ApiResponses(value = {
                        @ApiResponse(code = 201, message = "Saved reservation"),
                        @ApiResponse(code = 403, message = "Permission denied to access this resource"),
                        @ApiResponse(code = 404, message = "Claim not found in database"),
                        @ApiResponse(code = 500, message = "Internal exception"),
        })
        @PutMapping("/{id}")
        public ResponseEntity<Object> update(@PathVariable Long id,
                        @RequestBody @Valid ClaimUpdateDTO claimUpdateDTO) {
                Optional<Claim> claim = this.claimService.searchByID(id);

                if (claim.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        "Claim Not Found");
                }

                claim = this.claimService.update(claimUpdateDTO.toClaim(id));

                return claim.isPresent()
                                ? ResponseEntity.ok(new ClaimResponseDTO(claim.get()))
                                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                                "claim Not Found");
        }

        @ApiOperation(value = "Save a Claim", tags = { "Claim" })
        @ApiResponses(value = {
                        @ApiResponse(code = 201, message = "Saved reservation"),
                        @ApiResponse(code = 403, message = "Permission denied to access this resource"),
                        @ApiResponse(code = 500, message = "Internal exception"),
        })

        @PostMapping
        public ResponseEntity<Object> save(@RequestBody @Valid ClaimRequestDTO claimsRequest,
                        UriComponentsBuilder uriBuilder) {

                Claim claim = claimsRequest.toClaim();
                this.claimService.save(claim);

                URI uri = uriBuilder.path("/claims/{id}").buildAndExpand(claim.getId()).toUri();
                return ResponseEntity.created(uri).body(new ClaimResponseDTO(claim));
        }

        @ApiOperation(value = "Delete a Claim by id", tags = { "Claim" })
        @ApiResponses(value = {
                        @ApiResponse(code = 200, message = "Successful request"),
                        @ApiResponse(code = 403, message = "Permission denied to access this resource"),
                        @ApiResponse(code = 404, message = "Claims not found in database"),
                        @ApiResponse(code = 500, message = "Internal exception"),
        })

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> delete(@PathVariable Long id) {
                boolean removed = this.claimService.delete(id);

                return removed
                                ? ResponseEntity.status(HttpStatus.OK).body(
                                                "Claim was deleted")
                                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                                "Claim Not Found");
        }

        @ApiOperation(value = "Returns a Claim by id", tags = { "Claim" })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Successful request"),
                @ApiResponse(code = 403, message = "Permission denied to access this resource"),
                @ApiResponse(code = 404, message = "Claim not found in database"),
                @ApiResponse(code = 500, message = "Internal exception"),
        })
        @GetMapping("/{id}")
        public ResponseEntity<Object> searchById(@PathVariable Long id) {
                Optional<Claim> Claim = this.claimService.searchByID(id);

                return Claim.isPresent()
                                ? ResponseEntity.ok(new ClaimResponseDTO(Claim.get()))
                                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                                "Claim Not Found");
        }

}
