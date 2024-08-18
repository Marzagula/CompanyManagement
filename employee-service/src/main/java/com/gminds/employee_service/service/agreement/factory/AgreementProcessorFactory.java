package com.gminds.employee_service.service.agreement.factory;

import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.repository.EmployeeAgreementRepository;
import com.gminds.employee_service.service.agreement.AgreementManagementService;
import com.gminds.employee_service.service.agreement.processor.AgreementProcessor;
import com.gminds.employee_service.service.agreement.processor.B2BAgreementProcessor;
import com.gminds.employee_service.service.agreement.validator.AgreementValidator;
import com.gminds.employee_service.service.agreement.processor.EmploymentAgreementProcessor;
import org.springframework.stereotype.Component;

@Component
public class AgreementProcessorFactory {

    private final AgreementValidator agreementValidator;
    private final AgreementManagementService agreementManagementService;

    public AgreementProcessorFactory(AgreementValidator agreementValidator,
                                     AgreementManagementService agreementManagementService) {
        this.agreementValidator = agreementValidator;
        this.agreementManagementService = agreementManagementService;
    }

    public AgreementProcessor getProcessor(EmplAgreementType agreementType) {
        return switch (agreementType) {
            case B2B -> new B2BAgreementProcessor(agreementValidator,agreementManagementService);
            case EMPLOYMENT -> new EmploymentAgreementProcessor(agreementValidator,agreementManagementService);
        };
    }
}
