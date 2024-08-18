package com.gminds.employee_service.controller;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.service.agreement.AgreementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class AgreementController {
    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/{id}/agreement")
    public ResponseEntity<EmployeeAgreementDTO> newAgreement(@PathVariable Long id, @RequestBody @Valid EmployeeAgreementDTO employeeAgreement) throws EmployeeAgreementException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.agreementService
                        .newAgreement(id, employeeAgreement));
    }
}
