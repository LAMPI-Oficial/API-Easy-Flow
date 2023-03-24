package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.studyArea.StudyAreaRequestDTO;
import br.com.ifce.easyflow.controller.dto.studyArea.StudyAreaResponseDTO;
import br.com.ifce.easyflow.controller.dto.studyArea.StudyAreaUpdateDTO;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.service.StudyAreaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/study_area")
public class StudyAreaController {

    private final StudyAreaService StudyAreaService;

    @Autowired
    private StudyAreaController(StudyAreaService StudyAreaService){
        this.StudyAreaService = StudyAreaService;
    }

    @ApiOperation(value = "Returns a list of StudyAreas", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public List<StudyAreaResponseDTO> search(){
        return this.StudyAreaService
                .search()
                .stream()
                .map(StudyAreaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Returns a StudyArea by id", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "StudyArea not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> searchById(@PathVariable Long id) {
        Optional<StudyArea> StudyArea = this.StudyAreaService.searchByID(id);

        return StudyArea.isPresent()
                ? ResponseEntity.ok(new StudyAreaResponseDTO(StudyArea.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "StudyArea Not Found");
    }

    @ApiOperation(value = "Returns a StudyArea by login", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "StudyArea not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @RequestMapping(value = "/search_by_name", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByName(@RequestParam String name) {
        Optional<StudyArea> StudyArea = this.StudyAreaService.searchByName(name);

        return StudyArea.isPresent()
                ? ResponseEntity.ok(new StudyAreaResponseDTO(StudyArea.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "StudyArea Not Found");
    }

    @ApiOperation(value = "Save a StudyArea", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 409, message = "StudyArea login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid StudyAreaRequestDTO StudyAreaRequest, UriComponentsBuilder uriBuilder){
        if(StudyAreaService.existsByStudyArea(StudyAreaRequest.getStudyArea_name())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("StudyArea Name is already in use.");
        }

        StudyArea StudyArea = StudyAreaRequest.toStudyArea();
        this.StudyAreaService.save(StudyArea);

        URI uri = uriBuilder.path("/StudyAreas/{id}").buildAndExpand(StudyArea.getId()).toUri();
        return ResponseEntity.created(uri).body(new StudyAreaResponseDTO(StudyArea));
    }

    @ApiOperation(value = "Update a StudyArea by id", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "StudyArea not found in database"),
            @ApiResponse(code = 409, message = "StudyArea login is already being used"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid StudyAreaUpdateDTO StudyAreaUpdateDTO) {
        Optional<StudyArea> StudyArea = this.StudyAreaService.searchByID(id);

        if(StudyArea.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "StudyArea Not Found");
        }

        if(!Objects.equals(StudyArea.get().getName(), StudyAreaUpdateDTO.getStudy_area_name()) && StudyAreaService.existsByStudyArea(StudyAreaUpdateDTO.getStudy_area_name())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("StudyAreaName is already in use.");
        }

        StudyArea.get().setName(StudyAreaUpdateDTO.getStudy_area_name());

        StudyArea = this.StudyAreaService.update(StudyAreaUpdateDTO.toStudyArea(id));

        return StudyArea.isPresent()
                ? ResponseEntity.ok(new StudyAreaResponseDTO(StudyArea.get()))
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "StudyArea Not Found");
    }

    @ApiOperation(value = "Delete a StudyArea by id", tags = {"StudyArea"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "StudyArea not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean removed = this.StudyAreaService.delete(id);

        return removed
                ? ResponseEntity.status(HttpStatus.OK).body(
                        "StudyArea was deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "StudyArea Not Found");
    }

}
