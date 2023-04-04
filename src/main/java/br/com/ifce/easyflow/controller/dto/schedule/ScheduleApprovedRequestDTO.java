package br.com.ifce.easyflow.controller.dto.schedule;

import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleApprovedRequestDTO {
    @NotNull @NotEmpty
    private ScheduleRequestStatus status;

    @NotNull @NotEmpty
    private Long tableId;
}
