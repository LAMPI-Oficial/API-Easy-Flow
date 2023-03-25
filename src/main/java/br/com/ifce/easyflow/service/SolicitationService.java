package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.SolicitationPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.UpdateSolicitationStatusDTO;
import br.com.ifce.easyflow.controller.dto.solicitation.addDeviceToSolicitationDTO;
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
        return solicitationRepository.findAllByPerson(personId, pageable);
    }

    public Page<Solicitation> findByEquipmentId(Long equipmentId, Pageable pageable) {
        boolean equipmentExists = equipmentRepository.existsById(equipmentId);
        if (!equipmentExists) {
            throw new RuntimeException("Equipment not fond with id: " + equipmentId);
            // TODO: Throw a more specific exception, e.g. NotFoundException
        }
        return solicitationRepository.findAllByEquipment(equipmentId, pageable);
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
    public Solicitation updateStatus(Long id, UpdateSolicitationStatusDTO requestDTO) {

        Solicitation solicitationSaved = this.findById(id);

        solicitationSaved.setStatus(requestDTO.getStatus());

        return solicitationRepository.save(solicitationSaved);

    }

    @Transactional
    public Solicitation addDevice(Long id, addDeviceToSolicitationDTO requestDTO) {
        Solicitation solicitationSaved = this.findById(id);

        Equipment equipment = equipmentRepository.findById(requestDTO.getEquipmentId())
                .orElseThrow(); // TODO: Throw a more specific exception, e.g. NotFoundException


        if (solicitationSaved.getStatus().equals(SolicitationStatus.APPROVED)) {
            throw new RuntimeException("This request has already been approved.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        if (solicitationSaved.getStatus().equals(SolicitationStatus.DENIED)) {
            throw new RuntimeException("The request has already been denied.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        if (equipment.getEquipmentStatus().equals(EquipmentAvailabilityStatus.BUSY)) {
            throw new RuntimeException("The device is already busy.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        if (equipment.getEquipmentStatus().equals(EquipmentAvailabilityStatus.MAINTENANCE)) {
            throw new RuntimeException("The equipment is under maintenance.");
            // TODO: Throw a more specific exception, e.g. BadRequest
        }

        solicitationSaved.setEquipment(equipment);
        return solicitationRepository.save(solicitationSaved);

    }

    @Transactional
    public void delete(Long id) {
        if (!solicitationRepository.existsById(id)) {
            throw new RuntimeException("Solicitation not fond with id: " + id);
            // TODO: Throw a more specific exception, e.g. NotFoundException
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
