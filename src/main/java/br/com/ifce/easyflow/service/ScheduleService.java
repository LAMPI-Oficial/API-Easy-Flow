package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.schedule.SchedulePostRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.SchedulePutRequestDTO;
import br.com.ifce.easyflow.controller.dto.schedule.ScheduleResponseDTO;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.Schedule;
import br.com.ifce.easyflow.repository.PersonRepository;
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
                .tableNumber(requestDTO.getTableNumber())
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
    public void delete(Long idSchedule) {
        scheduleRepository.findById(idSchedule).orElseThrow();
        scheduleRepository.deleteById(idSchedule);
    }

    private Schedule updateScheduleEntity(Schedule scheduleSaved, SchedulePutRequestDTO requestDTO) {
        scheduleSaved.setShiftSchedule(requestDTO.getShiftSchedule());
        scheduleSaved.setDay(requestDTO.getDay());
        scheduleSaved.setTableNumber(requestDTO.getTableNumber());
        return scheduleSaved;
    }
}
