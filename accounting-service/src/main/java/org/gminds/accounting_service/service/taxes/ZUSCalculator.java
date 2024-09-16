package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.enums.TaxType;
import org.gminds.accounting_service.repository.TaxRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return taxes.stream()
                .filter(tax -> tax.getTaxType().equals(TaxType.ZUS) || tax.getTaxType().equals(TaxType.HEALTH))
                .map(tax -> BigDecimal.valueOf(transaction.getAmount())
                        .multiply(BigDecimal.valueOf(tax.getPercentage()).divide(BigDecimal.valueOf(100)))
                        .setScale(2, RoundingMode.HALF_UP))
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }
}
