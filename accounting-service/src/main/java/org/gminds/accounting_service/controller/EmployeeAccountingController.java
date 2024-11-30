package org.gminds.accounting_service.controller;


import org.gminds.accounting_service.model.dtos.PaymentRangeDTO;
import org.gminds.accounting_service.service.EmployeeAccounting;
import org.gminds.accounting_service.service.util.PaymentRangeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeAccountingController {

    private final EmployeeAccounting employeeAccounting;

    public EmployeeAccountingController(EmployeeAccounting employeeAccounting) {
        this.employeeAccounting = employeeAccounting;
    }

    @GetMapping("/paymentRanges")
    public ResponseEntity<List<PaymentRangeDTO>> findAllPaymentRanges() {
        List<PaymentRangeDTO> response = this.employeeAccounting.findAllPaymentRange()
                .stream()
                .map(paymentRange -> PaymentRangeMapper.INSTANCE.toPaymentRangeDTO(paymentRange))
                .toList();
        return ResponseEntity.ok(response);
    }
}
