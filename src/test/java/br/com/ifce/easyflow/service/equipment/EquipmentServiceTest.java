package br.com.ifce.easyflow.service.equipment;

import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentStatusPatchRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import br.com.ifce.easyflow.repository.EquipmentRepository;
import br.com.ifce.easyflow.service.EquipmentService;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class EquipmentServiceTest {

    @InjectMocks
    private EquipmentService equipmentService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_Equipments_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Equipment> equipmentList = List.of(createEquipment());
        PageImpl<Equipment> equipmentPage = new PageImpl<>(equipmentList);

        when(equipmentRepository.findAll(pageable)).thenReturn(equipmentPage);

        Page<Equipment> all = equipmentService.findAll(pageable);
        List<Equipment> equipmentsSavedList = all.stream().toList();


        Assertions.assertFalse(equipmentsSavedList.isEmpty());
        Assertions.assertEquals(equipmentList.get(0).getName(), equipmentsSavedList.get(0).getName());
        Assertions.assertNotNull(equipmentsSavedList.get(0).getId());

        verify(equipmentRepository).findAll(pageable);

    }

    @Test
    void findAll_Equipments_Returns_EmptyPage_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);
        List<Equipment> equipmentList = new ArrayList<>();
        PageImpl<Equipment> equipmentPage = new PageImpl<>(equipmentList);

        when(equipmentRepository.findAll(pageable)).thenReturn(equipmentPage);

        Page<Equipment> all = equipmentService.findAll(pageable);

        Assertions.assertTrue(all.isEmpty());
        verify(equipmentRepository).findAll(pageable);
        verifyNoMoreInteractions(equipmentRepository);
    }

    @Test
    void findById_Return_Equipment_WhenSuccessful() {
        Equipment equipment = createEquipment();

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipment));

        Equipment equipmentSaved = equipmentService.findById(1L);

        Assertions.assertEquals(equipment.getName(), equipmentSaved.getName());
        Assertions.assertEquals(1L, equipmentSaved.getId());

    }

    @Test
    void findById_Throws_ResourceNotFoundException_WhenEquipmentNotFound() {

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> equipmentService.findById(1L));

        Assertions.assertTrue(resourceNotFoundException.getMessage()
                .contains("No equipment was found with the provided id, check the registered equipment."));

    }

    @Test
    void save_Return_Equipment_WhenSuccessful() {
        Equipment equipment = createEquipment();

        EquipmentPostRequestDTO equipmentRequestDTO = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        Equipment equipmentSaved = equipmentService.save(equipmentRequestDTO);

        Assertions.assertNotNull(equipmentSaved.getId());
        Assertions.assertEquals(equipment.getId(), equipmentSaved.getId());
        Assertions.assertEquals(equipment.getName(), equipmentSaved.getName());
        Assertions.assertEquals(equipment.getTombo(), equipmentSaved.getTombo());
        Assertions.assertEquals(equipment.getProcessor(), equipmentSaved.getProcessor());
        Assertions.assertEquals(equipment.getBrand(), equipmentSaved.getBrand());
        Assertions.assertEquals(equipment.getRamMemory(), equipmentSaved.getRamMemory());
        Assertions.assertEquals(equipment.getStorageMemory(), equipmentSaved.getStorageMemory());

        Equipment equipmentTwo = createEquipment();
        equipmentTwo.setId(null);

        verify(equipmentRepository).save(equipmentTwo);
        verifyNoMoreInteractions(equipmentRepository);
    }

    @Test
    void update_Return_Equipment_WhenSuccessful() {
        Equipment equipmentToUpdated = createEquipment();

        EquipmentPutRequestDTO equipmentPutRequest = EquipmentPutRequestDTO.builder()
                .name("Aspire-5")
                .brand("ACER")
                .processor("Intel i3")
                .ramMemory("16 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        equipmentToUpdated.setName("Aspire-5");
        equipmentToUpdated.setProcessor("Intel i3");
        equipmentToUpdated.setRamMemory("16 gb");

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipmentToUpdated);
        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipmentToUpdated));

        Equipment equipmentUpdated = equipmentService.update(1L, equipmentPutRequest);

        Assertions.assertEquals(equipmentToUpdated.getId(), equipmentUpdated.getId());
        Assertions.assertEquals(equipmentToUpdated.getBrand(), equipmentUpdated.getBrand());
        Assertions.assertEquals(equipmentToUpdated.getName(), equipmentUpdated.getName());
        Assertions.assertEquals(equipmentToUpdated.getRamMemory(), equipmentUpdated.getRamMemory());

        verify(equipmentRepository).findById(1L);
        verify(equipmentRepository).save(equipmentToUpdated);
        verifyNoMoreInteractions(equipmentRepository);
    }

    @Test
    void updateEquipmentStatus_Return_Equipment_WhenSuccessful() {
        Equipment equipment = createEquipment();

        EquipmentStatusPatchRequestDTO equipmentStatusPatchRequest =
                new EquipmentStatusPatchRequestDTO(EquipmentAvailabilityStatus.BUSY);


        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);


        Equipment equipmentUpdated = equipmentService.updateEquipmentStatus(1L, equipmentStatusPatchRequest);

        Assertions.assertEquals(equipment.getId(), equipmentUpdated.getId());
        Assertions.assertEquals(EquipmentAvailabilityStatus.BUSY, equipmentUpdated.getEquipmentStatus());

    }

    @Test
    void delete_Equipment_WhenSuccessful() {

        Equipment equipment = createEquipment();

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipment));
        doNothing().when(equipmentRepository).deleteById(anyLong());
        equipmentService.delete(equipment.getId());
        verify(equipmentRepository, times(1)).deleteById(equipment.getId());

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
}