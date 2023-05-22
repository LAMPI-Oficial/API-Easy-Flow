package br.com.ifce.easyflow.service.address;

import br.com.ifce.easyflow.controller.dto.address.AddressRequestDTO;
import br.com.ifce.easyflow.controller.dto.address.AddressUpdateDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.Course;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.model.enums.StateEnum;
import br.com.ifce.easyflow.repository.AddressRepository;
import br.com.ifce.easyflow.service.AddressService;
import br.com.ifce.easyflow.service.PersonService;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PersonService personService;

    private StateEnum stateEnum;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Address_WhenSuccessful() {
        Address address = createAddress();

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address savedAddress = addressService.save(address);

        Assertions.assertEquals(address, savedAddress);

        verify(addressRepository).save(address);
    }

    @Test
    void findByPersonId_ExistingAddress_ReturnsTrue() {
        when(addressRepository.findByPersonId(anyLong())).thenReturn(Optional.of(createAddress()));

        boolean exists = addressService.findByPersonId(1L);

        Assertions.assertTrue(exists);

        verify(addressRepository).findByPersonId(1L);
    }

    @Test
    void findByPersonId_NonExistingAddress_ReturnsFalse() {
        when(addressRepository.findByPersonId(anyLong())).thenReturn(Optional.empty());

        boolean exists = addressService.findByPersonId(1L);

        Assertions.assertFalse(exists);

        verify(addressRepository).findByPersonId(1L);
    }

    @Test
    void search_ReturnsListOfAddresses() {
        List<Address> addresses = List.of(createAddress());

        when(addressRepository.findAll()).thenReturn(addresses);

        List<Address> result = addressService.search();

        Assertions.assertEquals(addresses, result);

        verify(addressRepository).findAll();
    }

    @Test
    void searchByID_ExistingAddress_ReturnsAddress() {
        Address address = createAddress();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        Address result = addressService.searchByID(1L);

        Assertions.assertEquals(address, result);

        verify(addressRepository).findById(1L);
    }

    @Test
    void searchByID_NonExistingAddress_ThrowsException() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.searchByID(1L);
        });

        verify(addressRepository).findById(1L);
    }

    @Test
    void update_ExistingAddress_ReturnsUpdatedAddress() {
        Address address = createAddress();
        AddressUpdateDTO updateDTO = createAddressUpdateDTO();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address updatedAddress = addressService.update(1L, updateDTO);

        Assertions.assertEquals(updateDTO.getComplement(), updatedAddress.getComplement());
        Assertions.assertEquals(updateDTO.getMunicipality(), updatedAddress.getMunicipality());
        Assertions.assertEquals(updateDTO.getNeighborhood(), updatedAddress.getNeighborhood());
        Assertions.assertEquals(updateDTO.getNumber(), updatedAddress.getNumber());
        Assertions.assertEquals(updateDTO.getStateEnum(), updatedAddress.getStateEnum());
        Assertions.assertEquals(updateDTO.getStreet(), updatedAddress.getStreet());

        verify(addressRepository).findById(1L);
        verify(addressRepository).save(address);
    }

    @Test
    void delete_ExistingAddress_ReturnsTrue() {
        Address address = createAddress();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        boolean deleted = addressService.delete(1L);

        Assertions.assertTrue(deleted);

        verify(addressRepository).findById(1L);
        verify(addressRepository).delete(address);
    }


    @Test
    void existsByID_ExistingAddress_ReturnsTrue() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(createAddress()));

        boolean exists = addressService.existsByID(1L);

        Assertions.assertTrue(exists);

        verify(addressRepository).findById(1L);
    }

    @Test
    void existsByID_NonExistingAddress_ReturnsFalse() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean exists = addressService.existsByID(1L);

        Assertions.assertFalse(exists);

        verify(addressRepository).findById(1L);
    }

    @Test
    void createAddress_ExistingPersonId_ThrowsConflictException() {
        AddressRequestDTO addressRequestDTO = createAddressRequestDTO();
        when(addressRepository.findByPersonId(anyLong())).thenReturn(Optional.of(createAddress()));

        Assertions.assertThrows(ConflictException.class, () -> {
            addressService.createAddress(addressRequestDTO);
        });

        verify(addressRepository).findByPersonId(addressRequestDTO.getPerson_id());
        verify(personService, never()).findById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
    }

    private Address createAddress() {
        Address address = new Address();
        address.setComplement("Complement");
        address.setMunicipality("Municipality");
        address.setNeighborhood("Neighborhood");
        address.setNumber("123");
        address.setStateEnum(stateEnum.CEARA);
        address.setStreet("Street");
        address.setPerson(createPerson());
        return address;
    }

    private AddressRequestDTO createAddressRequestDTO() {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setComplement("Complement");
        addressRequestDTO.setMunicipality("Municipality");
        addressRequestDTO.setNeighborhood("Neighborhood");
        addressRequestDTO.setNumber("123");
        addressRequestDTO.setStateEnum(stateEnum.CEARA);
        addressRequestDTO.setStreet("Street");
        addressRequestDTO.setPerson_id(1L);
        return addressRequestDTO;
    }

    private AddressUpdateDTO createAddressUpdateDTO() {
        AddressUpdateDTO addressUpdateDTO = new AddressUpdateDTO();
        addressUpdateDTO.setComplement("Updated Complement");
        addressUpdateDTO.setMunicipality("Updated Municipality");
        addressUpdateDTO.setNeighborhood("Updated Neighborhood");
        addressUpdateDTO.setNumber("456");
        addressUpdateDTO.setStateEnum(stateEnum.ACRE);
        addressUpdateDTO.setStreet("Updated Street");
        return addressUpdateDTO;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setName("Marcola");
        person.setEmail("marcos@test.com.br");
        person.setCourse(createCourse());
        person.setStudy_area(createStudyArea());
        return person;
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Computer Science");
        return course;
    }

    private StudyArea createStudyArea() {
        StudyArea studyArea = new StudyArea();
        studyArea.setId(1L);
        studyArea.setName("Software Engineering");
        return studyArea;
    }
}
