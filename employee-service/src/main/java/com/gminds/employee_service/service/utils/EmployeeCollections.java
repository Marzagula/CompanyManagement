package com.gminds.employee_service.service.utils;

import com.gminds.employee_service.model.Employee;

import java.util.Collection;
import java.util.function.Consumer;

public interface EmployeeCollections extends EmployeeProperty {

    default <E> void addEntityToEmployeeCollection(Employee employee, E entity, Consumer<Employee> employeeSetter, Collection<E> collection) {
        collection.add(entity);
        employeeSetter.accept(employee);
    }
}
