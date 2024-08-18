package com.gminds.employee_service.service.agreement.processor;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.service.agreement.AgreementManagementService;
import com.gminds.employee_service.service.agreement.validator.AgreementValidator;
import org.springframework.stereotype.Service;

@Service
public class B2BAgreementProcessor implements AgreementProcessor {
    private final AgreementManagementService agreementManagementService;
    private final AgreementValidator agreementValidator;

    public B2BAgreementProcessor(AgreementValidator agreementValidator,
                                 AgreementManagementService agreementManagementService) {
        this.agreementManagementService = agreementManagementService;
        this.agreementValidator = agreementValidator;
    }


    @Override
    public EmployeeAgreement process(EmployeeAgreement agreement) throws EmployeeAgreementException {
        /**TODO pełna logika dla umów B2B*/
        Employee employee = agreement.getEmployee();
        EmployeeAgreement newAgreement = agreementManagementService.createAndAddNewAgreement(employee, agreement);
        agreementManagementService.closePreviousAgreement(agreementManagementService.findMostRecentActiveAgreement(employee), agreement.getFromDate());
        agreementValidator.validateAgreement(newAgreement);
        return newAgreement;
    }

    @Override
    public EmplAgreementType getType() {
        return EmplAgreementType.B2B;
    }
}