package com.gminds.employee_service.service.agreement.processor;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.EmplAgreementType;

public interface AgreementProcessor {
    EmployeeAgreement process(EmployeeAgreement agreement) throws EmployeeAgreementException;
    EmplAgreementType getType();
}
