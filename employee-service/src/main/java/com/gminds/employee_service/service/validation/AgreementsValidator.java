package com.gminds.employee_service.service.validation;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.enums.AgreementStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementsValidator implements ListValidator<EmployeeAgreement> {

    @Override
    /**
     * Validates all agreements.
     *
     * @param agreements all employee agreements
     * */
    public void validate(List<EmployeeAgreement> agreements) throws EmployeeAgreementException {
        long activeAgreementsCount = agreements.stream()
                .filter(agreement -> agreement.getStatus().equals(AgreementStatus.ACTIVE))
                .count();

        if (activeAgreementsCount > 1) {
            throw new EmployeeAgreementException("Employee can't have more than 1 active agreement.");
        }
    }
}
