package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.agreement.factory.AgreementProcessorFactory;
import com.gminds.employee_service.service.agreement.processor.AgreementProcessor;
import com.gminds.employee_service.service.utils.TransactionHelper;
import com.gminds.employee_service.service.utils.mappers.EmployeeAgreementMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DefaultAgreementService implements AgreementService{

    private final TransactionHelper transactionHelper;
    private final AgreementProcessorFactory agreementProcessorFactory;
    private final EmployeeRepository employeeRepository;


    public DefaultAgreementService(TransactionHelper transactionHelper,
                                   AgreementProcessorFactory agreementProcessorFactory,
                                   EmployeeRepository employeeRepository) {
        this.transactionHelper = transactionHelper;
        this.agreementProcessorFactory = agreementProcessorFactory;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public EmployeeAgreementDTO newAgreement(Long employeeId, EmployeeAgreementDTO agreementDTO) {
        return transactionHelper.executeInTransaction(() -> {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee with id "+employeeId+" not found"));
            EmployeeAgreement newAgreement = prepareAgreement(employee, agreementDTO);
            AgreementProcessor processor = agreementProcessorFactory.getProcessor(newAgreement.getAgreementType());
            try {
                processor.process(newAgreement);
            } catch (EmployeeAgreementException e) {
                throw new RuntimeException(e);
            }

            return EmployeeAgreementMapper.INSTANCE.toEmployeeAgreementDTO(newAgreement);
        });
    }

    private EmployeeAgreement prepareAgreement(Employee employee, EmployeeAgreementDTO agreementDTO) {
        EmployeeAgreement agreement = EmployeeAgreementMapper.INSTANCE.toEmployeeAgreement(agreementDTO);
        agreement.setEmployee(employee);
        return agreement;
    }
}
