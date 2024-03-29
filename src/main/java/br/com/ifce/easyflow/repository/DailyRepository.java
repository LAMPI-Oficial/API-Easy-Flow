package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Daily;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DailyRepository extends JpaRepository<Daily, Long> {

    public Page<Daily> findByPersonId(Long id, Pageable pageable);

    @Query(value = "SELECT d FROM Daily d WHERE d.person.id = ?1 AND d.date = ?2")
    public Page<Daily> findByPersonIdAndDate(Long id, LocalDate date, Pageable pageable);

    public Page<Daily> findByDate(LocalDate localDate, Pageable pageable);
}
