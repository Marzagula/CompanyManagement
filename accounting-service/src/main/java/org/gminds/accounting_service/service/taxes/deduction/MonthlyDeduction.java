package org.gminds.accounting_service.service.taxes.deduction;

import org.gminds.accounting_service.model.SalaryTransactionItem;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.service.taxes.TaxDeduction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MonthlyDeduction implements TaxDeduction<SalaryTransactionItem> {
    private final FiscalValuesRepository fiscalValuesRepository;

    public MonthlyDeduction(FiscalValuesRepository fiscalValuesRepository) {
        this.fiscalValuesRepository = fiscalValuesRepository;
    }

    @Override
    public BigDecimal calculateDeduction(SalaryTransactionItem transaction) {
        return fiscalValuesRepository.findByFiscalYear(2024)
                .stream()
                .filter(fiscalValue -> fiscalValue.getFiscalValueSubtype().equals("maximum_deduction"))
                .map(fiscalValue -> BigDecimal.valueOf(fiscalValue.getLimitValue()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
                .findFirst().orElse(BigDecimal.ZERO);
    }

    @Override
    public FiscalValueType getFiscalValueType() {
        return FiscalValueType.TAX_DEDUCTION;
    }

    @Override
    public String getFiscalValueSubtype() {
        return "maximum_deduction";
    }
}
