package br.com.ifce.easyflow.controller.dto.table;

import javax.validation.constraints.NotNull;

public record TablePostRequestDTO(@NotNull Long number) {
}
