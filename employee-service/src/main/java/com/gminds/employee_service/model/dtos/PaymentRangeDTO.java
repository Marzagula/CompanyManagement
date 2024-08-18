package com.gminds.employee_service.model.dtos;

import com.gminds.employee_service.model.enums.EmplAgreementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PaymentRangeDTO(
        Long id,
        @NotNull @Min(0) Double minSalary,
        @NotNull @Min(0) Double maxSalary,
        @NotNull Long jobId,
        @NotNull EmplAgreementType emplAgreementType,
        @NotNull Integer year,
        String createdBy,
        LocalDate createdDate,
        String lastModifiedBy,
        LocalDate lastModifiedDate
) {
}
