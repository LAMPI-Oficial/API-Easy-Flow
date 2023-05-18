package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
