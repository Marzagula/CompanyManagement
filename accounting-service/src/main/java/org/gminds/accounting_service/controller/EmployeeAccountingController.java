package org.gminds.accounting_service.controller;

import org.gminds.accounting_service.model.PaymentRange;
import org.gminds.accounting_service.service.EmployeeAccounting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeAccountingController {

    private final EmployeeAccounting employeeAccounting;

    public EmployeeAccountingController(EmployeeAccounting employeeAccounting){
        this.employeeAccounting = employeeAccounting;
    }

    @GetMapping("/paymentRanges")
    public ResponseEntity<List<PaymentRange>> findAllPaymentRanges(){
        return ResponseEntity.ok(this.employeeAccounting.findAllPaymentRange());
    }
}
