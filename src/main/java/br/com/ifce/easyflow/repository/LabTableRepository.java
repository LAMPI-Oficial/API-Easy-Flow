package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.LabTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabTableRepository extends JpaRepository<LabTable, Long> {
    boolean existsByNumber(Long number);
}
