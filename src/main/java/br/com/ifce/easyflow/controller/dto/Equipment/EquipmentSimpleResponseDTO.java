package br.com.ifce.easyflow.controller.dto.Equipment;

import br.com.ifce.easyflow.model.Equipment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentSimpleResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tombo")
    private String tombo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("equipment_status")
    private String equipmentStatus;

    public EquipmentSimpleResponseDTO(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.tombo = equipment.getTombo();
        this.equipmentStatus = equipment.getEquipmentStatus().name();
    }
}
