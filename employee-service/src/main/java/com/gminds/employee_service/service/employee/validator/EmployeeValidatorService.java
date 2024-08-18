package com.gminds.employee_service.service.employee.validator;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.service.agreement.validator.AgreementValidator;
import com.gminds.employee_service.service.utils.validator.DateValidator;
import org.springframework.stereotype.Service;

@Service
public class EmployeeValidatorService implements EmployeeValidator{
    private final AgreementValidator agreementValidator;
    public EmployeeValidatorService(AgreementValidator agreementValidator){
        this.agreementValidator = agreementValidator;
    }

    /**
     * Check that employment history, contracts, and certifications of employee have logically correct dates.
     * Validate all agreements using AgreementValidator.validateAgreement()
     *
     * @param employee is employee whose collections are validated.
     */
    @Override
    public void validate(Employee employee) {
        employee.getEmploymentHistory().forEach(eH -> DateValidator.validateIfEarlierIsBeforeLater(eH.getFromDate(), eH.getToDate()));
        employee.getAgreements().forEach(agr -> {
            try {
                agreementValidator.validateAgreement(agr);
            } catch (EmployeeAgreementException e) {
                throw new RuntimeException(e);
            }
        });
        employee.getCertificates().forEach(cert -> DateValidator.validateIfEarlierIsBeforeLater(cert.getIssueDate(), cert.getExpiryDate()));
    }
}
