package org.gminds.accounting_service.model.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.gminds.accounting_service.model.enums.employee.AgreementStatus;
import org.gminds.accounting_service.model.enums.employee.EmplAgreementType;
import org.gminds.accounting_service.model.enums.employee.EmploymentPaymentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EmployeeAgreementDTO(Long id,
                                   @NotNull @Min(0) Double salary,
                                   @NotNull LocalDate fromDate,
                                   LocalDate toDate,
                                   @NotNull AgreementStatus status,
                                   @NotNull EmplAgreementType agreementType,
                                   @NotNull EmploymentPaymentType paymentType,
                                   @NotNull @Min(1) Long employeeId,
                                   List<EmployeeAgreementClauseDTO> clauses,
                                   String createdBy,
                                   LocalDateTime createdDate,
                                   String lastModifiedBy,
                                   LocalDateTime lastModifiedDate) {
}
