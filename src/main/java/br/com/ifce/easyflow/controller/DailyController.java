package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.service.DailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public  ResponseEntity<Page<Daily>>findByUserId(Pageable pageable){
        return ResponseEntity.ok(dailyService.findByUserId(pageable));
    }
}
