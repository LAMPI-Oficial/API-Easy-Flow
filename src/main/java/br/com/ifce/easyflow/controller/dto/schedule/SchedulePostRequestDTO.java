package br.com.ifce.easyflow.controller.dto.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "Day shift",
            example = "Morning")
    @JsonProperty(value = "shift-schedule")
    @NotBlank
    private String shiftSchedule;

    @ApiModelProperty(value = "Weekday",
            example = "Monday")
    @JsonProperty(value = "day")
    @NotBlank
    private String day;

    @ApiModelProperty(value = "Person id",
            notes = "ID of the person making the time request", example = "2")
    @NotNull
    @JsonProperty(value = "table-id")
    private Long tableId;
}
