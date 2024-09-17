package com.gminds.employee_service.service.agreement.processor;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.service.agreement.AgreementManagementService;
import com.gminds.employee_service.service.agreement.validator.AgreementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmploymentAgreementProcessor implements AgreementProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EmploymentAgreementProcessor.class);
    private final AgreementValidator agreementValidator;
    private final AgreementManagementService agreementManagementService;

    public EmploymentAgreementProcessor(AgreementValidator agreementValidator,
                                        AgreementManagementService agreementManagementService) {
        this.agreementManagementService = agreementManagementService;
        this.agreementValidator = agreementValidator;
    }

    @Override
    public EmployeeAgreement process(EmployeeAgreement agreement) throws EmployeeAgreementException {
        /**TODO pełna logika dla umów o pracę*/
        Employee employee = agreement.getEmployee();
        EmployeeAgreement newAgreement = agreementManagementService.createAndAddNewAgreement(employee, agreement);
        agreementManagementService.closePreviousAgreement(agreementManagementService.findMostRecentActiveAgreement(employee), agreement.getFromDate());
        agreementValidator.validateAgreement(newAgreement);
        return newAgreement;
    }

    @Override
    public EmplAgreementType getType() {
        return EmplAgreementType.EMPLOYMENT;
    }
}
