package br.com.ifce.easyflow.controller.dto.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public record TablePostRequestDTO(
        @ApiModelProperty(value = "Table number",
        example = "32")
        @JsonProperty(value = "number")
        @NotNull Long number) {
}
