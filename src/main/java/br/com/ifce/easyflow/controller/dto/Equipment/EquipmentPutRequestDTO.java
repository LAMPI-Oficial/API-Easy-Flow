package br.com.ifce.easyflow.controller.dto.equipment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EquipmentPutRequestDTO {
    private String tombo;

    private String brand;

    private String name;

    @JsonProperty("ram-memory")
    private String ramMemory;

    private String processor;

    @JsonProperty("storage-memory")
    private String storageMemory;
}
