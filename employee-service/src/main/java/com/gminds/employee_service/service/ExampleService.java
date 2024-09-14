package com.gminds.employee_service.service;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {
    private final EmployeeRepository repository;

    public ExampleService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> findAllEmployees(){
        return repository.findAll();
    }

}
