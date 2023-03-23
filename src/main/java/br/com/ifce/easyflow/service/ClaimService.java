package br.com.ifce.easyflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import br.com.ifce.easyflow.model.Claim;
import br.com.ifce.easyflow.repository.ClaimRepository;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Transactional
    public Claim save(Claim claim) {
        return this.claimRepository.save(claim);
    }

    public List<Claim> search() {
        return this.claimRepository.findAll();
    }

    @Transactional
    public Boolean delete(Long id) {
        Optional<Claim> claim = this.claimRepository.findById(id);

        if (claim.isPresent()) {
            this.claimRepository.delete(claim.get());
            return true;
        }

        return false;
    }

    public Optional<Claim> searchByID(Long id) {
        return this.claimRepository.findById(id);
    }

    @Transactional
    public Optional<Claim> update(Claim newClaim) {
        Optional<Claim> oldClaim = this.searchByID(newClaim.getId());

        return oldClaim.isPresent()
                ? Optional.of(this.save(this.fillUpdateClaim(oldClaim.get(), newClaim)))
                : Optional.empty();
    }

    private Claim fillUpdateClaim(Claim oldClaim, Claim newClaim) {
        oldClaim.setUser_name(newClaim.getUser_name());
        oldClaim.setUser_email(newClaim.getUser_email());
        oldClaim.setDescrition(newClaim.getDescrition());

        return oldClaim;
    }

}
