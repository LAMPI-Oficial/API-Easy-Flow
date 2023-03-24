package br.com.ifce.easyflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import br.com.ifce.easyflow.model.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Optional<Announcement> findByTitle(String announcement_name);

    @Override
    Optional<Announcement> findById(Long id);
}
