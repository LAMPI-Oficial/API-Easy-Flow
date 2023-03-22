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

    //TODO: Verificar se é necessário fazer um método para fazer consultas de Daily´s por data.
    //TODO: Verificar se é necessário fazer um método PATCH para modificar o status da task.
    //TODO: Verificar os erros de validação.
    @GetMapping("/person/{id}")
    public ResponseEntity<Page<Daily>> listsByIdPerson(@PathVariable Long id, Pageable pageable){
        // TODO: Verificar se é necessario fazer um DTO para o response desse método
        return ResponseEntity.ok(dailyService.listByPersonId(id, pageable));
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

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        dailyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

