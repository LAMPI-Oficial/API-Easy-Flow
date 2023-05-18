package br.com.ifce.easyflow.controller.dto.schedule;

import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "Status of the schedule request",
            example = "APPROVED")
    @NotNull
    @JsonProperty(value = "status")
    private ScheduleRequestStatus status;

    @ApiModelProperty(value = "Id of the table to be linked to the reserved time",
            example = "1")
    @NotNull
    @JsonProperty(value = "table-id")
    private Long tableId;
}
