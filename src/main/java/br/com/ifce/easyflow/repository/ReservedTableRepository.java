package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.ReservedTables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedTableRepository extends JpaRepository<ReservedTables, Long> {
    boolean existsByTableIdAndShiftScheduleAndDay(Long tableId, String shiftSchedule, String day);

    List<ReservedTables> findByShiftScheduleAndDay(String shiftSchedule, String day);

    void deleteByShiftScheduleAndDayAndTableId(String shiftSchedule, String day, Long tableId);
}
