package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
