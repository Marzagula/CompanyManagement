package com.gminds.employee_service.service.agreement.processor;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;

public interface AgreementProcessor {
    void process(EmployeeAgreement agreement) throws EmployeeAgreementException;
}
