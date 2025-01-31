package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.repository.EmployeeAgreementRepository;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.agreement.template.AgreementTransactionHelper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;

@Service
public class AgreementManagementService {

    private static final Logger logger = LoggerFactory.getLogger(AgreementManagementService.class);
    private final EmployeeAgreementRepository employeeAgreementRepository;
    private final EmployeeRepository employeeRepository;
    private final AgreementTransactionHelper transactionHelper;

    public AgreementManagementService(EmployeeAgreementRepository employeeAgreementRepository,
                                      AgreementTransactionHelper transactionHelper,
                                      EmployeeRepository employeeRepository) {
        this.employeeAgreementRepository = employeeAgreementRepository;
        this.transactionHelper = transactionHelper;
        this.employeeRepository = employeeRepository;
    }

    public EmployeeAgreement createAndAddNewAgreement(Employee employee, EmployeeAgreement newAgreement) throws EmployeeAgreementException {
        employee.getAgreements().add(newAgreement);
        newAgreement.setEmployee(employee);
        newAgreement.setStatus(determineAgreementStatus(newAgreement));
        employee.getAgreements().add(newAgreement);

        return transactionHelper.executeInTransaction(() ->
                employeeRepository.save(employee)
                        .getAgreements()
                        .stream()
                        .max(Comparator.comparing(EmployeeAgreement::getId))
                        .orElseThrow());
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
    @Transactional
    public void closePreviousAgreement(EmployeeAgreement lastAgreement, LocalDate newAgreementStartDate) throws EmployeeAgreementException {
        LocalDate adjustedToDate = newAgreementStartDate.minusDays(1);
        lastAgreement.setToDate(adjustedToDate);
        if (!lastAgreement.getToDate().isAfter(LocalDate.now())) {
            lastAgreement.setStatus(AgreementStatus.FINISHED);
        }
        transactionHelper.executeInTransaction(() -> {
            employeeAgreementRepository.saveAndFlush(lastAgreement);
            return null;
        });

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
    public EmployeeAgreement findMostRecentActiveAgreement(Employee employee) {
        return employee.getAgreements()
                .stream()
                .filter(agreement -> agreement.getStatus() == AgreementStatus.ACTIVE)
                .max(Comparator.comparing(EmployeeAgreement::getFromDate))
                .orElseThrow(() -> {
                    logger.error("Can't find any active agreement.");
                    return new ResourceNotFoundException("Can't find any active agreement.");
                });
    }

    public AgreementStatus determineAgreementStatus(EmployeeAgreement agreement) {
        return agreement.getFromDate().isAfter(LocalDate.now()) ? AgreementStatus.FUTURE : AgreementStatus.ACTIVE;
    }
}


