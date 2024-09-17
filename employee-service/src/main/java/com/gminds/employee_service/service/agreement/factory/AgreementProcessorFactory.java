package com.gminds.employee_service.service.agreement.factory;

import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.service.agreement.processor.AgreementProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AgreementProcessorFactory {

    private final Map<EmplAgreementType, AgreementProcessor> processorMap;

    public AgreementProcessorFactory(List<AgreementProcessor> processors
    ) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(AgreementProcessor::getType, Function.identity()));

    }

    public AgreementProcessor getProcessor(EmplAgreementType agreementType) {
        return processorMap.get(agreementType);
    }
}
