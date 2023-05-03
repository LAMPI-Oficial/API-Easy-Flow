package br.com.ifce.easyflow.controller.dto.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePutRequestDTO {
    @NotBlank
    private String shiftSchedule;

    @NotBlank
    private String day;

    @NotNull
    @JsonProperty(value = "table-id")
    private Long tableId;

}
