package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.FiscalValue;
import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.TaxTransaction;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZUSCalculator implements TaxCalculator<Salary> {

    private final TaxRepository taxRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final FiscalValuesRepository fiscalValuesRepository;
    Map<String, BigDecimal> fiscalValues;
    List<Tax> taxes;

    public ZUSCalculator(TaxRepository taxRepository,
                         FiscalValuesRepository fiscalValuesRepository,
                         LedgerAccountRepository ledgerAccountRepository) {
        this.taxRepository = taxRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.fiscalValuesRepository = fiscalValuesRepository;

    }

    void init(int year) {
        fiscalValues = fiscalValuesRepository.findByFiscalYear(year)
                .stream()
                .collect(Collectors.toMap(FiscalValue::getTaxSubtype, fiscalValue -> BigDecimal.valueOf(fiscalValue.getLimitValue())));
        taxes = taxRepository.findByFiscalYear(year);
    }

    @Override
    public Double calculateTax(Salary transaction) {
        init(transaction.getTransactionDate().getYear());
        List<TaxTransaction> transactions = ledgerAccountRepository.findByAccountNameWithTransactions("UOP").getTransactions()
                .stream()
                .filter(trans -> trans instanceof TaxTransaction)
                .filter(trans -> ((TaxTransaction) trans).getEmployeeId() == transaction.getEmployeeId())
                .filter(trans -> ((TaxTransaction) trans).getTaxCategory().equals(TaxCategory.ZUS))
                .map(trans -> (TaxTransaction) trans)
                .toList();

        BigDecimal currentZUSBaseSummary = transactions.stream()
                .map(trans -> BigDecimal.valueOf(trans.getTaxBase()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal monthlyIncome = BigDecimal.valueOf(transaction.getAmount());
        BigDecimal employeeZusTax = getEmployeeZusTax(
                monthlyIncome,
                currentZUSBaseSummary,
                transaction.getTransactionDate().getMonthValue()
        );
        BigDecimal employerZusTax = getEmployerZusTax(
                monthlyIncome,
                currentZUSBaseSummary,
                transaction.getTransactionDate().getMonthValue()
        );
        BigDecimal healthTaxPercentage = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.HEALTH))
                .map(tax -> BigDecimal.valueOf(tax.getPercentage()))
                .findFirst().orElseThrow();

        BigDecimal healthTax = (monthlyIncome
                .subtract(employeeZusTax))
                .multiply(healthTaxPercentage
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        return employeeZusTax.add(healthTax).add(employerZusTax)
                .setScale(0, RoundingMode.HALF_UP).doubleValue();
    }


    public BigDecimal getEmployeeZusTax(BigDecimal income, BigDecimal zusBaseSummary, int monthNo) {
        BigDecimal retirementLimit = fiscalValues.get("retirement_contribution_limit");
        BigDecimal zusTaxBase;

        List<BigDecimal> zusPercentages = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.ZUS) &&
                        (tax.getTaxSubcategory().equals("emerytalna pracownika") ||
                                tax.getTaxSubcategory().equals("rentowa pracownika") ||
                                tax.getTaxSubcategory().equals("chorobowa"))
                )
                .map(tax -> BigDecimal.valueOf(tax.getPercentage()))
                .toList();

        if (zusBaseSummary.compareTo(retirementLimit) <= 0 &&
                income.multiply(BigDecimal.valueOf(monthNo)).compareTo(retirementLimit) == 1) {
            zusTaxBase = retirementLimit.subtract(zusBaseSummary);
        } else if (zusBaseSummary.compareTo(retirementLimit) > 0) {
            zusTaxBase = BigDecimal.ZERO;
        } else if (income.compareTo(retirementLimit) > 0) {
            zusTaxBase = retirementLimit;
        } else {
            zusTaxBase = income;
        }

        BigDecimal zusTax = zusPercentages.stream()
                .map(percentage -> zusTaxBase.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return zusTax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getEmployerZusTax(BigDecimal income, BigDecimal zusBaseSummary, int monthNo) {
        BigDecimal retirementLimit = fiscalValues.get("retirement_contribution_limit");
        BigDecimal zusTaxBase;

        List<BigDecimal> zusPercentages = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.ZUS) &&
                        !(tax.getTaxSubcategory().equals("emerytalna pracownika") ||
                                tax.getTaxSubcategory().equals("rentowa pracownika") ||
                                tax.getTaxSubcategory().equals("chorobowa"))
                )
                .map(tax -> BigDecimal.valueOf(tax.getPercentage()))
                .toList();

        if (zusBaseSummary.compareTo(retirementLimit) <= 0 &&
                income.multiply(BigDecimal.valueOf(monthNo)).compareTo(retirementLimit) == 1) {
            zusTaxBase = retirementLimit.subtract(zusBaseSummary);
        } else if (zusBaseSummary.compareTo(retirementLimit) > 0) {
            zusTaxBase = BigDecimal.ZERO;
        } else if (income.compareTo(retirementLimit) > 0) {
            zusTaxBase = retirementLimit;
        } else {
            zusTaxBase = income;
        }

        BigDecimal zusTax = zusPercentages.stream()
                .map(percentage -> zusTaxBase.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return zusTax.setScale(2, RoundingMode.HALF_UP);
    }
}
