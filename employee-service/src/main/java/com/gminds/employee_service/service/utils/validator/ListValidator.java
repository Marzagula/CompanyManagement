package com.gminds.employee_service.service.utils.validator;

import java.util.List;

public interface ListValidator<T> {
    void validateList(List<T> list) throws Exception;
}
