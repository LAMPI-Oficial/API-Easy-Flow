package br.com.ifce.easyflow.service.schedule;

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
    void findAllByStatus() {
    }

    @Test
    void findAllByTableId() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void approved() {
    }

    @Test
    void deny() {
    }

    @Test
    void delete() {
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