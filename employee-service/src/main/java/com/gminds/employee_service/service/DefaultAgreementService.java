package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.DataValidationException;
import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.utils.AgreementUtils;
import com.gminds.employee_service.service.utils.TransactionalServiceHelper;
import com.gminds.employee_service.service.utils.mappers.EmployeeAgreementMapper;
import com.gminds.employee_service.service.validation.AgreementsValidator;
import com.gminds.employee_service.service.validation.DateValidator;
import com.gminds.employee_service.service.validation.DateValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Consumer;

@Service
public class DefaultAgreementService implements AgreementService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAgreementService.class);
    private final TransactionalServiceHelper transactionalHelper;
    private final EmployeeRepository employeeRepository;
    private final DateValidator dateValidatorService;
    private final AgreementsValidator agreementValidatorService;
    private final AgreementUtils agreementUtils;

    public DefaultAgreementService(TransactionalServiceHelper transactionalHelper,
                                   EmployeeRepository employeeRepository,
                                   DateValidatorImpl dateValidatorService,
                                   AgreementsValidator agreementValidatorService,
                                   AgreementUtils agreementUtils) {
        this.transactionalHelper = transactionalHelper;
        this.employeeRepository = employeeRepository;
        this.dateValidatorService = dateValidatorService;
        this.agreementValidatorService = agreementValidatorService;
        this.agreementUtils = agreementUtils;
    }

    @Override
    public EmployeeAgreementDTO terminatePreviousAndAddNewAgreement(Long id, EmployeeAgreementDTO agreementDTO) {
        return transactionalHelper.executeInTransaction(() -> {
            Employee employee = getEmployeeById(id);
            dateValidatorService.validateIfEarlierIsBeforeLater(agreementDTO.fromDate(), agreementDTO.toDate());
            EmployeeAgreement newAgreement = createAndAddNewAgreement(employee, agreementDTO);
            closePreviousAgreement(employee, newAgreement.getFromDate());
            validateEmployeeAgreements(employee);

            return EmployeeAgreementMapper.INSTANCE.toEmployeeAgreementDTO(newAgreement);
        });
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Can't find employee with id: {}", id);
                    return new ResourceNotFoundException("Can't find employee with id: " + id);
                });
    }

    @Override
    public <E> void addEntityToEmployeeCollection(Employee employee, E entity, Consumer<Employee> employeeSetter, Collection<E> collection) {
        AgreementService.super.addEntityToEmployeeCollection(employee, entity, employeeSetter, collection);
    }

    private void validateEmployeeAgreements(Employee employee) {
        try {
            agreementValidatorService.validate(employee.getAgreements());
        } catch (EmployeeAgreementException e) {
            throw new DataValidationException("Agreement validation failed", e);
        }
    }

    private EmployeeAgreement createAndAddNewAgreement(Employee employee, EmployeeAgreementDTO agreementDTO) {
        EmployeeAgreement newAgreement = agreementUtils.prepareAgreement(agreementDTO);
        addEntityToEmployeeCollection(employee, newAgreement, newAgreement::setEmployee, employee.getAgreements());
        return newAgreement;
    }

    private void closePreviousAgreement(Employee employee, LocalDate newAgreementStartDate) {
        EmployeeAgreement lastAgreement = agreementUtils.findLastActiveAgreement(employee);
        agreementUtils.closePreviousAgreement(lastAgreement, newAgreementStartDate);
    }

}
