package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.ReservedTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservedTableRepository extends JpaRepository<ReservedTables, Long> {
    boolean existsByTableIdAndShiftScheduleAndDay(Long tableId, String shiftSchedule, String day);
}
