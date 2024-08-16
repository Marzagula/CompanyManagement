package com.gminds.employee_service.service.validation;

import java.time.LocalDate;

public interface DateValidator<T> {
    void validateIfEarlierIsBeforeLater(LocalDate shouldBeEarlier, LocalDate shouldBeLater);
}


