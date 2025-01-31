package com.gminds.employee_service.controller;

import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.service.employee.EmployeeService;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeMapper.INSTANCE.toEmployeeDTO(employeeService.findEmployeeById(id)));
    }

    @GetMapping
    ResponseEntity<Page<EmployeeDTO>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.findAllEmployeesPaginated(PageRequest.of(page, size)));
    }

    @PostMapping
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<EmployeeDTO> updatePersonalInformation(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updatePersonalInformation(id, employeeDTO));
    }
}
