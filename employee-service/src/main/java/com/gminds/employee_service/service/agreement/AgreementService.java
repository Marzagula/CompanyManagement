package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;

public interface AgreementService {
    EmployeeAgreementDTO newAgreement(Long id, EmployeeAgreementDTO agreementDTO);
}
