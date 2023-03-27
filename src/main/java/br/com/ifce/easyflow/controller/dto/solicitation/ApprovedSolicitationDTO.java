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
public class ApprovedSolicitationDTO {

    @JsonProperty(value = "equipment-id")
    @NotBlank(message = "The equipment cannot be empty or null")
    private Long equipmentId;

}
