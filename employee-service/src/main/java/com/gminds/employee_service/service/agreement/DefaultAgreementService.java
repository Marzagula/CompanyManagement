package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.agreement.factory.AgreementProcessorFactory;
import com.gminds.employee_service.service.agreement.processor.AgreementProcessor;
import com.gminds.employee_service.service.agreement.strategy.AgreementCollectionStrategy;
import com.gminds.employee_service.service.agreement.template.AgreementTransactionHelper;
import com.gminds.employee_service.service.utils.mappers.EmployeeAgreementMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DefaultAgreementService extends AgreementCollectionStrategy implements AgreementService {

    private final AgreementTransactionHelper transactionHelper;
    private final AgreementProcessorFactory agreementProcessorFactory;
    private final EmployeeRepository employeeRepository;


    public DefaultAgreementService(AgreementTransactionHelper transactionHelper,
                                   AgreementProcessorFactory agreementProcessorFactory,
                                   EmployeeRepository employeeRepository) {
        this.transactionHelper = transactionHelper;
        this.agreementProcessorFactory = agreementProcessorFactory;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public EmployeeAgreementDTO newAgreement(Long employeeId, EmployeeAgreementDTO agreementDTO) throws EmployeeAgreementException {
        return transactionHelper.executeInTransaction(() -> {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employeeId + " not found"));
            EmployeeAgreement newAgreement = EmployeeAgreementMapper.INSTANCE.toEmployeeAgreement(agreementDTO);
            addToCollection(employee, newAgreement);
            AgreementProcessor processor = agreementProcessorFactory.getProcessor(newAgreement.getAgreementType());

            try {
                processor.process(newAgreement);
            } catch (EmployeeAgreementException e) {
                throw new RuntimeException(e);
            }

            return EmployeeAgreementMapper.INSTANCE.toEmployeeAgreementDTO(newAgreement);
        });
    }

}
