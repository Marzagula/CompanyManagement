package com.gminds.employee_service.controller;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.dtos.JobDTO;
import com.gminds.employee_service.service.JobService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class JobController {
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;

    }

    @PutMapping("/{id}/changeJob")
    ResponseEntity<JobDTO> updateEmployeeJob(@PathVariable Long id, @RequestBody @Valid JobDTO jobDTO) throws ResourceNotFoundException, EmployeeAgreementException {
        logger.info("Received valid request to change job for employee with id: {} and data: {}", id, jobDTO);
        return ResponseEntity.status(HttpStatus.OK).body(jobService.updateEmployeeJob(id, jobDTO));
    }
}
