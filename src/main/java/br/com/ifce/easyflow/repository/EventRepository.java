package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM Event e WHERE e.date = :date")
    Page<Event> findByDate(@Param("date") LocalDate date, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.date = :date AND e.time = :time")
    Page<Event> findByDateTime(@Param("date") LocalDate date, @Param("time") LocalTime time, Pageable pageable);
}
