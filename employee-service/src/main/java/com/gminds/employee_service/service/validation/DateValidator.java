package com.gminds.employee_service.service.validation;

import java.time.LocalDate;

public interface DateValidator {
    void validateIfEarlierIsBeforeLater(LocalDate shouldBeEarlier, LocalDate shouldBeLater);
}


