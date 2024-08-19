package com.gminds.employee_service.service.agreement.template;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.service.utils.TransactionHelper;
import org.springframework.stereotype.Service;

@Service
public class AgreementTransactionHelper extends TransactionHelper<EmployeeAgreementDTO, RuntimeException> {
    @Override
    protected RuntimeException prepareException(RuntimeException e) {
        return new EmployeeAgreementException(e.getMessage());
    }
}
