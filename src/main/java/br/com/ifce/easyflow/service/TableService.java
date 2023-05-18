package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.table.LabTableUpdateRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.SearchTablesAvailableRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.ReservedTables;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("No table was found with the provided id, " +
                        "check the registered tables."));
    }

    @Transactional
    public LabTable save(LabTable newLabTable) {

        boolean existsTableWithNumber = labTableRepository
                .existsByNumber(newLabTable.getNumber());

        if (existsTableWithNumber) {
            throw new ConflictException("A table has already been registered with that number.");
        }

        return labTableRepository.save(newLabTable);
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
        return this.save(oldTable);

    }

    @Transactional
    public void delete(Long id) {

        LabTable table = this.findById(id);

        if (!scheduleService.findAllByTableId(id).isEmpty()) {
            throw new ConflictException("The table cannot be excluded because it is linked to times already reserved.");
        }

        labTableRepository.deleteById(table.getId());
    }
}
