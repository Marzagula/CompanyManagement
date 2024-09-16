package org.gminds.accounting_service.service;

import org.gminds.accounting_service.model.PaymentRange;
import org.gminds.accounting_service.repository.PaymentRangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeAccounting {

    final private PaymentRangeRepository paymentRangeRepository;

    public EmployeeAccounting(PaymentRangeRepository paymentRangeRepository){
        this.paymentRangeRepository = paymentRangeRepository;
    }

    public List<PaymentRange> findAllPaymentRange(){
        return paymentRangeRepository.findAll().stream().toList();
    }
}
