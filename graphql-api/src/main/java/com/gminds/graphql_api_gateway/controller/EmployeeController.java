package com.gminds.graphql_api_gateway.controller;

import com.gminds.graphql_api_gateway.model.dtos.EmployeeDTO;
import com.gminds.graphql_api_gateway.service.EmployeeService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmployeeController implements GraphQLQueryResolver {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @QueryMapping
    public Map<String, Object> getEmployeesPaged(@Argument int page, @Argument int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmployeeDTO> employeePage = employeeService.getAllEmployeesPaged(pageRequest);

        Map<String, Object> result = new HashMap<>();
        result.put("content", employeePage.getContent());
        result.put("pageNumber", employeePage.getNumber());
        result.put("pageSize", employeePage.getSize());
        result.put("totalPages", employeePage.getTotalPages());
        result.put("totalElements", employeePage.getTotalElements());

        return result;

    }

    @QueryMapping
    public EmployeeDTO getEmployeeById(@Argument Long id) {
        logger.debug("Fetching employee with id: {}", id);
        return employeeService.getEmployeeById(id);
    }

    @MutationMapping
    public EmployeeDTO createEmployee(@Argument("employee") EmployeeDTO employeeDTO){
        logger.debug("Creating employee with name: {} {}", employeeDTO.name(),employeeDTO.surname());
        return employeeService.createEmployee(employeeDTO);
    }
}
