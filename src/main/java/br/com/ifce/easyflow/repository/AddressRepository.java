package br.com.ifce.easyflow.repository;

import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findByPersonId(Long address_id);
}