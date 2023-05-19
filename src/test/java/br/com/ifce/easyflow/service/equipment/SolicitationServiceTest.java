package br.com.ifce.easyflow.service.equipment;

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

    }

    @Test
    void findAllByStartDate_Return_PageOfEquipmentsSolicitations_ByEquipmentId_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);
        List<Solicitation> solicitations = List.of(createSolicitation());

        PageImpl<Solicitation> solicitationPage = new PageImpl<>(solicitations);

        LocalDate localDate = LocalDate.parse("2023-11-12", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        when(solicitationRepository.findAllByStartDate(localDate, pageable))
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

        verify(solicitationRepository).findAllByStartDate(localDate, pageable);
        verifyNoMoreInteractions(solicitationRepository);

    }

    @Test
    void findAllByEndDate() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
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