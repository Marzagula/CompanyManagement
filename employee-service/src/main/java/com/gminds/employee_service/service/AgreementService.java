package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;

@Service
public class AgreementService {

    private final EmployeeRepository repository;

    public AgreementService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeAgreement terminatePreviousAndAddNewAgreement(EmployeeAgreement newAgreement) throws EmployeeAgreementException {
        Employee employee = repository.findById(newAgreement.getEmployee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        EmployeeAgreement lastAgreement = findLastAgreement(employee);

        if (lastAgreement != null) {
            closePreviousAgreement(lastAgreement, newAgreement.getFromDate());
        }

        validateAgreement(newAgreement);

        AgreementStatus newStatus = determineAgreementStatus(newAgreement);
        newAgreement.setStatus(newStatus);

        employee.getAgreements().add(newAgreement);
        repository.save(employee);
        return newAgreement;
    }

    private EmployeeAgreement findLastAgreement(Employee employee) {
        return employee.getAgreements()
                .stream()
                .max(Comparator.comparing(EmployeeAgreement::getFromDate))
                .orElse(null);
    }

    private void closePreviousAgreement(EmployeeAgreement lastAgreement, LocalDate newAgreementStartDate) {
        if(lastAgreement.getToDate()==null){
            lastAgreement.setToDate(newAgreementStartDate.minusDays(1));
            if (!lastAgreement.getToDate().isAfter(LocalDate.now())) {
                lastAgreement.setStatus(AgreementStatus.FINISHED);
            }
        }
    }

    private AgreementStatus determineAgreementStatus(EmployeeAgreement agreement) {
        return agreement.getFromDate().isAfter(LocalDate.now()) ? AgreementStatus.FUTURE : AgreementStatus.ACTIVE;
    }

    private void validateAgreement(EmployeeAgreement agreement) throws EmployeeAgreementException {
        if (agreement.getToDate() != null && !agreement.getFromDate().isBefore(agreement.getToDate())) {
            throw new EmployeeAgreementException("Agreement end date can't be earlier than start date.");
        }
    }
}
