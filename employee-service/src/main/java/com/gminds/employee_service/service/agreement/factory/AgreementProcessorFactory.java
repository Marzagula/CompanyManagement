package com.gminds.employee_service.service.agreement.factory;

import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.service.agreement.AgreementManagementService;
import com.gminds.employee_service.service.agreement.processor.AgreementProcessor;
import com.gminds.employee_service.service.agreement.validator.AgreementValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AgreementProcessorFactory {

    private final AgreementValidator agreementValidator;
    private final AgreementManagementService agreementManagementService;
    private final Map<EmplAgreementType, AgreementProcessor> processorMap;

    public AgreementProcessorFactory(List<AgreementProcessor> processors,
                                     AgreementValidator agreementValidator,
                                     AgreementManagementService agreementManagementService) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(AgreementProcessor::getType, Function.identity()));

        this.agreementValidator = agreementValidator;
        this.agreementManagementService = agreementManagementService;
    }

    public AgreementProcessor getProcessor(EmplAgreementType agreementType) {
        return processorMap.get(agreementType);
    }
}
