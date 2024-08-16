package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.validation.AgreementsValidator;
import com.gminds.employee_service.service.validation.DateValidatorImpl;
import com.gminds.employee_service.util.mappers.EmployeeAgreementMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;

@Service
public class AgreementService extends AbstractTransactionalService<EmployeeAgreementDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
    private final EmployeeRepository employeeRepository;
    DateValidatorImpl datesValidator;
    AgreementsValidator agreementListValidator;

    public AgreementService(EmployeeRepository employeeRepository,
                            DateValidatorImpl datesValidator,
                            AgreementsValidator agreementListValidator) {
        this.employeeRepository = employeeRepository;
        this.datesValidator = datesValidator;
        this.agreementListValidator = agreementListValidator;
    }

    @Transactional
    public EmployeeAgreementDTO terminatePreviousAndAddNewAgreement(Long id, EmployeeAgreementDTO agreementDTO) throws ResourceNotFoundException, EmployeeAgreementException {
        return executeChanges(id, agreementDTO);
    }

    /**
     * Finds the last active employee agreement based on the latest fromDate.
     * <p>
     * This method filters the employee's agreements to find those that are in
     * ACTIVE status, then returns the one with the latest fromDate.
     * <p>
     * New agreement always will change the last active agreement if it's still in force at moment of transaction.
     *
     * @param employee the employee whose agreements are being searched
     * @return the employee's last active agreement
     * @throws ResourceNotFoundException if no active agreement is found
     */
    private EmployeeAgreement findLastActiveAgreement(Employee employee) {
        return employee.getAgreements()
                .stream()
                .filter(agreement -> agreement.getStatus() == AgreementStatus.ACTIVE)
                .max(Comparator.comparing(EmployeeAgreement::getFromDate))
                .orElseThrow(() -> {
                    logger.error("Can't find any active agreement.");
                    return new ResourceNotFoundException("Can't find any active agreement.");
                });
    }

    /**
     * Adjusts the `toDate` of the previous agreement to the day before the new agreement's start date.
     * <p>
     * If the `toDate` is in the past after the adjustment, the agreement's status is set to `FINISHED`.
     * <p>
     * It's made to prevent situation of two agreements in force.
     *
     * @param lastAgreement         the previous agreement to be closed
     * @param newAgreementStartDate the start date of the new agreement
     */
    private void closePreviousAgreement(EmployeeAgreement lastAgreement, LocalDate newAgreementStartDate) {
        LocalDate adjustedToDate = newAgreementStartDate.minusDays(1);
        lastAgreement.setToDate(adjustedToDate);
        if (!lastAgreement.getToDate().isAfter(LocalDate.now())) {
            lastAgreement.setStatus(AgreementStatus.FINISHED);
        }
    }

    /**
     * Determines status of new agreement,
     * if its start date is in future then status is FUTURE,
     * if its not in future then its ACTIVE
     * <p>
     * It's necessary for proper identification of agreement.
     *
     * @param agreement represents new agreement
     */
    private AgreementStatus determineAgreementStatus(EmployeeAgreement agreement) {
        return agreement.getFromDate().isAfter(LocalDate.now()) ? AgreementStatus.FUTURE : AgreementStatus.ACTIVE;
    }

    /**
     * Mapping EmployeeAgreementDTO to EmployeeAgreement and change its status.
     *
     * @param agreementDTO is new agreement passed as RequestBody.
     * @return is new adjusted agreement.
     */
    private EmployeeAgreement prepareAgreement(EmployeeAgreementDTO agreementDTO) {
        EmployeeAgreement newAgreement = EmployeeAgreementMapper.INSTANCE.toEmployeeAgreement(agreementDTO);
        newAgreement.setStatus(determineAgreementStatus(newAgreement));
        return newAgreement;
    }

    @Override
    protected Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Can't find employee with id: {}", id);
                    return new ResourceNotFoundException("Can't find employee with id: " + id);
                });
    }

    @Override
    protected EmployeeAgreementDTO processTransaction(Employee employee, EmployeeAgreementDTO agreementDTO) throws EmployeeAgreementException {
        datesValidator.validateIfEarlierIsBeforeLater(agreementDTO.fromDate(), agreementDTO.toDate());
        EmployeeAgreement newAgreement = prepareAgreement(agreementDTO);
        addEntityToEmployeeCollection(employee, newAgreement, newAgreement::setEmployee, employee.getAgreements());
        closePreviousAgreement(findLastActiveAgreement(employee), agreementDTO.fromDate());
        agreementListValidator.validate(employee.getAgreements());
        return EmployeeAgreementMapper.INSTANCE.toEmployeeAgreementDTO(newAgreement);
    }
}
