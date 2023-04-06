package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.schedule.ScheduleApprovedRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.ReservedTables;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import br.com.ifce.easyflow.repository.LabTableRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.ReservedTableRepository;
import br.com.ifce.easyflow.repository.ScheduleRepository;
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
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

        return ScheduleResponseDTO.toResponseDTO(schedule);
    }

    public List<Schedule> findByUserId(Long personId) {
        return scheduleRepository.findByPersonId(personId)
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

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
            throw new RuntimeException("The chosen status does not exist");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        return scheduleRepository.findAllByStatus(ScheduleRequestStatus.valueOf(status.toUpperCase()));

    }

    public List<Schedule> findAllByTableId(Long id) {

        boolean tableExist = labTableRepository.existsById(id);

        if (!tableExist) {
            throw new RuntimeException("There is no table with id: " + id);
            // TODO: Throw a more specific exception, e.g. NotFoundException
        }

        return scheduleRepository.findAllByTableId(id);
    }

    @Transactional
    public Schedule save(SchedulePostRequestDTO requestDTO) {
        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

        Schedule scheduleToSave = Schedule.builder()
                .day(requestDTO.getDay())
                .shiftSchedule(requestDTO.getShiftSchedule())
                .status(ScheduleRequestStatus.PENDING)
                .person(person)
                .build();

        return scheduleRepository.save(scheduleToSave);
    }

    @Transactional
    public Schedule update(Long idSchedule, SchedulePutRequestDTO requestDTO) {
        Schedule scheduleSaved = scheduleRepository.findById(idSchedule)
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new RuntimeException("The time request can only be edited if it is pending.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }
        Schedule scheduleToSave = updateScheduleEntity(scheduleSaved, requestDTO);

        return scheduleRepository.save(scheduleToSave);

    }

    @Transactional
    public Schedule approved(Long idSchedule, ScheduleApprovedRequestDTO requestDTO) {

        Schedule scheduleSaved = scheduleRepository.findById(idSchedule)
                .orElseThrow();

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new RuntimeException("The schedule request has a status other than pending.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        LabTable table = labTableRepository.findById(requestDTO.getTableId())
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

        boolean existsReserve = reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(table.getId(),
                scheduleSaved.getShiftSchedule(),
                scheduleSaved.getDay());

        if (existsReserve) {
            throw new RuntimeException("This table is already booked for this time.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        ReservedTables reservedTable = ReservedTables.builder()
                .table(table)
                .shiftSchedule(scheduleSaved.getShiftSchedule())
                .day(scheduleSaved.getDay())
                .build();

        reservedTableRepository.save(reservedTable);

        scheduleSaved.setTable(table);
        scheduleSaved.setStatus(ScheduleRequestStatus.APPROVED);

        return scheduleRepository.save(scheduleSaved);
    }

    @Transactional
    public void deny(Long id) {
        Schedule scheduleSaved = scheduleRepository.findById(id)
                .orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new RuntimeException("The schedule request has a status other than pending.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        scheduleSaved.setStatus(ScheduleRequestStatus.DENIED);
        scheduleRepository.save(scheduleSaved);
    }

    @Transactional
    public void delete(Long idSchedule) {
        Schedule schedule = scheduleRepository.findById(idSchedule).orElseThrow();
        // TODO: Throw a more specific exception, e.g. NotFoundException
        if (!schedule.getStatus().equals(ScheduleRequestStatus.APPROVED)) {

            scheduleRepository.deleteById(idSchedule);


        } else {

            reservedTableRepository.deleteByShiftScheduleAndDayAndTableId(schedule.getShiftSchedule(), schedule.getDay(), schedule.getTable().getId());
            scheduleRepository.deleteById(idSchedule);
        }
    }

    private Schedule updateScheduleEntity(Schedule scheduleSaved, SchedulePutRequestDTO requestDTO) {
        scheduleSaved.setShiftSchedule(requestDTO.getShiftSchedule());
        scheduleSaved.setDay(requestDTO.getDay());
        return scheduleSaved;
    }
}
