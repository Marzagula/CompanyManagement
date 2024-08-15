package com.gminds.employee_service.controller;

import com.gminds.employee_service.exceptions.EmployeeException;
import com.gminds.employee_service.exceptions.JobException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    ResponseEntity<Page<EmployeeDTO>> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.findAllEmployees(page,size));
    }

    @PostMapping
    ResponseEntity<String> addEmployee(@RequestBody Employee e){
        employeeService.addEmployee(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(e.getName() + " is hired.");
    }

    @PutMapping("/{id}/changeJob")
    ResponseEntity<EmployeeDTO> changeJob(@PathVariable Long id, @RequestBody Job job) throws EmployeeException, JobException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployeeJob(id, job));
    }
}
