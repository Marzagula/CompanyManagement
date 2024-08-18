package com.gminds.employee_service.controller;

import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import com.gminds.employee_service.service.employee.DefaultCertificateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class CertificateController {
    private final DefaultCertificateService certificateService;

    public CertificateController(DefaultCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/{id}/certificate")
    ResponseEntity<String> addCertificateToEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeCertificateDTO employeeCertificateDTO) {
        certificateService.addCertificateToEmployee(id, employeeCertificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Certificate has been added to employee with id: " + id);
    }

}
