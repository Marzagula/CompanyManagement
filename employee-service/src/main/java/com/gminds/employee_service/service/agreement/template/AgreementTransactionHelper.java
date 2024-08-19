package com.gminds.employee_service.service.agreement.template;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.service.utils.AbstractTransactionHelper;
import org.springframework.stereotype.Service;

@Service
public class AgreementTransactionHelper extends AbstractTransactionHelper<EmployeeAgreementException> {


    @Override
    protected EmployeeAgreementException convertException(Exception e) {
        return new EmployeeAgreementException(e.getMessage());
    }
}