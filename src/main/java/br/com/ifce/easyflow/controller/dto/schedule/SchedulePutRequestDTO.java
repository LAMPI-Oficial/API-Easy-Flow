package br.com.ifce.easyflow.controller.dto.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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

    @NotNull
    @JsonProperty(value = "table-id")
    private Long tableId;

}
