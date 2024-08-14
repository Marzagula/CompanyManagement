package com.gminds.employee_service.controller;


import com.gminds.employee_service.model.EmployeeUser;
import com.gminds.employee_service.repository.EmployeeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    EmployeeUserRepository repository;
    @GetMapping("/getUsers")
    List<EmployeeUser> findAllUsers(){
        return repository.findAll();
    }

}
