package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.table.TablePostRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.repository.LabTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final LabTableRepository labTableRepository;

    public List<LabTable> findAll() {
        return labTableRepository.findAll();
    }

    public LabTable findById(Long id) {
        return labTableRepository.findById(id)
                .orElseThrow();

    }

    @Transactional
    public LabTable save(TablePostRequestDTO requestDTO) {

       boolean existsTableWithNumber = labTableRepository
               .existsByNumber(requestDTO.number());

       if (existsTableWithNumber) {
           throw new RuntimeException("A table has already been registered with that number.");
       }

      LabTable table = LabTable.builder()
              .number(requestDTO.number())
              .build();

       return labTableRepository.save(table);
    }
}
