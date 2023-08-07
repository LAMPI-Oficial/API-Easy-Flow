package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.claim.ClaimRequestDTO;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import br.com.ifce.easyflow.model.Claim;
import br.com.ifce.easyflow.repository.ClaimRepository;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final PersonService personService;

    @Autowired
    public ClaimService(ClaimRepository claimRepository, PersonService personService) {
        this.claimRepository = claimRepository;
        this.personService = personService;
    }

    @Transactional
    public Claim save(Claim claim) {
        return this.claimRepository.save(claim);
    }

    public Claim saveClaim(Long id, ClaimRequestDTO request) {
        Person person = personService.findById(id);

        Claim claim = Claim.builder()
                .user_name(person.getName())
                .user_email(person.getEmail())
                .criationDate(LocalDate.now())
                .descrition(request.getClaim_descrition())
                .build();

        return claimRepository.save(claim);

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

    public Claim searchByID(Long id) {
        return this.claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No claim with given id was found"));
    }

    @Transactional
    public Claim update(Long id, Claim newClaim) {
        Claim oldClaim = this.searchByID(id);

        return this.save(this.fillUpdateClaim(oldClaim, newClaim));
    }

    private Claim fillUpdateClaim(Claim oldClaim, Claim newClaim) {
        oldClaim.setUser_name(newClaim.getUser_name());
        oldClaim.setUser_email(newClaim.getUser_email());
        oldClaim.setDescrition(newClaim.getDescrition());

        return oldClaim;
    }

}
