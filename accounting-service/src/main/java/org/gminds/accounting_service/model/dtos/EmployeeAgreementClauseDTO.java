package org.gminds.accounting_service.model.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmployeeAgreementClauseDTO(
        @NotNull @Min(1) Long id,
        @NotNull @Min(1) Long clauseId,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull @Min(1) Long employeeAgreementId
) {
}
