package com.gminds.employee_service.service.validation;

import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateValidatorImpl implements DateValidator<EmployeeAgreementDTO> {


    @Override
    public void validateIfEarlierIsBeforeLater(LocalDate shouldBeEarlier, LocalDate shouldBeLater) {
        if (shouldBeLater != null && !shouldBeEarlier.isBefore(shouldBeLater)) {
            throw new DateTimeException("End date can't be earlier than start date.");
        }
    }
}
