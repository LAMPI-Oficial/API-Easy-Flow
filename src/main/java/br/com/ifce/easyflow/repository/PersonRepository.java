package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String person_email);

    Optional<Person> existsByEmail(String person_email);
}
