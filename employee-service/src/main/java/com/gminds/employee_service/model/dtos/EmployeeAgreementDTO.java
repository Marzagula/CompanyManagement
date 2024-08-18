package com.gminds.employee_service.model.dtos;

import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.model.enums.EmploymentPaymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeAgreementDTO(Long id,
                                   @NotNull @Min(0) Double salary,
                                   @NotNull LocalDate fromDate,
                                   LocalDate toDate,
                                   @NotNull AgreementStatus status,
                                   @NotNull EmplAgreementType agreementType,
                                   @NotNull EmploymentPaymentType paymentType,
                                   @NotNull @Min(1) Long employeeId,
                                   String createdBy,
                                   LocalDateTime createdDate,
                                   String lastModifiedBy,
                                   LocalDateTime lastModifiedDate) {
}
