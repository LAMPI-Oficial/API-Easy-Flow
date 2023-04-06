package br.com.ifce.easyflow.controller.dto.schedule;

import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleApprovedRequestDTO {
    @NotNull
    @JsonProperty(value = "status")
    private ScheduleRequestStatus status;

    @NotNull
    @JsonProperty(value = "table-id")
    private Long tableId;
}
