package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.schedule.ScheduleApprovedRequestDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Schedule>> findByUserId(@PathVariable Long personId) {
       List<Schedule> schedules = scheduleService.findByUserId(personId);
       return ResponseEntity.ok(schedules);
    }

    @GetMapping("/find-shift-schedule")
    public ResponseEntity<List<Schedule>> findByShiftSchedule(@RequestParam(defaultValue = "Manha") String shiftSchedule) {
        List<Schedule> schedules = scheduleService.findByShiftSchedule(shiftSchedule);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/find-day")
    public ResponseEntity<List<Schedule>> findByDay(@RequestParam(defaultValue = "Segunda") String day) {
        List<Schedule> schedules = scheduleService.findByDay(day);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/create")
    public ResponseEntity<Schedule> save(@RequestBody @Valid SchedulePostRequestDTO requestDTO) {
        URI uri = URI.create("/create");
        return ResponseEntity.created(uri).body(scheduleService.save(requestDTO));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Schedule> approveSchedule(@PathVariable Long id,
                                                    @RequestBody @Valid ScheduleApprovedRequestDTO requestDTO) {

        return ResponseEntity.ok(scheduleService.approved(id, requestDTO));
    }

    @PatchMapping("/deny/{id}")
    public ResponseEntity<Schedule> approveSchedule(@PathVariable Long id) {
        scheduleService.deny(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit-schedule/{idSchedule}")
    public ResponseEntity<Schedule> update(@PathVariable Long idSchedule,
                                           @RequestBody @Valid SchedulePutRequestDTO requestDTO) {

        return ResponseEntity.ok(scheduleService.update(idSchedule, requestDTO));
    }

    @DeleteMapping("/delete-schedule/{idSchedule}")
    public ResponseEntity<Void> delete(@PathVariable Long idSchedule) {
        scheduleService.delete(idSchedule);
        return ResponseEntity.noContent().build();
    }
}
