package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Daily;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailyRepository extends JpaRepository<Daily, Long> {

//    @Query(
//            "SELECT Daily FROM Daily WHERE Daily.person_id = :userId"
//    )
//   public Page<Daily> findDailyByUserId(Long userId, Pageable pageable);
}
