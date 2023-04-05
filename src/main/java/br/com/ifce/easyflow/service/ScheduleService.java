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
        return ScheduleResponseDTO.toResponseDTO(schedule);
    }

    public List<Schedule> findByUserId(Long personId) {
        return scheduleRepository.findByPersonId(personId)
                .orElseThrow();
    }

    public List<Schedule> findByShiftSchedule(String shiftSchedule) {
        return scheduleRepository.findByShiftSchedule(shiftSchedule);
    }

    public List<Schedule> findByDay(String day) {
        return scheduleRepository.findByDay(day);
    }

    @Transactional
    public Schedule save(SchedulePostRequestDTO requestDTO) {
        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow();
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
        Schedule scheduleToSave = updateScheduleEntity(scheduleSaved, requestDTO);

        return scheduleRepository.save(scheduleToSave);

    }

    @Transactional
    public Schedule approved(Long idSchedule, ScheduleApprovedRequestDTO requestDTO) {

        Schedule scheduleSaved = scheduleRepository.findById(idSchedule)
                .orElseThrow();

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new RuntimeException("The schedule request has a status other than pending.");
        }

        LabTable table = labTableRepository.findById(requestDTO.getTableId())
                .orElseThrow();

        boolean existsReserve = reservedTableRepository.existsByTableIdAndShiftScheduleAndDay(table.getId(),
                scheduleSaved.getShiftSchedule(),
                scheduleSaved.getDay());

        if (existsReserve) {
            throw new RuntimeException("This table is already booked for this time.");
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

        if (!scheduleSaved.getStatus().equals(ScheduleRequestStatus.PENDING)) {
            throw new RuntimeException("The schedule request has a status other than pending.");
        }

        scheduleSaved.setStatus(ScheduleRequestStatus.DENIED);
        scheduleRepository.save(scheduleSaved);
    }

    @Transactional
    public void delete(Long idSchedule) {
        scheduleRepository.findById(idSchedule).orElseThrow();
        scheduleRepository.deleteById(idSchedule);
    }

    private Schedule updateScheduleEntity(Schedule scheduleSaved, SchedulePutRequestDTO requestDTO) {
        scheduleSaved.setShiftSchedule(requestDTO.getShiftSchedule());
        scheduleSaved.setDay(requestDTO.getDay());
        return scheduleSaved;
    }
}
