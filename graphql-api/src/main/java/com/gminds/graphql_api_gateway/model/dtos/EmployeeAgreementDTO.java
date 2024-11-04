package com.gminds.graphql_api_gateway.model.dtos;


import com.gminds.graphql_api_gateway.model.enums.AgreementStatus;
import com.gminds.graphql_api_gateway.model.enums.EmplAgreementType;
import com.gminds.graphql_api_gateway.model.enums.EmploymentPaymentType;
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
