package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.StudyArea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyAreaRepository extends JpaRepository<StudyArea,Long> {

    Optional<StudyArea> findByName(String Study_area_name);
}
