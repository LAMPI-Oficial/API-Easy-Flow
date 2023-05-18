package br.com.ifce.easyflow.controller.dto.solicitation;

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
public class ApprovedSolicitationDTO {

    @ApiModelProperty(value = "Equipment ID to be linked to the request",
            example = "1")
    @JsonProperty(value = "equipment-id")
    @NotBlank(message = "The equipment cannot be empty or null")
    private Long equipmentId;

}
