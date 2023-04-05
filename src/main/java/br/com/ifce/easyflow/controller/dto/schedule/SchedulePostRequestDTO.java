package br.com.ifce.easyflow.controller.dto.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostRequestDTO {
    @NotBlank
    private String shiftSchedule;

    @NotBlank
    private String day;

    @NotNull
    @JsonProperty(value = "person-id")
    private Long personId;
}
