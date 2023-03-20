package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Page<Schedule>> listAll(Pageable pageable) {
        Page<Schedule> schedules = scheduleService.listAll(pageable);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> findById(@PathVariable Long id) {
        ScheduleResponseDTO responseDTO = scheduleService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Schedule> save(@RequestBody SchedulePostRequestDTO requestDTO) {
        URI uri = URI.create("/create");
        return ResponseEntity.created(uri).body(scheduleService.save(requestDTO));
    }

    @PutMapping("edit-schedule/{idSchedule}")
    public ResponseEntity<Schedule> update(@PathVariable Long idSchedule,
                                           @RequestBody SchedulePutRequestDTO requestDTO) {

        return ResponseEntity.ok(scheduleService.update(idSchedule, requestDTO));
    }

    @DeleteMapping("delete-schedule/{idSchedule}")
    public ResponseEntity<Void> delete(@PathVariable Long idSchedule) {
        scheduleService.delete(idSchedule);
        return ResponseEntity.noContent().build();
    }
}
