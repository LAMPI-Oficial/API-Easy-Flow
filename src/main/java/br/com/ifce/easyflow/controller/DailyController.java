package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.daily.*;
import br.com.ifce.easyflow.service.DailyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily")
public class DailyController {
    private final DailyService dailyService;

    @ApiOperation(value = "Returns a list of dailys", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public ResponseEntity<List<DailySimpleResponseDTO>> listAll() {
        return ResponseEntity.ok(dailyService.listAll());
    }

    @ApiOperation(value = "Returns a daily by id", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Daily not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> listById(@PathVariable Long id) {
        return ResponseEntity.ok(dailyService.findById(id));
    }

    @ApiOperation(value = "Returns a dailys by person id", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/person/{id}")
    public ResponseEntity<Page<DailyResponseDTO>> listsByIdPerson(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(dailyService.listByPersonId(id, pageable));
    }

    @ApiOperation(value = "Returns the dailys by date. (Pattern: yyyy-MM-dd)", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Date pattern not supported. Default accepted (yyyy-MM-dd)"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/find-by-date")
    public ResponseEntity<Page<DailyResponseDTO>> listByDate(@RequestParam(name = "date") String date, Pageable pageable){
        return ResponseEntity.ok(dailyService.listByDate(date, pageable));

    }

    @ApiOperation(value = "Returns the dailys by person id and date. (Pattern: yyyy-MM-dd)", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Date pattern not supported. Default accepted (yyyy-MM-dd)"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/person/{id}/find-by-date")
    public ResponseEntity<Page<DailyResponseDTO>> listDailysByPersonIdAndDate(@PathVariable Long id, @RequestParam(name = "date") String date, Pageable pageable) {

        return ResponseEntity.ok(dailyService.listByPersonIdAndDate(id, date, pageable));
    }

    @ApiOperation(value = "Save a daily", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Daily successfully created"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Person not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping()
    public ResponseEntity<DailyResponseDTO> save(@RequestBody @Valid DailyRequestSaveDTO dailyRequestSaveDTO) {
        URI uri = URI.create("/daily");
        return ResponseEntity.created(uri).body(dailyService.save(dailyRequestSaveDTO));
    }

    @ApiOperation(value = "Update a daily by id", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Daily successfully updated"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Daily not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> update(@PathVariable Long id, @RequestBody @Valid DailyRequestUpdateDTO dailyRequestUpdateDTO) {

        return ResponseEntity.ok(dailyService.update(id, dailyRequestUpdateDTO));

    }
    @ApiOperation(value = "Send daily feedback by daily id", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Feedback successfully added"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Daily not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PatchMapping("/send-feedback/{id}")
    public ResponseEntity<DailyResponseDTO> saveFeedbackOfDaily(@PathVariable Long id, @RequestBody DailyRequestSaveFeedbackDTO dailyRequestSaveFeedbackDTO){

        return ResponseEntity.ok(dailyService.saveFeedback(id, dailyRequestSaveFeedbackDTO));
    }

    @ApiOperation(value = "Delete daily by id", tags = {"Daily"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Daily not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        dailyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

