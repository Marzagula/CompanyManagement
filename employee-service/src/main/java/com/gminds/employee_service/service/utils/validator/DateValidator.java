package com.gminds.employee_service.service.utils.validator;


import java.time.DateTimeException;
import java.time.LocalDate;

public class DateValidator {
    public static void validateIfEarlierIsBeforeLater(LocalDate shouldBeEarlier, LocalDate shouldBeLater) {
        if (shouldBeLater != null && !shouldBeEarlier.isBefore(shouldBeLater)) {
            throw new DateTimeException("End date (" + shouldBeLater + ") can't be earlier than start date (" + shouldBeEarlier + ").");
        }
    }
}
