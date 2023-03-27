package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.event.EventRequestDTO;
import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> listAll(Pageable pageable){
        return ResponseEntity.ok(eventService.listAll(pageable));
    }

    @GetMapping("/find-by-date")
    public ResponseEntity<Page<EventResponseDTO>> listByDate(@RequestParam(name = "date") String date, Pageable pageable){
        return ResponseEntity.ok(eventService.listByDate(date,pageable));
    }

    @GetMapping("/find-by-date-and-time")
    public ResponseEntity<Page<EventResponseDTO>> listByDateTime(@RequestParam(name = "date") String date, @RequestParam(name = "time") String time, Pageable pageable){
        return ResponseEntity.ok(eventService.listByDateTime(date, time, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> listById(@PathVariable Long id){
        return ResponseEntity.ok(eventService.listById(id));
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> save(@RequestBody EventRequestDTO eventRequestSaveDTO){
        return ResponseEntity.ok(eventService.save(eventRequestSaveDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id ,@RequestBody EventRequestDTO eventRequestsDTO){
        return ResponseEntity.ok(eventService.update(id, eventRequestsDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.ok().build();
    }
}
