package com.gminds.employee_service.service.agreement.validator;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.PaymentRangeDTO;

public interface SalaryValidator {
    void validateSalary(PaymentRangeDTO paymentRange, EmployeeAgreement employeeAgreement) throws EmployeeAgreementException;
}
