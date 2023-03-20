package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Daily;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DailyRepository extends JpaRepository<Daily, Long> {
    @Query(
            value = "SELECT * FROM daily"
    )
    Page<Daily> findByUserId();
}
