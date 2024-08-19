package com.gminds.employee_service.service.agreement.validator;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.PaymentRange;

public interface SalaryValidator {
    void validateSalary(PaymentRange paymentRange, EmployeeAgreement employeeAgreement) throws EmployeeAgreementException;
}
