package br.com.ifce.easyflow.controller.dto.equipment;

import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class EquipmentStatusPatchRequestDTO {

    @JsonProperty("equipment-status")
    EquipmentAvailabilityStatus equipmentStatus;
}
