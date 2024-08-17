package com.gminds.employee_service.service.validation;

import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;

@Service
public class DateValidatorImpl implements DateValidator {


    @Override
    public void validateIfEarlierIsBeforeLater(LocalDate shouldBeEarlier, LocalDate shouldBeLater) {
        if (shouldBeLater != null && !shouldBeEarlier.isBefore(shouldBeLater)) {
            throw new DateTimeException("End date can't be earlier than start date.");
        }
    }
}
