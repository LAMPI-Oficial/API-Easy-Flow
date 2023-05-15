package br.com.ifce.easyflow.service.daily;

import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.ReservedTables;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import br.com.ifce.easyflow.repository.ScheduleRepository;
import br.com.ifce.easyflow.service.daily.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.daily.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PersonRepository personRepository;
    private final ReservedTableRepository reservedTableRepository;
    private final LabTableRepository labTableRepository;

    public Page<Schedule> listAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    public ScheduleResponseDTO findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No time was found with the given id."));

        return ScheduleResponseDTO.toResponseDTO(schedule);
    }

    public List<Schedule> findByUserId(Long personId) {
        if (personRepository.existsById(personId)) {
            return scheduleRepository.findByPersonId(personId)
                    .orElseThrow(() -> new ResourceNotFoundException("The user has no registered schedules."));
        }
        throw new PersonNotFoundException();
    }

    public List<Schedule> findByShiftSchedule(String shiftSchedule) {
        return scheduleRepository.findByShiftSchedule(shiftSchedule);
    }

    public List<Schedule> findByDay(String day) {
        return scheduleRepository.findByDay(day);
    }

    public List<Schedule> findAllByStatus(String status) {

        boolean statusMatches = Arrays.stream(ScheduleRequestStatus
                        .values())
                .anyMatch(s -> s.name().equals(status.toUpperCase()));

        if (!statusMatches) {
            throw new BadRequestException("The status provided does not exist or was not properly written. " +
                    "Please check the documentation.");
        }

        return scheduleRepository.findAllByStatus(ScheduleRequestStatus.valueOf(status.toUpperCase()));

    }

    public List<Schedule> findAllByTableId(Long id) {

        boolean tableExist = labTableRepository.existsById(id);

        if (!tableExist) {
            throw new ResourceNotFoundException("No table was found with the provided id, " +
                    "check the registered tables.");
        }

        return scheduleRepository.findAllByTableId(id);
    }

    @Transactional
    public List<Schedule> save(Long personId, List<SchedulePostRequestDTO> requestDTO) {

        return requestDTO.stream()
                .map(re -> convertScheduleRequest(personId, re))
                .peek(scheduleRepository::save).toList();

    }

    @Transactional
    public Schedule update(Long idSchedule, SchedulePutRequestDTO requestDTO) {
        Schedule scheduleSaved = scheduleRepository.findById(idSchedule)
                .orElseThrow(() -> new ResourceNotFoundException("No time was found with the given id."));

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new BadRequestException("The time request can only be edited if it is pending.");
        }

        boolean existsReserveBySchedule = reservedTableRepository.existsByScheduleId(scheduleSaved.getId());

        if (existsReserveBySchedule) {
            reservedTableRepository.deleteByScheduleId(scheduleSaved.getId());
        }

        LabTable table = labTableRepository.findById(requestDTO.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("No table was found with the provided id, " +
                        "check the registered tables."));

        boolean existsOtherReserve = reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(
                table.getId(),
                requestDTO.getShiftSchedule(),
                requestDTO.getDay());

        if (existsOtherReserve) {
            throw new BadRequestException("This table is already booked for this time.");
        }


        Schedule scheduleToSave = updateScheduleEntity(scheduleSaved, requestDTO);

        ReservedTables reservedTable = ReservedTables.builder()
                .table(scheduleToSave.getTable())
                .shiftSchedule(scheduleToSave.getShiftSchedule())
                .day(scheduleToSave.getDay())
                .build();

        reservedTableRepository.save(reservedTable);

        return scheduleRepository.save(scheduleToSave);

    }

    @Transactional
    public Schedule approved(Long idSchedule) {

        Schedule scheduleSaved = scheduleRepository.findById(idSchedule)
                .orElseThrow();

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new BadRequestException("The schedule request has a status other than pending.");
        }

        boolean existsReserve = reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(scheduleSaved.getTable().getId(),
                scheduleSaved.getShiftSchedule(),
                scheduleSaved.getDay());

        if (!existsReserve) {
            throw new BadRequestException("This table is not reserved for this time. Please look at the requests.");
        }

        scheduleSaved.setStatus(ScheduleRequestStatus.APPROVED);

        return scheduleRepository.save(scheduleSaved);
    }

    @Transactional
    public void deny(Long id) {
        Schedule scheduleSaved = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No time was found with the given id."));

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new BadRequestException("The schedule request has a status other than pending.");
        }

        reservedTableRepository.deleteByShiftScheduleAndDayAndTableId(
                scheduleSaved.getShiftSchedule(),
                scheduleSaved.getDay(),
                scheduleSaved.getTable().getId());

        scheduleSaved.setStatus(ScheduleRequestStatus.DENIED);
        scheduleSaved.setTable(null);
        scheduleRepository.save(scheduleSaved);
    }

    @Transactional
    public void delete(Long idSchedule) {
        Schedule schedule = scheduleRepository.findById(idSchedule)
                .orElseThrow(() -> new ResourceNotFoundException("No time was found with the given id."));

        if (!schedule.getStatus().equals(ScheduleRequestStatus.APPROVED)) {

            scheduleRepository.deleteById(idSchedule);


        } else {
            reservedTableRepository.deleteByShiftScheduleAndDayAndTableId(schedule.getShiftSchedule(), schedule.getDay(), schedule.getTable().getId());
            scheduleRepository.deleteById(idSchedule);
        }
    }

    private Schedule updateScheduleEntity(Schedule scheduleSaved, SchedulePutRequestDTO requestDTO) {

        LabTable table = labTableRepository.findById(requestDTO.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("No table was found with the provided id, " +
                        "check the registered tables."));

        scheduleSaved.setShiftSchedule(requestDTO.getShiftSchedule());
        scheduleSaved.setDay(requestDTO.getDay());
        scheduleSaved.setTable(table);
        return scheduleSaved;
    }

    private Schedule convertScheduleRequest(Long personId, SchedulePostRequestDTO request) {
        Person person = personRepository.findById(personId)
                .orElseThrow(PersonNotFoundException::new);

        LabTable table = labTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("No table was found with the provided id, " +
                        "check the registered tables."));

        boolean existsReserve = reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(table.getId(),
                request.getShiftSchedule(),
                request.getDay());

        if (existsReserve) {
            throw new BadRequestException("This table is already booked for this time.");
        }

        Schedule schedule = Schedule.builder()
                .day(request.getDay())
                .shiftSchedule(request.getShiftSchedule())
                .status(ScheduleRequestStatus.PENDING)
                .table(table)
                .person(person)
                .build();

        ReservedTables reservedTable = ReservedTables.builder()
                .table(table)
                .shiftSchedule(request.getShiftSchedule())
                .day(request.getDay())
                .schedule(schedule)
                .build();

        reservedTableRepository.save(reservedTable);
        return schedule;
    }
}
