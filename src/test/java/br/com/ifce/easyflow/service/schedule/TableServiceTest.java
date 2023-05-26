package br.com.ifce.easyflow.service.schedule;

import br.com.ifce.easyflow.controller.dto.table.LabTableUpdateRequestDTO;
import br.com.ifce.easyflow.controller.dto.table.SearchTablesAvailableRequestDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.ReservedTables;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import br.com.ifce.easyflow.service.ScheduleService;
import br.com.ifce.easyflow.service.TableService;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    TableService tableService;
    @Mock
    LabTableRepository labTableRepository;
    @Mock
    ReservedTableRepository reservedTableRepository;
    @Mock
    ScheduleService scheduleService;

    @Test
    void findAll_Return_ListOfTables_WhenSuccessful() {
        LabTable labTable = createLabTable();
        when(labTableRepository.findAll()).thenReturn(List.of(labTable));

        List<LabTable> tables = tableService.findAll();

        Assertions.assertEquals(labTable.getNumber(), tables.get(0).getNumber());
        Assertions.assertEquals(labTable.getId(), tables.get(0).getId());

        verify(labTableRepository).findAll();
        verifyNoMoreInteractions(labTableRepository);
    }

    @Test
    void findById_Return_LabTable_WhenSuccessful() {
        LabTable labTable = createLabTable();
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(labTable));

        LabTable table = tableService.findById(1L);

        Assertions.assertEquals(labTable.getId(), table.getId());
        Assertions.assertEquals(labTable.getNumber(), table.getNumber());

        verify(labTableRepository).findById(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }

    @Test
    void findById_Throw_RecourseNotFoundException_WhenLabTableNotFound() {

        when(labTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> tableService.findById(1L));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("No table was found with the provided id, check the registered tables."));

        verify(labTableRepository).findById(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }

    @Test
    void save_Return_LabTable_WhenSuccessful() {
        LabTable labTable = createLabTable();

        LabTable newLabTable = createLabTable();

        when(labTableRepository.save(any(LabTable.class))).thenReturn(labTable);

        LabTable labTableResponse = tableService.save(newLabTable);

        Assertions.assertEquals(labTable.getNumber(), labTableResponse.getNumber());
        Assertions.assertEquals(labTable.getId(), labTableResponse.getId());

        verify(labTableRepository).save(any(LabTable.class));
    }

    @Test
    void save_Throw_ConflictException_WhenThereIsATableWithTheSameNumber() {

        LabTable newLabTable = createLabTable();

        when(labTableRepository.existsByNumber(anyLong())).thenReturn(true);

        ConflictException exception = Assertions.assertThrows(ConflictException.class,
                () -> tableService.save(newLabTable));

        Assertions.assertTrue(exception.getMessage().contains("A table has already been registered with that number."));

        verify(labTableRepository).existsByNumber(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }

    @Test
    void tablesAvailable_Return_ListOfTableAvailable_ForDayAndShift() {
        List<LabTable> labTables = createLabTables();

        SearchTablesAvailableRequestDTO requestDTO =
                new SearchTablesAvailableRequestDTO("Morning", "Monday");

        when(labTableRepository.findAll()).thenReturn(labTables);
        when(reservedTableRepository
                .findByShiftScheduleAndDay(anyString(), anyString())).thenReturn(List.of(createReservedTable()));

        List<LabTable> tablesAvailable = tableService.tablesAvailable(requestDTO);
        Assertions.assertEquals(4, tablesAvailable.size());
        Assertions.assertEquals(2, tablesAvailable.get(0).getNumber());
        Assertions.assertEquals(3, tablesAvailable.get(1).getNumber());
        Assertions.assertEquals(4, tablesAvailable.get(2).getNumber());
        Assertions.assertEquals(5, tablesAvailable.get(3).getNumber());
    }

    @Test
    void update_Return_LabTableUpdated_WhenSuccessful() {
        LabTable oldLabTable = createLabTable();
        LabTableUpdateRequestDTO requestDTO = new LabTableUpdateRequestDTO(4L);

        when(labTableRepository.existsByNumber(anyLong())).thenReturn(false);
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(oldLabTable));
        when(labTableRepository.save(any(LabTable.class))).thenReturn(oldLabTable);

        LabTable labTableUpdated = tableService.update(1L, requestDTO);

        Assertions.assertEquals(oldLabTable.getId(), labTableUpdated.getId());
        Assertions.assertNotEquals(1L, labTableUpdated.getNumber());
        Assertions.assertEquals(requestDTO.number(), labTableUpdated.getNumber());
    }

    @Test
    void update_Throw_ConflictException_WhenThereIsATableWithTheSameNumber() {
        LabTableUpdateRequestDTO requestDTO = new LabTableUpdateRequestDTO(4L);

        when(labTableRepository.existsByNumber(anyLong())).thenReturn(true);

        ConflictException exception = Assertions.assertThrows(ConflictException.class,
                () -> tableService.update(1L, requestDTO));

        Assertions.assertTrue(exception.getMessage().contains("A table has already been registered with that number."));

        verify(labTableRepository).existsByNumber(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }

    @Test
    void update_Throw_ResourceNotFoundException_WhenLabTableNotFound() {
        LabTableUpdateRequestDTO requestDTO = new LabTableUpdateRequestDTO(4L);

        when(labTableRepository.existsByNumber(anyLong())).thenReturn(false);
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> tableService.update(1L, requestDTO));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("No table was found with the provided id, check the registered tables."));

        verify(labTableRepository).findById(anyLong());
        verify(labTableRepository).existsByNumber(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }


    @Test
    void delete_Delete_LabTable_WhenSuccessful() {
        LabTable labTable = createLabTable();
        List<Schedule> schedules = new ArrayList<>();

        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(labTable));
        when(scheduleService.findAllByTableId(anyLong())).thenReturn(schedules);

        tableService.delete(1L);

        verify(labTableRepository).deleteById(anyLong());
    }

    @Test
    void delete_Throw_ResourceNotFoundException_WhenTableNotFound() {

        when(labTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions
                .assertThrows(ResourceNotFoundException.class, () -> tableService.delete(1L));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("No table was found with the provided id, check the registered tables."));

        verify(labTableRepository).findById(anyLong());
        verifyNoMoreInteractions(labTableRepository);
        verifyNoInteractions(scheduleService);
    }

    @Test
    void delete_Throw_ConflictException_WhenTableIsActiveOnASchedule() {
        LabTable labTable = createLabTable();
        Schedule schedule = Schedule.builder()
                .id(1L)
                .table(labTable)
                .build();

        when(scheduleService.findAllByTableId(anyLong())).thenReturn(List.of(schedule));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(labTable));

        ConflictException exception = Assertions
                .assertThrows(ConflictException.class, () -> tableService.delete(1L));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("The table cannot be excluded because it is linked to times already reserved."));

        verify(labTableRepository).findById(anyLong());
        verify(scheduleService).findAllByTableId(anyLong());
        verifyNoMoreInteractions(labTableRepository);
    }

    private LabTable createLabTable() {
        return LabTable.builder().id(1L).number(1L).build();
    }

    private List<LabTable> createLabTables() {
        List<LabTable> labTables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            labTables.add(new LabTable(i + 1L, i + 1L));
        }
        return labTables;
    }

    private ReservedTables createReservedTable() {
        return ReservedTables.builder()
                .day("Monday")
                .shiftSchedule("Morning")
                .table(createLabTable())
                .build();
    }
}