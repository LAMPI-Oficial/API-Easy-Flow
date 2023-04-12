package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.schedule.ScheduleApprovedRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.service.ScheduleService;
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
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "Returns a list of scheduled and unscheduled times",tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping
    public ResponseEntity<Page<Schedule>> listAll(Pageable pageable) {
        Page<Schedule> schedules = scheduleService.listAll(pageable);
        return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Returns a scheduled times by id",tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Scheduled not found in database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> findById(@PathVariable Long id) {
        ScheduleResponseDTO responseDTO = scheduleService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }


    @ApiOperation(value = "Returns a list of scheduled times by person id",tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Person not found in database"),
    })
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Schedule>> findByUserId(@PathVariable Long personId) {
       List<Schedule> schedules = scheduleService.findByUserId(personId);
       return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Returns a list of scheduled times by table id",tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Table not found in database"),
    })
    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<Schedule>> findByTableId(@PathVariable Long tableId) {
        List<Schedule> schedules = scheduleService.findAllByTableId(tableId);
        return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Returns a list of scheduled times by shift",
            notes = "Send the shifts: Morning, Afternoon and Night.",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/find-shift-schedule")
    public ResponseEntity<List<Schedule>> findByShiftSchedule(@RequestParam(defaultValue = "Morning") String shiftSchedule) {
        List<Schedule> schedules = scheduleService.findByShiftSchedule(shiftSchedule);
        return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Returns a list of scheduled times by status",
            notes = "Submit one of the following statuses: PENDING, APPROVED, DENIED",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/find-status")
    public ResponseEntity<List<Schedule>> findByStatusSchedule(@RequestParam(defaultValue = "PENDING") String status) {
        List<Schedule> schedules = scheduleService.findAllByStatus(status);
        return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Returns a list of scheduled times by weekday",
            notes = "Submit the name of the days of the week, such as: Monday, Tuesday, Wednesday, Thursday and Friday.",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
    })
    @GetMapping("/find-day")
    public ResponseEntity<List<Schedule>> findByDay(@RequestParam(defaultValue = "Monday") String day) {
        List<Schedule> schedules = scheduleService.findByDay(day);
        return ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Save a schedule times",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful request"),
            @ApiResponse(code = 404, message = "Person not found in database"),
    })
    @PostMapping("/create")
    public ResponseEntity<Schedule> save(@RequestBody @Valid SchedulePostRequestDTO requestDTO) {
        URI uri = URI.create("/create");
        return ResponseEntity.created(uri).body(scheduleService.save(requestDTO));
    }

    @ApiOperation(value = "Approve a schedule times by id",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Person or schedule not found in database"),
            @ApiResponse(code = 400, message = "The schedule request has a status other than pending."),
            @ApiResponse(code = 409, message = "This table is already booked for this time."),
    })
    @PatchMapping("/approve/{id}")
    public ResponseEntity<Schedule> approveSchedule(@PathVariable Long id,
                                                    @RequestBody @Valid ScheduleApprovedRequestDTO requestDTO) {

        return ResponseEntity.ok(scheduleService.approved(id, requestDTO));
    }

    @ApiOperation(value = "Deny a schedule times by id",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful request"),
            @ApiResponse(code = 404, message = "Schedule not found in database"),
            @ApiResponse(code = 400, message = "The schedule request has a status other than pending."),
    })
    @PatchMapping("/deny/{id}")
    public ResponseEntity<Schedule> denySchedule(@PathVariable Long id) {
        scheduleService.deny(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Update a schedule time",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Schedule not found in database"),
            @ApiResponse(code = 400, message = "The schedule request has a status other than pending."),
    })
    @PutMapping("/edit-schedule/{idSchedule}")
    public ResponseEntity<Schedule> update(@PathVariable Long idSchedule,
                                           @RequestBody @Valid SchedulePutRequestDTO requestDTO) {

        return ResponseEntity.ok(scheduleService.update(idSchedule, requestDTO));
    }

    @ApiOperation(value = "Update a schedule time",
            tags = {"Schedule"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful request"),
            @ApiResponse(code = 404, message = "Schedule not found in database"),
            @ApiResponse(code = 400, message = "The schedule request has a status other than pending."),
    })
    @DeleteMapping("/delete-schedule/{idSchedule}")
    public ResponseEntity<Void> delete(@PathVariable Long idSchedule) {
        scheduleService.delete(idSchedule);
        return ResponseEntity.noContent().build();
    }
}
