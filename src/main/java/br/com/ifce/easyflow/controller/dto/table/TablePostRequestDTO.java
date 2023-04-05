package br.com.ifce.easyflow.controller.dto.table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record TablePostRequestDTO(@NotNull @NotBlank Long number) {
}
