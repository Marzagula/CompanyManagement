package com.gminds.employee_service.service.utils;

import com.gminds.employee_service.model.Employee;

public interface EmployeeCollectionStrategy<E> {
    void addToCollection(Employee employee, E entity);
}
