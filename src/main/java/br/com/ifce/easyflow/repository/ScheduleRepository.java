package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
