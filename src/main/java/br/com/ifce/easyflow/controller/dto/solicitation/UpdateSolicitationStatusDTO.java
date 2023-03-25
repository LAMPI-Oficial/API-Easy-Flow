package br.com.ifce.easyflow.controller.dto.solicitation;

import br.com.ifce.easyflow.model.enums.SolicitationStatus;
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
public class UpdateSolicitationStatusDTO {

    @JsonProperty(value = "solicitation-status")
    @NotBlank(message = "The solicitation-status cannot be empty or null")
    private SolicitationStatus status;
}
