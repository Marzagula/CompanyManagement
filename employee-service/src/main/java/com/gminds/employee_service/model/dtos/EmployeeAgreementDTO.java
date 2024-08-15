package com.gminds.employee_service.model.dtos;

import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.model.enums.EmploymentPaymentType;

import java.time.LocalDate;

public record EmployeeAgreementDTO(Long id,
                                   Double salary,
                                   LocalDate fromDate,
                                   LocalDate toDate,
                                   AgreementStatus status,
                                   EmplAgreementType agreementType,
                                   EmploymentPaymentType paymentType) {
}