package org.gminds.accounting_service.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.gminds.accounting_service.model.enums.employee.EmplAgreementType;

import java.time.LocalDate;

public record PaymentRangeDTO(
        @NotNull @Min(1) Long id,
        @NotNull @Min(0) Double minSalary,
        @NotNull @Min(0) Double maxSalary,
        @NotNull Long jobId,
        @NotNull EmplAgreementType emplAgreementType,
        @NotNull Integer fiscalYear,
        String createdBy,
        LocalDate createdDate,
        String lastModifiedBy,
        LocalDate lastModifiedDate
) {
}
