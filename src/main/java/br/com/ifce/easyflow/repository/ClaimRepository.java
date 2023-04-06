package br.com.ifce.easyflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import br.com.ifce.easyflow.model.Claim;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    @Override
    Optional<Claim> findById(Long id);

}
