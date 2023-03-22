package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyResponseDTO;
import br.com.ifce.easyflow.service.DailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily")
public class DailyController {
    private final DailyService dailyService;

    @GetMapping
    public ResponseEntity<Page<DailyResponseDTO>> listAll(Pageable pageable) {
        return ResponseEntity.ok(dailyService.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> listById(@PathVariable Long id) {
        return ResponseEntity.ok(dailyService.findById(id));
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Page<DailyResponseDTO>> listsByIdPerson(@PathVariable Long id, Pageable pageable) {

        return ResponseEntity.ok(dailyService.listByPersonId(id, pageable));
    }

    @GetMapping("/find-by-date")
    public ResponseEntity<Page<DailyResponseDTO>> listByDate(@RequestParam(name = "date") String date, Pageable pageable){
        return ResponseEntity.ok(dailyService.listByDate(date, pageable));

    }

    @GetMapping("/person/{id}/find-by-date")
    public ResponseEntity<Page<DailyResponseDTO>> listDailysByPersonIdAndDate(@PathVariable Long id, @RequestParam(name = "date") String date, Pageable pageable) {

        return ResponseEntity.ok(dailyService.listByPersonIdAndDate(id, date, pageable));
    }

    @PostMapping()
    public ResponseEntity<DailyResponseDTO> save(@RequestBody @Valid DailyRequestSaveDTO dailyRequestSaveDTO) {
        URI uri = URI.create("/daily");
        return ResponseEntity.created(uri).body(dailyService.save(dailyRequestSaveDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> update(@PathVariable Long id, @RequestBody @Valid DailyRequestUpdateDTO dailyRequestUpdateDTO) {

        return ResponseEntity.ok(dailyService.update(id, dailyRequestUpdateDTO));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        dailyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

