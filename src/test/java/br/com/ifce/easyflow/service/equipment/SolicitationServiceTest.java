package br.com.ifce.easyflow.service.equipment;

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
import br.com.ifce.easyflow.service.SolicitationService;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitationServiceTest {

    @InjectMocks
    private SolicitationService solicitationService;

    @Mock
    private SolicitationRepository solicitationRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private PersonRepository personRepository;

    @Test
    void findAll_Return_PageOfEquipmentsSolicitations_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());

        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        when(solicitationRepository.findAll(pageable)).thenReturn(solicitationPage);

        Page<Solicitation> solicitationsPageResponse = solicitationService.findAll(pageable);
        List<Solicitation> solicitationsSavedList = solicitationsPageResponse.stream().toList();


        Assertions.assertFalse(solicitationsSavedList.isEmpty());
        Assertions.assertEquals(solicitations.get(0).getId(), solicitationsSavedList.get(0).getId());
        Assertions.assertEquals(solicitations.get(0).getEquipment(), solicitationsSavedList.get(0).getEquipment());
        Assertions.assertEquals(solicitations.get(0).getStartDate(), solicitationsSavedList.get(0).getStartDate());
        Assertions.assertEquals(solicitations.get(0).getEndDate(), solicitationsSavedList.get(0).getEndDate());
        Assertions.assertEquals(solicitations.get(0).getPerson(), solicitationsSavedList.get(0).getPerson());

        verify(solicitationRepository).findAll(pageable);
        verifyNoMoreInteractions(solicitationRepository);
    }

    @Test
    void findById_Return_EquipmentSolicitation_WhenSuccessful() {
        Solicitation solicitation = createSolicitation();

        when(solicitationRepository.findById(anyLong())).thenReturn(Optional.of(solicitation));

        Solicitation solicitationSaved = solicitationService.findById(1L);

        Assertions.assertNotNull(solicitationSaved.getId());
        Assertions.assertEquals(solicitation.getId(), solicitationSaved.getId());
        Assertions.assertEquals(solicitation.getEquipment(), solicitationSaved.getEquipment());
        Assertions.assertEquals(solicitation.getStartDate(), solicitationSaved.getStartDate());
        Assertions.assertEquals(solicitation.getEndDate(), solicitationSaved.getEndDate());
        Assertions.assertEquals(solicitation.getPerson(), solicitationSaved.getPerson());

        verify(solicitationRepository).findById(1L);
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findById_Throws_ResourceNotFountException_WhenEquipmentSolicitation_NotFound() {

        when(solicitationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> solicitationService.findById(1L));

        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("No request was found with the given id."));

        verify(solicitationRepository).findById(1L);
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findByPersonId_Return_PageOfEquipmentsSolicitations_ByPersonId_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());

        Person personSaved = createPerson();

        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        when(solicitationRepository.findAllByPersonId(1L, pageable)).thenReturn(solicitationPage);
        when(personRepository.existsById(1L)).thenReturn(true);

        Page<Solicitation> solicitationsPageResponse = solicitationService.findByPersonId(1L, pageable);
        List<Solicitation> solicitationsSavedList = solicitationsPageResponse.stream().toList();


        Assertions.assertFalse(solicitationsSavedList.isEmpty());
        Assertions.assertEquals(solicitations.get(0).getId(), solicitationsSavedList.get(0).getId());
        Assertions.assertEquals(solicitations.get(0).getEquipment(), solicitationsSavedList.get(0).getEquipment());
        Assertions.assertEquals(solicitations.get(0).getStartDate(), solicitationsSavedList.get(0).getStartDate());
        Assertions.assertEquals(solicitations.get(0).getEndDate(), solicitationsSavedList.get(0).getEndDate());

        Assertions.assertEquals(personSaved.getEmail(), solicitationsSavedList.get(0).getPerson().getEmail());
        Assertions.assertEquals(personSaved.getName(), solicitationsSavedList.get(0).getPerson().getName());
        Assertions.assertEquals(personSaved.getId(), solicitationsSavedList.get(0).getPerson().getId());

        verify(solicitationRepository).findAllByPersonId(1L, pageable);
        verify(personRepository).existsById(1L);
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findByPersonId_Throws_PersonNotFoundException_WhenPersonNotFound() {
        PageRequest pageable = PageRequest.of(0, 5);

        when(personRepository.existsById(anyLong())).thenReturn(false);

        PersonNotFoundException personNotFoundException = Assertions
                .assertThrows(PersonNotFoundException.class,
                        () -> solicitationService.findByPersonId(1L, pageable));

        Assertions.assertTrue(personNotFoundException
                .getMessage()
                .contains("The person was not found in the database, please check the registered persons."));

        verify(personRepository).existsById(1L);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(solicitationRepository);
    }

    @Test
    void findByEquipmentId_Return_PageOfEquipmentsSolicitations_ByEquipmentId_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());
        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        when(equipmentRepository.existsById(anyLong())).thenReturn(true);
        when(solicitationRepository.findAllByEquipmentId(anyLong(), any(Pageable.class))).thenReturn(solicitationPage);

        Page<Solicitation> solicitationsSavedPage = solicitationService.findByEquipmentId(1L, pageable);
        List<Solicitation> solicitationList = solicitationsSavedPage.stream().toList();

        Assertions.assertEquals(solicitations.get(0).getEquipment(), solicitationList.get(0).getEquipment());
        Assertions.assertEquals(solicitations.get(0).getId(), solicitationList.get(0).getId());
        Assertions.assertEquals(solicitations.get(0).getStatus(), solicitationList.get(0).getStatus());
    }

    @Test
    void findByEquipmentId_Throws_ResourceNotFoundException_WhenEquipmentNotFound() {

        PageRequest pageable = PageRequest.of(0, 5);

        when(equipmentRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> solicitationService.findByEquipmentId(1L, pageable));

        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("The equipment was not found in the database, please check the registered equipment."));

        verify(equipmentRepository).existsById(1L);
        verifyNoMoreInteractions(equipmentRepository);
        verifyNoInteractions(solicitationRepository);
    }

    @Test
    void findAllByStartDate_Return_PageOfEquipmentsSolicitations_ByStartDate_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());

        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        when(solicitationRepository.findAllByStartDate(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(solicitationPage);

        String date = "2023-11-12";

        Page<Solicitation> solicitationsPageResponse = solicitationService.findAllByStartDate(date, pageable);
        List<Solicitation> solicitationsSavedList = solicitationsPageResponse.stream().toList();

        Assertions.assertFalse(solicitationsSavedList.isEmpty());
        Assertions.assertEquals(solicitations.get(0).getId(), solicitationsSavedList.get(0).getId());
        Assertions.assertEquals(solicitations.get(0).getEquipment(), solicitationsSavedList.get(0).getEquipment());
        Assertions.assertEquals(solicitations.get(0).getStartDate(), solicitationsSavedList.get(0).getStartDate());
        Assertions.assertEquals(solicitations.get(0).getEndDate(), solicitationsSavedList.get(0).getEndDate());
        Assertions.assertEquals(solicitations.get(0).getPerson(), solicitationsSavedList.get(0).getPerson());

        verify(solicitationRepository).findAllByStartDate(any(LocalDate.class), any(Pageable.class));
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findAllByStartDate_Throws_BadRequestException_ByStartDate_WhenDateIsInWrongFormat() {

        PageRequest pageable = PageRequest.of(0, 5);

        String date = "2023:11:12";

        BadRequestException badRequestException = Assertions
                .assertThrows(BadRequestException.class,
                        () -> solicitationService.findAllByStartDate(date, pageable));

        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("The date format does not conform to the format: yyyy-MM-dd. "));

        verifyNoInteractions(solicitationRepository);

    }

    @Test
    void findAllByEndDate_Return_PageOfEquipmentsSolicitations_ByEndDate_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());

        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        when(solicitationRepository.findAllByEndDate(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(solicitationPage);

        String date = "2023-11-16";

        Page<Solicitation> solicitationsPageResponse = solicitationService.findAllByEndDate(date, pageable);
        List<Solicitation> solicitationsSavedList = solicitationsPageResponse.stream().toList();

        Assertions.assertFalse(solicitationsSavedList.isEmpty());
        Assertions.assertEquals(solicitations.get(0).getId(), solicitationsSavedList.get(0).getId());
        Assertions.assertEquals(solicitations.get(0).getEquipment(), solicitationsSavedList.get(0).getEquipment());
        Assertions.assertEquals(solicitations.get(0).getStartDate(), solicitationsSavedList.get(0).getStartDate());
        Assertions.assertEquals(solicitations.get(0).getEndDate(), solicitationsSavedList.get(0).getEndDate());
        Assertions.assertEquals(solicitations.get(0).getPerson(), solicitationsSavedList.get(0).getPerson());

        verify(solicitationRepository).findAllByEndDate(any(LocalDate.class), any(Pageable.class));
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findAllByEndDate_Throws_BadRequestException_ByEndDate_WhenDateIsInWrongFormat() {

        PageRequest pageable = PageRequest.of(0, 5);

        String date = "23-11-12";

        BadRequestException badRequestException = Assertions
                .assertThrows(BadRequestException.class,
                        () -> solicitationService.findAllByEndDate(date, pageable));

        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("The date format does not conform to the format: yyyy-MM-dd. "));

        verifyNoInteractions(solicitationRepository);

    }


    @Test
    void save_Return_EquipmentSolicitation_WhenSuccessful() {

        Person person = createPerson();
        Solicitation solicitation = createSolicitation();
        solicitation.setEquipment(null);

        SolicitationPostRequestDTO requestDTO = SolicitationPostRequestDTO.builder()
                .personId(1L)
                .justification("Justification")
                .startDate("2023-11-12")
                .endDate("2023-11-16")
                .build();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(solicitationRepository.save(any(Solicitation.class))).thenReturn(solicitation);

        Solicitation saved = solicitationService.save(requestDTO);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(requestDTO.getPersonId(), saved.getPerson().getId());
        Assertions.assertEquals(requestDTO.getStartDate(), saved.getStartDate().toString());
        Assertions.assertEquals(requestDTO.getEndDate(), saved.getEndDate().toString());

        verify(solicitationRepository, times(1)).save(any(Solicitation.class));
        verifyNoMoreInteractions(solicitationRepository);
        verify(personRepository).findById(anyLong());

    }

    @Test
    void save_Throw_PersonNotFondException_WhenPersonNotFound() {

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        SolicitationPostRequestDTO requestDTO = SolicitationPostRequestDTO.builder()
                .personId(1L)
                .justification("Justification")
                .startDate("23-11-12")
                .endDate("2023:11:16")
                .build();

        PersonNotFoundException personNotFoundException = Assertions.assertThrows(PersonNotFoundException.class,
                () -> solicitationService.save(requestDTO));


        Assertions.assertTrue(personNotFoundException
                .getMessage()
                .contains("The person was not found in the database, please check the registered persons."));

        verify(personRepository).findById(anyLong());
        verifyNoInteractions(solicitationRepository);

    }

    @Test
    void save_Throw_BadRequestException_WhenDateIsInWrongFormat() {

        Person person = createPerson();

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        SolicitationPostRequestDTO requestDTO = SolicitationPostRequestDTO.builder()
                .personId(1L)
                .justification("Justification")
                .startDate("23-11-12")
                .endDate("2023:11:16")
                .build();

        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> solicitationService.save(requestDTO));


        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("The date format does not conform to the format: yyyy-MM-dd. "));

        verifyNoInteractions(solicitationRepository);

    }

    @Test
    void update_Return_UpdatedEquipmentSolicitation_WhenSuccessful() {

        Solicitation solicitationUpdated = createSolicitation();
        Solicitation oldSolicitation = createSolicitation();

        SolicitationPutRequestDTO requestDTO = SolicitationPutRequestDTO.builder()
                .justification("Other Justification")
                .startDate("2023-11-13")
                .endDate("2023-11-16")
                .build();

        LocalDate endDate = LocalDate.parse(requestDTO.getEndDate());
        LocalDate startDate = LocalDate.parse(requestDTO.getStartDate());

        solicitationUpdated.setJustification(requestDTO.getJustification());
        solicitationUpdated.setEndDate(endDate);
        solicitationUpdated.setStartDate(startDate);

        when(solicitationRepository.save(any(Solicitation.class))).thenReturn(solicitationUpdated);
        when(solicitationRepository.findById(anyLong())).thenReturn(Optional.of(oldSolicitation));
        Solicitation update = solicitationService.update(1L, requestDTO);

        Assertions.assertNotNull(update.getId());
        Assertions.assertEquals(solicitationUpdated.getId(), update.getId());
        Assertions.assertEquals(requestDTO.getJustification(), update.getJustification());
        Assertions.assertEquals(endDate, update.getEndDate());
        Assertions.assertEquals(startDate, update.getStartDate());

    }

    @Test
    void update_Throw_BadRequestExceptionInUpdatedSolicitation_WhenDateIsInWrongFormat() {

        Solicitation solicitation = createSolicitation();

        SolicitationPutRequestDTO requestDTO = SolicitationPutRequestDTO.builder()
                .justification("Other Justification")
                .startDate("23-11-13")
                .endDate("2023-11-16")
                .build();

        when(solicitationRepository.findById(anyLong())).thenReturn(Optional.of(solicitation));

        BadRequestException badRequestException = Assertions
                .assertThrows(BadRequestException.class,
                        () -> solicitationService.update(1L, requestDTO));


        Assertions.assertTrue(badRequestException
                .getMessage()
                .contains("The date format does not conform to the format: yyyy-MM-dd. "));

    }

    @Test
    void update_Throw_ResourceNotFoundExceptionInUpdatedSolicitation_WhenSolicitationNotFound() {

        Solicitation solicitation = createSolicitation();

        SolicitationPutRequestDTO requestDTO = SolicitationPutRequestDTO.builder()
                .justification("Other Justification")
                .startDate("23-11-13")
                .endDate("2023-11-16")
                .build();

        when(solicitationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> solicitationService.update(1L, requestDTO));


        Assertions.assertTrue(resourceNotFoundException
                .getMessage()
                .contains("No request was found with the given id."));

    }
    @Test
    void approvedSolicitation() {

    }

    @Test
    void denySolicitation() {
    }

    @Test
    void delete() {
    }

    private Solicitation createSolicitation() {
        return Solicitation.builder()
                .id(1L)
                .equipment(createEquipment())
                .startDate(LocalDate.parse("2023-11-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .endDate(LocalDate.parse("2023-11-16", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .justification("Justification")
                .status(SolicitationStatus.PENDING)
                .person(createPerson())
                .build();
    }

    private Equipment createEquipment() {
        return Equipment.builder()
                .id(1L)
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .equipmentStatus(EquipmentAvailabilityStatus.AVAILABLE)
                .build();
    }

    private Person createPerson() {
        Person person = new Person();
        person.setEmail("person@email.com");
        person.setName("person");
        person.setId(1L);
        person.setPhone("859565");
        return person;
    }
}