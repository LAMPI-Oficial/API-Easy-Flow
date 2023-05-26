package br.com.ifce.easyflow.service.schedule;

import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import br.com.ifce.easyflow.repository.ScheduleRepository;
import br.com.ifce.easyflow.service.ScheduleService;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    PersonRepository personRepository;

    @Mock
    ReservedTableRepository reservedTableRepository;

    @Mock
    LabTableRepository labTableRepository;

    @Test
    void listAll_Return_PageOfSchedule_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Schedule> scheduleList = List.of(createSchedule());
        PageImpl<Schedule> scheduleResponsePage = new PageImpl<>(scheduleList);

        when(scheduleRepository.findAll(any(Pageable.class))).thenReturn(scheduleResponsePage);

        List<Schedule> schedules = scheduleService.listAll(pageable).stream().toList();

        Assertions.assertEquals(scheduleList.get(0).getShiftSchedule(), schedules.get(0).getShiftSchedule());
        Assertions.assertEquals(scheduleList.get(0).getId(), schedules.get(0).getId());
        Assertions.assertEquals(scheduleList.get(0).getPerson(), schedules.get(0).getPerson());
        Assertions.assertEquals(scheduleList.get(0).getTable(), schedules.get(0).getTable());

    }

    @Test
    void findById_Return_ScheduleDTO_WhenSuccessful() {
        Schedule schedule = createSchedule();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        ScheduleResponseDTO scheduleResponseDTO = scheduleService.findById(1L);

        Assertions.assertEquals(schedule.getDay(), scheduleResponseDTO.getDay());
        Assertions.assertEquals(schedule.getShiftSchedule(), scheduleResponseDTO.getShiftSchedule());
        Assertions.assertEquals(schedule.getPerson(), scheduleResponseDTO.getPerson());
    }

    @Test
    void findById_Throw_ResourceNotFoundException_WhenScheduleNotFound() {

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.findById(1L));

        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("No time was found with the given id."));
    }

    @Test
    void findByUserId_Return_ListOfSchedulesByUserId_WhenSuccessful() {
        Schedule schedule = createSchedule();
        List<Schedule> schedules = List.of(schedule);

        when(scheduleRepository.findByPersonId(anyLong())).thenReturn(Optional.of(schedules));
        when(personRepository.existsById(anyLong())).thenReturn(true);

        List<Schedule> scheduleList = scheduleService.findByUserId(1L);

        Assertions.assertEquals(schedule.getShiftSchedule(), scheduleList.get(0).getShiftSchedule());
        Assertions.assertEquals(schedule.getDay(), scheduleList.get(0).getDay());
        Assertions.assertEquals(schedule.getPerson(), scheduleList.get(0).getPerson());
    }

    @Test
    void findByUserId_Throw_ResourceNotFoundException_WhenNotFoundUserSchedules() {

        when(scheduleRepository.findByPersonId(anyLong())).thenReturn(Optional.empty());
        when(personRepository.existsById(anyLong())).thenReturn(true);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.findByUserId(1L));
        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("The user has no registered schedules."));
    }

    @Test
    void findByUserId_Throw_PersonNotFoundException_WhenPersonNotFound() {

        when(personRepository.existsById(anyLong())).thenReturn(false);

        PersonNotFoundException personNotFoundException = assertThrows(PersonNotFoundException.class,
                () -> scheduleService.findByUserId(1L));

        Assertions.assertTrue(personNotFoundException
                .getMessage()
                .contains("The person was not found in the database, please check the registered persons."));

        verifyNoInteractions(scheduleRepository);
        verify(personRepository, times(1)).existsById(anyLong());
    }

    @Test
    void findByShiftSchedule_Return_ListSchedulesByShift_WhenSuccessful() {
        Schedule schedule = createSchedule();
        List<Schedule> schedules = List.of(schedule);

        when(scheduleRepository.findByShiftSchedule(anyString())).thenReturn(schedules);

        List<Schedule> scheduleList = scheduleService.findByShiftSchedule("Morning");

        Assertions.assertEquals(schedule.getShiftSchedule(), scheduleList.get(0).getShiftSchedule());
        Assertions.assertEquals(schedule.getPerson(), scheduleList.get(0).getPerson());
        Assertions.assertEquals(schedule.getDay(), scheduleList.get(0).getDay());

    }

    @Test
    void findByDay_Return_ListSchedulesByDay_WhenSuccessful() {
        Schedule schedule = createSchedule();

        when(scheduleRepository.findByDay(anyString())).thenReturn(List.of(schedule));

        List<Schedule> scheduleList = scheduleService.findByDay("Monday");

        Assertions.assertEquals(schedule.getShiftSchedule(), scheduleList.get(0).getShiftSchedule());
        Assertions.assertEquals(schedule.getPerson(), scheduleList.get(0).getPerson());
        Assertions.assertEquals(schedule.getDay(), scheduleList.get(0).getDay());
    }

    @Test
    void findAllByStatus_Return_ListOfSchedulesByStatus_WhenSuccessful() {
        Schedule schedule = createSchedule();

        when(scheduleRepository.findAllByStatus(any(ScheduleRequestStatus.class))).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.findAllByStatus("PENDING");

        Assertions.assertEquals(schedule.getStatus(), schedules.get(0).getStatus());
        Assertions.assertEquals(schedule.getDay(), schedules.get(0).getDay());
        Assertions.assertEquals(schedule.getId(), schedules.get(0).getId());
        Assertions.assertEquals(schedule.getShiftSchedule(), schedules.get(0).getShiftSchedule());
    }

    @Test
    void findAllByStatus_Throw_BadRequestException_WhenStatusIsNotFound() {

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> scheduleService.findAllByStatus("Busy"));

        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("The status provided does not exist or was not properly written. Please check the documentation."));

        verifyNoInteractions(scheduleRepository);
    }

    @Test
    void findAllByTableId_Return_ListOfScheduleByTable_WhenSuccessful() {
        Schedule schedule = createSchedule();

        when(labTableRepository.existsById(anyLong())).thenReturn(true);
        when(scheduleRepository.findAllByTableId(anyLong())).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.findAllByTableId(1L);

        Assertions.assertEquals(schedule.getStatus(), schedules.get(0).getStatus());
        Assertions.assertEquals(schedule.getDay(), schedules.get(0).getDay());
        Assertions.assertEquals(schedule.getId(), schedules.get(0).getId());
        Assertions.assertEquals(schedule.getShiftSchedule(), schedules.get(0).getShiftSchedule());

        verify(scheduleRepository).findAllByTableId(anyLong());
        verify(labTableRepository).existsById(anyLong());
    }

    @Test
    void findAllByTableId_Throw_ResourceNotFoundException_WhenTableNotFound() {
        when(labTableRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.findAllByTableId(1L));

        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("No table was found with the provided id, check the registered tables."));

        verify(labTableRepository).existsById(anyLong());
        verifyNoInteractions(scheduleRepository);
    }

    @Test
    void save_Return_ListOfSchedule_WhenSuccessful() {

        Schedule schedule = createSchedule();
        Person person = createPerson();
        LabTable table = createTable();
        SchedulePostRequestDTO requestDTO = createSchedulePostRequestDTO();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(reservedTableRepository
                .existsByTableIdAndShiftScheduleAndDay(
                        anyLong(), anyString(), anyString())).thenReturn(false);


        List<Schedule> schedules = scheduleService.save(1L, List.of(requestDTO));

        Assertions.assertEquals(schedule.getShiftSchedule(), schedules.get(0).getShiftSchedule());
        Assertions.assertEquals(schedule.getDay(), schedules.get(0).getDay());
        Assertions.assertEquals(schedule.getPerson().getId(), schedules.get(0).getTable().getId());
        Assertions.assertEquals(1L, schedules.get(0).getPerson().getId());

    }

    @Test
    void save_Throw_PersonNotFoundException_WhenPersonNotFound() {

        SchedulePostRequestDTO requestDTO = createSchedulePostRequestDTO();

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        PersonNotFoundException personNotFoundException = assertThrows(PersonNotFoundException.class,
                () -> scheduleService.save(1L, List.of(requestDTO)));

        Assertions.assertTrue(personNotFoundException
                .getMessage()
                .contains("The person was not found in the database, please check the registered persons."));

        verify(personRepository).findById(anyLong());
        verifyNoInteractions(scheduleRepository);
        verifyNoInteractions(labTableRepository);
        verifyNoInteractions(reservedTableRepository);
    }

    @Test
    void save_Throw_ResourceNotFoundException_WhenTableNotFound() {

        SchedulePostRequestDTO requestDTO = createSchedulePostRequestDTO();
        Person person = createPerson();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.save(1L, List.of(requestDTO)));

        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("No table was found with the provided id, check the registered tables."));

        verify(personRepository).findById(anyLong());
        verify(labTableRepository).findById(anyLong());
        verifyNoInteractions(scheduleRepository);
        verifyNoInteractions(reservedTableRepository);
    }

    @Test
    void save_Throw_BadRequestException_WhenThereAreAlreadyTableReserved() {

        SchedulePostRequestDTO requestDTO = createSchedulePostRequestDTO();
        Person person = createPerson();
        LabTable table = createTable();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(reservedTableRepository.
                existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString())).thenReturn(true);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> scheduleService.save(1L, List.of(requestDTO)));

        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("This table is already booked for this time."));

        verify(personRepository).findById(anyLong());
        verify(labTableRepository).findById(anyLong());
        verify(reservedTableRepository).existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString());
        verifyNoInteractions(scheduleRepository);
    }

    @Test
    void update_Return_ScheduleUpdated_WhenSuccessful() {
        Schedule oldSchedule = createSchedule();
        LabTable table = createTable();
        SchedulePutRequestDTO requestDTO = createSchedulePutRequestDTO();
        Schedule newSchedule = createSchedule();

        newSchedule.setShiftSchedule(requestDTO.getShiftSchedule());
        newSchedule.setDay(requestDTO.getDay());

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(oldSchedule));
        when(reservedTableRepository.existsByScheduleId(anyLong())).thenReturn(true);
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(newSchedule);
        when(reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString())).thenReturn(false);

        Schedule scheduleUpdated = scheduleService.update(1L, requestDTO);

        Assertions.assertEquals(requestDTO.getDay(), scheduleUpdated.getDay());
        Assertions.assertEquals(requestDTO.getShiftSchedule(), scheduleUpdated.getShiftSchedule());
        Assertions.assertEquals(newSchedule.getTable(), scheduleUpdated.getTable());
        Assertions.assertEquals(newSchedule.getPerson(), scheduleUpdated.getPerson());
    }

    @Test
    void update_Throw_ResourceNotFoundException_WhenScheduleNotFound() {

        SchedulePutRequestDTO requestDTO = createSchedulePutRequestDTO();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.update(1L, requestDTO));

        Assertions.assertTrue(exception.getMessage().contains("No time was found with the given id."));

        verify(scheduleRepository, times(1)).findById(anyLong());
        verifyNoInteractions(labTableRepository);
        verifyNoInteractions(reservedTableRepository);
    }

    @Test
    void update_Throw_BedRequestException_WhenScheduleStatusIsDifferentOfPending() {

        Schedule schedule = createSchedule();
        SchedulePutRequestDTO requestDTO = createSchedulePutRequestDTO();

        schedule.setStatus(ScheduleRequestStatus.APPROVED);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> scheduleService.update(1L, requestDTO));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("The time request can only be edited if it is pending."));

        verify(scheduleRepository, times(1)).findById(anyLong());
        verifyNoInteractions(labTableRepository);
        verifyNoInteractions(reservedTableRepository);
    }

    @Test
    void update_Throw_ResourceNotFoundException_WhenTableNotFound() {

        Schedule schedule = createSchedule();
        SchedulePutRequestDTO requestDTO = createSchedulePutRequestDTO();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.update(1L, requestDTO));

        Assertions.assertTrue(exception.getMessage().contains("No table was found with the provided id, check the registered tables."));

        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(labTableRepository).findById(anyLong());
    }

    @Test
    void update_Throw_BedRequestException_WhenThereIsScheduleInTheDayAndShift() {

        Schedule schedule = createSchedule();
        LabTable table = createTable();
        SchedulePutRequestDTO requestDTO = createSchedulePutRequestDTO();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(labTableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(reservedTableRepository
                .existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString()))
                .thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> scheduleService.update(1L, requestDTO));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("This table is already booked for this time."));
    }

    @Test
    void approved_Return_ScheduleWithStatusApproved_WhenSuccessful() {
        Schedule oldSchedule = createSchedule();

        Schedule newSchedule = createSchedule();
        newSchedule.setStatus(ScheduleRequestStatus.APPROVED);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(oldSchedule));
        when(reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString())).thenReturn(true);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(newSchedule);

        Schedule scheduleResponse = scheduleService.approved(1L);

        Assertions.assertEquals(scheduleResponse.getStatus(), ScheduleRequestStatus.APPROVED);
        Assertions.assertEquals(newSchedule.getPerson(), scheduleResponse.getPerson());
        Assertions.assertEquals(newSchedule.getId(), scheduleResponse.getId());

    }

    @Test
    void approved_Throw_BadRequestException_WhenScheduleStatusIsDifferentOfPending() {
        Schedule schedule = createSchedule();
        schedule.setStatus(ScheduleRequestStatus.DENIED);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> scheduleService.approved(1L));

        Assertions.assertTrue(exception.getMessage().contains("The schedule request has a status other than pending."));

        verify(scheduleRepository).findById(anyLong());
        verifyNoMoreInteractions(scheduleRepository);
        verifyNoInteractions(reservedTableRepository);

    }

    @Test
    void approved_Throw_ResourceNotFoundException_WhenScheduleNotFound() {

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.approved(1L));

        Assertions.assertTrue(exception.getMessage().contains("No time was found with the given id."));

        verify(scheduleRepository).findById(anyLong());
        verifyNoMoreInteractions(scheduleRepository);
        verifyNoInteractions(reservedTableRepository);

    }

    @Test
    void approved_Throw_BadRequestException_WhenThereIsNoReservationForSchedule() {
        Schedule schedule = createSchedule();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(reservedTableRepository
                .existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString())).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> scheduleService.approved(1L));

        Assertions.assertTrue(exception
                .getMessage()
                .contains("This table is not reserved for this time. Please look at the requests."));

        verify(scheduleRepository).findById(anyLong());
        verify(reservedTableRepository).existsByTableIdAndShiftScheduleAndDay(anyLong(), anyString(), anyString());
        verifyNoMoreInteractions(scheduleRepository);

    }

    @Test
    void deny_DenySchedule_WhenSuccessful() {
        Schedule schedule = createSchedule();
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        scheduleService.deny(1L);

        verify(scheduleRepository).save(any(Schedule.class));

    }

    @Test
    void deny_Throw_ResourceNotFoundException_WhenScheduleNotFound() {

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.deny(1L));

        Assertions.assertTrue(exception.getMessage().contains("No time was found with the given id."));

        verify(scheduleRepository).findById(anyLong());
        verifyNoMoreInteractions(scheduleRepository);

    }

    @Test
    void deny_Throw_BadRequestException_WhenScheduleStatusIsDifferentOfPending() {

        Schedule schedule = createSchedule();
        schedule.setStatus(ScheduleRequestStatus.APPROVED);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> scheduleService.deny(1L));

        Assertions.assertTrue(exception.getMessage().contains("The schedule request has a status other than pending."));

        verify(scheduleRepository).findById(anyLong());
        verifyNoMoreInteractions(scheduleRepository);
        verifyNoInteractions(reservedTableRepository);

    }

    @Test
    void delete_DeleteSchedule_WhenSuccessful() {
        Schedule schedule = createSchedule();
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        scheduleService.delete(1L);

        verify(reservedTableRepository).deleteByShiftScheduleAndDayAndTableId(anyString(), anyString(), anyLong());
        verify(scheduleRepository).deleteById(anyLong());
    }

    @Test
    void delete_Throw_WhenScheduleNotFound() {

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.delete(1L));

        Assertions.assertTrue(exception.getMessage().contains("No time was found with the given id."));

        verify(scheduleRepository).findById(anyLong());
        verifyNoMoreInteractions(scheduleRepository);
        verifyNoInteractions(reservedTableRepository);
    }

    private SchedulePostRequestDTO createSchedulePostRequestDTO() {
        return SchedulePostRequestDTO.builder()
                .shiftSchedule("Monday")
                .day("Tuesday")
                .tableId(1L)
                .build();
    }

    private SchedulePutRequestDTO createSchedulePutRequestDTO() {
        return SchedulePutRequestDTO.builder()
                .shiftSchedule("Afternoon")
                .day("Friday")
                .tableId(1L)
                .build();
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .id(1L)
                .day("Tuesday")
                .shiftSchedule("Monday")
                .status(ScheduleRequestStatus.PENDING)
                .person(createPerson())
                .table(createTable())
                .build();
    }

    private Person createPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Person Name");
        person.setEmail("person@email.com");
        return person;
    }

    private LabTable createTable() {
        return LabTable.builder()
                .id(1L)
                .number(22L)
                .build();
    }
}