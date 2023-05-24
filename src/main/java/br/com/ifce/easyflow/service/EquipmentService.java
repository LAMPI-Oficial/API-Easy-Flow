package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPostRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentPutRequestDTO;
import br.com.ifce.easyflow.controller.dto.equipment.EquipmentStatusPatchRequestDTO;
import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import br.com.ifce.easyflow.repository.EquipmentRepository;
import br.com.ifce.easyflow.service.exceptions.DatabaseException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public Page<Equipment> findAll(Pageable pageable) {
        return equipmentRepository.findAll(pageable);
    }

    public Equipment findById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No equipment was found with the provided id, " +
                        "check the registered equipment."));
    }

    @Transactional
    public Equipment save(EquipmentPostRequestDTO requestDTO) {
        Equipment equipment = Equipment.builder()
                .name(requestDTO.getName())
                .brand(requestDTO.getBrand())
                .tombo(requestDTO.getTombo())
                .processor(requestDTO.getProcessor())
                .ramMemory(requestDTO.getRamMemory())
                .storageMemory(requestDTO.getStorageMemory())
                .equipmentStatus(EquipmentAvailabilityStatus.AVAILABLE)
                .build();

        return equipmentRepository.save(equipment);
    }

    public Equipment update(Long id, EquipmentPutRequestDTO requestDTO) {
        Equipment equipmentSaved = this.findById(id);
        Equipment updatedEquipment = updateEquipmentEntity(equipmentSaved, requestDTO);

        return equipmentRepository.save(updatedEquipment);
    }

    @Transactional
    public Equipment updateEquipmentStatus(Long id, EquipmentStatusPatchRequestDTO requestDTO) {
        Equipment equipment = this.findById(id);
        equipment.setEquipmentStatus(requestDTO.getEquipmentStatus());
        return equipmentRepository.save(equipment);
    }

    public void delete(Long id) {
        this.findById(id);

        try {
            equipmentRepository.deleteById(id);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private Equipment updateEquipmentEntity(Equipment equipmentSaved, EquipmentPutRequestDTO request) {
        equipmentSaved.setBrand(request.getBrand());
        equipmentSaved.setName(request.getName());
        equipmentSaved.setTombo(request.getTombo());
        equipmentSaved.setProcessor(request.getProcessor());
        equipmentSaved.setRamMemory(request.getRamMemory());
        equipmentSaved.setStorageMemory(request.getStorageMemory());
        return equipmentSaved;
    }
}
