package br.com.ifce.easyflow.controller.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostRequestDTO {
    @NotBlank
    private String shiftSchedule;

    @NotBlank
    private String day;

    @NotBlank
    private int tableNumber;

    @NotBlank
    private Long personId;
}
