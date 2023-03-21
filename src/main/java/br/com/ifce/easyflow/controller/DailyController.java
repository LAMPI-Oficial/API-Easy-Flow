package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyResponseDTO;
import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.service.DailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily")
public class DailyController {
    private final DailyService dailyService;

    @GetMapping
    public ResponseEntity< Page<Daily>> listAll(Pageable pageable){
        return ResponseEntity.ok(dailyService.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> listById(@PathVariable Long id){
        return ResponseEntity.ok(dailyService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<DailyResponseDTO> save(@RequestBody @Valid DailyRequestSaveDTO dailyRequestSaveDTO){
        URI uri = URI.create("/daily");
        return ResponseEntity.created(uri).body(dailyService.save(dailyRequestSaveDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyResponseDTO> update(@PathVariable Long id,@RequestBody @Valid DailyRequestUpdateDTO dailyRequestUpdateDTO){

        return ResponseEntity.ok(dailyService.update(id,dailyRequestUpdateDTO));

    }

    @DeleteMapping("/id")
    public ResponseEntity delete(@PathVariable Long id){
        dailyService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/user/{id}")
//    public  ResponseEntity<Page<Daily>>findDailyByUserId(@PathVariable Long userId, Pageable pageable){
//        return ResponseEntity.ok(dailyService.findDailyByUserId(userId, pageable));
//    }
}
