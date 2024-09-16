package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.Transaction;
import org.gminds.accounting_service.model.enums.TaxType;
import org.gminds.accounting_service.repository.TaxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZUSCalculator implements TaxCalculator<Salary> {

    private final TaxRepository taxRepository;

    public ZUSCalculator(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    @Override
    public Double calculateTax(Salary transaction) {
        List<Tax> taxes = taxRepository.findByFiscalYear(transaction.getTransactionDate().getYear());
        List<Double> taxPercentages = taxes.stream()
                .filter(tax ->
                        tax.getTaxType().equals(TaxType.ZUS)
                                || tax.getTaxType().equals(TaxType.HEALTH)
                )
                .map(Tax::getPercentage)
                .toList();
        return taxPercentages.stream()
                .mapToDouble(taxPercentage -> transaction.getAmount() * (taxPercentage / 100))
                .sum();
    }
}
