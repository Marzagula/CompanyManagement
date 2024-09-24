package org.gminds.accounting_service.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClauseDTO(
        @NotNull @Min(1) Long id,
        @NotNull String clauseType,
        @NotNull String clauseTitle,
        @NotNull String clauseDescription) {
}
