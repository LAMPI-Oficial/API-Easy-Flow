package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.solicitation.ApprovedSolicitationDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.Solicitation;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import br.com.ifce.easyflow.model.enums.SolicitationStatus;
import br.com.ifce.easyflow.repository.EquipmentRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.SolicitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SolicitationService {

    private final SolicitationRepository solicitationRepository;
    private final EquipmentRepository equipmentRepository;
    private final PersonRepository personRepository;

    public Page<Solicitation> findAll(Pageable pageable) {
        return solicitationRepository.findAll(pageable);
    }

    public Solicitation findById(Long id) {
        return solicitationRepository.findById(id)
                .orElseThrow();
        // TODO: Throws a exception NotFound;
    }

    public Page<Solicitation> findByPersonId(Long personId, Pageable pageable) {
        boolean personExists = personRepository.existsById(personId);
        if (!personExists) {
            throw new RuntimeException("Person not fond with id: " + personId);
            // TODO: Throw a more specific exception, e.g. NotFoundException
        }
        return solicitationRepository.findAllByPersonId(personId, pageable);
    }

    public Page<Solicitation> findByEquipmentId(Long equipmentId, Pageable pageable) {
        boolean equipmentExists = equipmentRepository.existsById(equipmentId);
        if (!equipmentExists) {
            throw new RuntimeException("Equipment not fond with id: " + equipmentId);
            // TODO: Throw a more specific exception, e.g. NotFoundException
        }
        return solicitationRepository.findAllByEquipmentId(equipmentId, pageable);
    }

    public Page<Solicitation> findAllByStartDate(String startDate, Pageable pageable) {
        LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        return solicitationRepository.findAllByStartDate(date, pageable);
    }

    public Page<Solicitation> findAllByEndDate(String endDate, Pageable pageable) {
        LocalDate date = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        return solicitationRepository.findAllByEndDate(date, pageable);
    }

    @Transactional
    public Solicitation save(SolicitationPostRequestDTO requestDTO) {

        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(); // TODO: Throw a more specific exception, e.g. NotFoundException


        Solicitation newSolicitation = Solicitation.builder()
                .justification(requestDTO.getJustification())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .person(person)
                .status(SolicitationStatus.PENDING)
                .build();

        return solicitationRepository.save(newSolicitation);
    }

    @Transactional
    public Solicitation update(Long id, SolicitationPutRequestDTO requestDTO) {
        Solicitation solicitationSaved = this.findById(id);

        Solicitation solicitationUpdated = updateSolicitationEntity(solicitationSaved, requestDTO);

        return solicitationRepository.save(solicitationUpdated);
    }

    @Transactional
    public Solicitation approvedSolicitation(Long id, ApprovedSolicitationDTO requestDTO) {

        Solicitation solicitationSaved = this.findById(id);

        Equipment equipment = equipmentRepository.findById(requestDTO.getEquipmentId())
                .orElseThrow(); // TODO: Throw a more specific exception, e.g. NotFoundException


        if (!solicitationSaved.getStatus().equals(SolicitationStatus.PENDING)) {
            throw new RuntimeException("The request must be pending to be approved.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        if (!equipment.getEquipmentStatus().equals(EquipmentAvailabilityStatus.AVAILABLE)) {
            throw new RuntimeException("Equipment must be available to be attached to a request.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        solicitationSaved.setStatus(SolicitationStatus.APPROVED);

        solicitationSaved.setEquipment(equipment);
        equipment.setEquipmentStatus(EquipmentAvailabilityStatus.BUSY);

        equipmentRepository.save(equipment);

        return solicitationRepository.save(solicitationSaved);

    }

    @Transactional
    public void denySolicitation(Long id) {

        Solicitation solicitation = this.findById(id);
        if (solicitation.getStatus().equals(SolicitationStatus.PENDING)) {
            solicitation.setStatus(SolicitationStatus.DENIED);
            solicitationRepository.save(solicitation);
        }
        throw new RuntimeException("Request must be pending to disapprove it");  // TODO: Throw a more specific exception, e.g. BadRequest
    }

    @Transactional
    public void delete(Long id) {

        Solicitation solicitation = this.findById(id);
        Long equipmentId = solicitation.getEquipment().getId();

        if (equipmentId != null) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow();
            // TODO: Throw a more specific exception, e.g. NotFoundException

            equipment.setEquipmentStatus(EquipmentAvailabilityStatus.AVAILABLE);

            equipmentRepository.save(equipment);

        }
        solicitationRepository.deleteById(id);
    }

    private Solicitation updateSolicitationEntity(Solicitation solicitationSaved,
                                                  SolicitationPutRequestDTO requestDTO) {

        solicitationSaved.setJustification(requestDTO.getJustification());
        solicitationSaved.setStartDate(requestDTO.getStartDate());
        solicitationSaved.setEndDate(requestDTO.getEndDate());
        return solicitationSaved;

    }
}
