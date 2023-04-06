package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.table.LabTableUpdateRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.SearchTablesAvailableRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.TablePostRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.ReservedTables;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final LabTableRepository labTableRepository;
    private final ReservedTableRepository reservedTableRepository;
    private final ScheduleService scheduleService;

    public List<LabTable> findAll() {
        return labTableRepository.findAll();
    }

    public LabTable findById(Long id) {
        return labTableRepository.findById(id)
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException
    }

    @Transactional
    public LabTable save(TablePostRequestDTO requestDTO) {

        boolean existsTableWithNumber = labTableRepository
                .existsByNumber(requestDTO.number());

        if (existsTableWithNumber) {
            throw new RuntimeException("A table has already been registered with that number.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        LabTable table = LabTable.builder()
                .number(requestDTO.number())
                .build();

        return labTableRepository.save(table);
    }

    public List<LabTable> tablesAvailable(SearchTablesAvailableRequestDTO requestDTO) {

        List<ReservedTables> reservedTables = reservedTableRepository
                .findByShiftScheduleAndDay(requestDTO.shiftSchedule(), requestDTO.day());


        List<LabTable> tables = labTableRepository.findAll();

        return tables.stream()
                .filter(t -> reservedTables.stream()
                        .noneMatch((rt -> rt.getTable().getId()
                                .equals(t.getId())))).toList();
    }

    @Transactional
    public LabTable update(Long id, LabTableUpdateRequestDTO requestDTO) {

        LabTable oldTable = this.findById(id);
        oldTable.setNumber(requestDTO.number());
        return labTableRepository.save(oldTable);

    }

    @Transactional
    public void delete(Long id) {

        LabTable table = this.findById(id);

        if (!scheduleService.findAllByTableId(id).isEmpty()) {
            throw new RuntimeException("The table cannot be excluded because it is linked to times already reserved.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        labTableRepository.deleteById(table.getId());
    }
}
