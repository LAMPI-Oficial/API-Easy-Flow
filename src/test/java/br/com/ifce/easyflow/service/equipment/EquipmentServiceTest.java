package br.com.ifce.easyflow.service.equipment;

import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentStatusPatchRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import br.com.ifce.easyflow.service.EquipmentService;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EquipmentServiceTest {

    @Autowired
    private EquipmentService equipmentService;

    @Test
    void findAll_Equipments_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);

        EquipmentPostRequestDTO equipment1 = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        EquipmentPostRequestDTO equipment2 = EquipmentPostRequestDTO.builder()
                .name("ThinkPad - V14")
                .brand("Lenovo")
                .processor("Intel i5")
                .ramMemory("16 gb")
                .storageMemory("500 mb ssd")
                .tombo("254876")
                .build();

        equipmentService.save(equipment1);
        equipmentService.save(equipment2);

        Page<Equipment> equipmentsPage = equipmentService.findAll(pageable);
        List<Equipment> list = equipmentsPage.stream().toList();

        Assertions.assertEquals(equipment1.getName(), list.get(0).getName());
        Assertions.assertNotNull(list.get(0).getId());
        Assertions.assertEquals(2, list.size());

    }

    @Test
    void findAll_Equipments_Returns_EmptyPage_WhenSuccessful() {

        PageRequest pageable = PageRequest.of(0, 5);

        Page<Equipment> equipmentsPage = equipmentService.findAll(pageable);

        Assertions.assertTrue(equipmentsPage.isEmpty());
    }

    @Test
    void findById_Return_Equipment_WhenSuccessful() {
        EquipmentPostRequestDTO equipment = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        equipmentService.save(equipment);

        Equipment equipmentSaved = equipmentService.findById(1L);

        Assertions.assertEquals(equipment.getName(), equipmentSaved.getName());
        Assertions.assertEquals(1L, equipmentSaved.getId());

    }

    @Test
    void findById_Throws_ResourceNotFoundException_WhenEquipmentNotFound() {

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> equipmentService.findById(1L));

        Assertions.assertTrue(resourceNotFoundException.getMessage()
                .contains("No equipment was found with the provided id, check the registered equipment."));

    }

    @Test
    void save_Return_Equipment_WhenSuccessful() {
        EquipmentPostRequestDTO equipment = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        Equipment equipmentSaved = equipmentService.save(equipment);

        Assertions.assertNotNull(equipmentSaved.getId());
        Assertions.assertEquals(equipment.getName(), equipmentSaved.getName());
        Assertions.assertEquals(equipment.getTombo(), equipmentSaved.getTombo());
        Assertions.assertEquals(equipment.getProcessor(), equipmentSaved.getProcessor());
        Assertions.assertEquals(equipment.getBrand(), equipmentSaved.getBrand());
        Assertions.assertEquals(equipment.getRamMemory(), equipmentSaved.getRamMemory());
        Assertions.assertEquals(equipment.getStorageMemory(), equipmentSaved.getStorageMemory());

    }

    @Test
    void update_Return_Equipment_WhenSuccessful() {
        EquipmentPostRequestDTO equipment = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        EquipmentPutRequestDTO equipmentPutRequest = EquipmentPutRequestDTO.builder()
                .name("Aspire-5")
                .brand("ACER")
                .processor("Intel i3")
                .ramMemory("16 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        Equipment equipmentSaved = equipmentService.save(equipment);
        Equipment equipmentUpdated = equipmentService.update(1L, equipmentPutRequest);

        Assertions.assertEquals(equipmentSaved.getId(), equipmentUpdated.getId());
        Assertions.assertEquals(equipmentSaved.getBrand(), equipmentUpdated.getBrand());
        Assertions.assertNotEquals(equipmentSaved.getName(), equipmentUpdated.getName());

    }

    @Test
    void updateEquipmentStatus_Return_Equipment_WhenSuccessful() {
        EquipmentStatusPatchRequestDTO equipmentStatusPatchRequest =
                new EquipmentStatusPatchRequestDTO(EquipmentAvailabilityStatus.BUSY);

        EquipmentPostRequestDTO equipment = EquipmentPostRequestDTO.builder()
                .name("Aspire-3")
                .brand("ACER")
                .processor("Intel i5")
                .ramMemory("8 gb")
                .storageMemory("500 mb ssd")
                .tombo("125478")
                .build();

        Equipment equipmentSaved = equipmentService.save(equipment);

        Equipment equipmentUpdated = equipmentService.updateEquipmentStatus(1L, equipmentStatusPatchRequest);

        Assertions.assertEquals(equipmentSaved.getId(), equipmentUpdated.getId());
        Assertions.assertEquals(EquipmentAvailabilityStatus.BUSY, equipmentUpdated.getEquipmentStatus());

    }

    @Test
    void delete() {
    }
}