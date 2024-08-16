package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractTransactionalService<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public T executeChanges(T dto) {
        return executeChanges(null, dto);
    }

    @Transactional
    public T executeChanges(Long id, T dto) {
        try {
            Employee employee = findEmployeeById(id);
            return processTransaction(employee, dto);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Using implemented EmployeeRepository to find Employee by ID.
     *
     * @param id is ID of employee in database.
     */
    protected abstract Employee findEmployeeById(Long id);

    protected abstract T processTransaction(Employee employee, T dto) throws Exception;

    protected <E> void addEntityToEmployeeCollection(Employee employee, E entity, Consumer<Employee> employeeSetter, Collection<E> collection) {
        collection.add(entity);
        employeeSetter.accept(employee);
    }
}
