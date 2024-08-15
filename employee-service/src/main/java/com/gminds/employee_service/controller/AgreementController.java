package com.gminds.employee_service.controller;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import com.gminds.employee_service.service.AgreementService;
import com.gminds.employee_service.util.mappers.EmployeeAgreementMapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class AgreementController {
    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/agreement")
    public ResponseEntity<EmployeeAgreementDTO> newAgreement(@RequestBody EmployeeAgreement employeeAgreement) throws EmployeeAgreementException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmployeeAgreementMapperImpl
                        .INSTANCE
                        .toEmployeeAgreementDTO(
                                this.agreementService
                                        .terminatePreviousAndAddNewAgreement(employeeAgreement)
                        ));
    }
}
