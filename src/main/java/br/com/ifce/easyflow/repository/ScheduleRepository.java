package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<List<Schedule>> findByPersonId(Long personId);

    List<Schedule> findByShiftSchedule(String shiftSchedule);

    List<Schedule> findByDay(String day);
}
