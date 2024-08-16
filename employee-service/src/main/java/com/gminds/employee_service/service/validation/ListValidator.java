package com.gminds.employee_service.service.validation;

import java.util.List;

public interface ListValidator<T> {
    void validate(List<T> list) throws Exception;
}
