package com.gminds.employee_service.service.agreement.validator;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;

public interface AgreementValidator {
    void validateAgreement(EmployeeAgreement employeeAgreement) throws EmployeeAgreementException;
}
