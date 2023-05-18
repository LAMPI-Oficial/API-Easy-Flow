package br.com.ifce.easyflow.controller.dto.equipment;

import br.com.ifce.easyflow.model.Equipment;
import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor @Data
public class EquipmentStatusPatchRequestDTO {

    @ApiModelProperty(value = "Equipment status",
            example = "AVAILABLE")
    @JsonProperty("equipment-status")
    @NotNull
    EquipmentAvailabilityStatus equipmentStatus;
}
