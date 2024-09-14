package com.gminds.employee_service.controller;


import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.service.ExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ExampleService exampleService;

    public PublicController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/getUsers")
    ResponseEntity<List<Employee>> findAllUsers() {
        return ResponseEntity.ok(exampleService.findAllEmployees());
    }

}
