package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.event.EventRequestDTO;
import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.service.EventService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @ApiOperation(value = "Returns a list of events", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> listAll(Pageable pageable){
        return ResponseEntity.ok(eventService.listAll(pageable));
    }

    @ApiOperation(value = "Returns the events by date.", notes = "Pattern: yyyy-MM-dd.  Example: /find-by-date?date=2023-10-25", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Date pattern not supported. Default accepted (yyyy-MM-dd)"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/find-by-date")
    public ResponseEntity<List<EventResponseDTO>> listByDate(@RequestParam(name = "date") String date, Pageable pageable){
        return ResponseEntity.ok(eventService.listByDate(date,pageable));
    }

    @ApiOperation(value = "Returns the events by date and time.", notes=" [Pattern of date: yyyy-MM-dd] [Pattern of time: HH:mm]. Example: /find-by-date-and-time?date=2023-10-25&time=15:30", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 400, message = "Date or time pattern not supported."),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/find-by-date-and-time")
    public ResponseEntity<List<EventResponseDTO>> listByDateTime(@RequestParam(name = "date") String date, @RequestParam(name = "time") String time, Pageable pageable){
        return ResponseEntity.ok(eventService.listByDateTime(date, time, pageable));
    }

    @ApiOperation(value = "Returns an event by event id", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Event not found"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> listById(@PathVariable Long id){
        return ResponseEntity.ok(eventService.listById(id));
    }

    @ApiOperation(value = "Save a event", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event successfully created"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PostMapping
    public ResponseEntity<EventResponseDTO> save(@RequestBody @Valid EventRequestDTO eventRequestSaveDTO){
        return ResponseEntity.ok(eventService.save(eventRequestSaveDTO));
    }

    @ApiOperation(value = "Update a event by id", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event successfully updated"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Event not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id ,@RequestBody @Valid EventRequestDTO eventRequestsDTO){
        return ResponseEntity.ok(eventService.update(id, eventRequestsDTO));
    }

    @ApiOperation(value = "Delete event by id", tags = {"Event"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Event not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.ok().build();
    }
}
