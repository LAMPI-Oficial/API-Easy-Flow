package br.com.ifce.easyflow.controller.dto.table;

import javax.validation.constraints.NotBlank;

public record SearchTablesAvailableRequestDTO(@NotBlank String shiftSchedule, @NotBlank String day) {
}
