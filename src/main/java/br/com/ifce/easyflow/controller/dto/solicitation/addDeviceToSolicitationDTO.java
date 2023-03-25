package br.com.ifce.easyflow.controller.dto.solicitation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class addDeviceToSolicitationDTO {

    @NotBlank(message = "The equipment id cannot be empty or null")
    @JsonProperty("equipment-id")
    private Long equipmentId;

}
