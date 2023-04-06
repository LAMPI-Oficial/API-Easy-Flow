package br.com.ifce.easyflow.controller.dto.table;

import javax.validation.constraints.NotNull;

public record LabTableUpdateRequestDTO(@NotNull Long number) {
}
