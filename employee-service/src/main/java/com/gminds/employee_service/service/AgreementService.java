package com.gminds.employee_service.service;

import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;

public interface AgreementService extends EmployeeCollections {
    EmployeeAgreementDTO terminatePreviousAndAddNewAgreement(Long id, EmployeeAgreementDTO agreementDTO);
}
