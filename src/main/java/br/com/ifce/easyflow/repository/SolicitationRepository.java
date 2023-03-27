package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Solicitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface SolicitationRepository extends JpaRepository<Solicitation, Long> {
    Page<Solicitation> findAllByPersonId(Long personId, Pageable pageable);

    Page<Solicitation> findAllByEquipmentId(Long equipmentId, Pageable pageable);

    Page<Solicitation> findAllByStartDate(LocalDate date, Pageable pageable);

    Page<Solicitation> findAllByEndDate(LocalDate date, Pageable pageable);
}
