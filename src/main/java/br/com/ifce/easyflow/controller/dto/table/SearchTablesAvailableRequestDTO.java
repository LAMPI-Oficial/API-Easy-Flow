package br.com.ifce.easyflow.controller.dto.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public record SearchTablesAvailableRequestDTO(
        @ApiModelProperty(value = "Day shift",
        example = "Morning")
        @JsonProperty(value = "shift-schedule")
        @NotBlank String shiftSchedule,
        @ApiModelProperty(value = "Weekday",
        example = "Monday")
        @JsonProperty(value = "day")
        @NotBlank String day) {
}
