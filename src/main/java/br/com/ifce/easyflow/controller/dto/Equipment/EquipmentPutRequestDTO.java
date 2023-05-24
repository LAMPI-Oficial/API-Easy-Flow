package br.com.ifce.easyflow.controller.dto.equipment;

import br.com.ifce.easyflow.model.Equipment;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EquipmentPutRequestDTO {
    @ApiModelProperty(value = "Equipment tombo",
            example = "156321")
    @JsonProperty(value = "tombo")
    @NotBlank(message = "The tombo cannot be empty or null")
    private String tombo;

    @ApiModelProperty(value = "Equipment brand",
            example = "Acer")
    @JsonProperty(value = "brand")
    @NotBlank(message = "The brand cannot be empty or null")
    private String brand;

    @ApiModelProperty(value = "Equipment name",
            example = "Aspire 3 a345")
    @JsonProperty(value = "name")
    @NotBlank(message = "The name cannot be empty or null")
    private String name;

    @ApiModelProperty(value = "Equipment RAM memory",
            example = "8gb")
    @NotBlank(message = "The ram-memory cannot be empty or null")
    @JsonProperty("ram-memory")
    private String ramMemory;

    @ApiModelProperty(value = "Equipment processor",
            example = "AMD Ryzen 5 5500U")
    @NotBlank(message = "The processor cannot be empty or null")
    private String processor;

    @ApiModelProperty(value = "Equipment storage memory",
            example = "SSD 500gb")
    @NotBlank(message = "The storage-memory cannot be empty or null")
    @JsonProperty("storage-memory")
    private String storageMemory;
}
