package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.LabTable;
import br.com.ifce.easyflow.model.ReservedTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservedTableRepository extends JpaRepository<ReservedTables, Long> {
//    List<ReservedTables> findAllByLabTable(Long id);

 //   @Query(value = "SELECT d FROM ReservedTable d WHERE d. = ?1 AND d.date = ?2")
//    boolean existsByLabTableAndShiftScheduleAndDay(LabTable labTable, String shiftSchedule, String day);
}
