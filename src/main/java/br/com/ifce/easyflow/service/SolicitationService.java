package br.com.ifce.easyflow.service.daily;

import br.com.ifce.easyflow.controller.dto.solicitation.ApprovedSolicitationDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.Solicitation;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import br.com.ifce.easyflow.model.enums.SolicitationStatus;
import br.com.ifce.easyflow.repository.EquipmentRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.repository.SolicitationRepository;
//import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.daily.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.daily.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

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
                .orElseThrow(() -> new ResourceNotFoundException("No request was found with the given id."));
    }

    public Page<Solicitation> findByPersonId(Long personId, Pageable pageable) {
        boolean personExists = personRepository.existsById(personId);
        if (!personExists) {
            throw new PersonNotFoundException();
        }
        return solicitationRepository.findAllByPersonId(personId, pageable);
    }

    public Page<Solicitation> findByEquipmentId(Long equipmentId, Pageable pageable) {
        boolean equipmentExists = equipmentRepository.existsById(equipmentId);
        if (!equipmentExists) {
            throw new ResourceNotFoundException("The equipment was not found in the database," +
                    " please check the registered equipment.");
        }
        return solicitationRepository.findAllByEquipmentId(equipmentId, pageable);
    }

    public Page<Solicitation> findAllByStartDate(String startDate, Pageable pageable) {
        try {

            LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return solicitationRepository.findAllByStartDate(date, pageable);

        } catch (DateTimeParseException e) {
            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd. " + e.getMessage());
        }
    }

    public Page<Solicitation> findAllByEndDate(String endDate, Pageable pageable) {
        try {
            LocalDate date = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
            return solicitationRepository.findAllByEndDate(date, pageable);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd. " + e.getMessage());
        }
    }

    @Transactional
    public Solicitation save(SolicitationPostRequestDTO requestDTO) {

        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(PersonNotFoundException::new);

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

        Equipment equipment = equipmentRepository
                .findById(requestDTO.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("The equipment was not found in the database," +
                        " please check the registered equipment."));


        if (!solicitationSaved.getStatus().equals(SolicitationStatus.PENDING)) {
            throw new BadRequestException("The request must be pending to be approved.");
        }

        if (!equipment.getEquipmentStatus().equals(EquipmentAvailabilityStatus.AVAILABLE)) {
            throw new BadRequestException("Equipment must be available to be attached to a request.");
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
        throw new BadRequestException("Request must be pending to disapprove it");
    }

    @Transactional
    public void delete(Long id) {

        Solicitation solicitation = this.findById(id);
        Long equipmentId = solicitation.getEquipment().getId();

        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);

        if (equipment.isPresent()) {

            equipment.get().setEquipmentStatus(EquipmentAvailabilityStatus.AVAILABLE);
            equipmentRepository.save(equipment.get());
            solicitationRepository.deleteById(id);

        } else {
            solicitationRepository.deleteById(id);
        }
    }

    private Solicitation updateSolicitationEntity(Solicitation solicitationSaved,
                                                  SolicitationPutRequestDTO requestDTO) {

        solicitationSaved.setJustification(requestDTO.getJustification());
        solicitationSaved.setStartDate(requestDTO.getStartDate());
        solicitationSaved.setEndDate(requestDTO.getEndDate());
        return solicitationSaved;

    }
}
