package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.FiscalValue;
import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PITCalculator implements TaxCalculator<Salary> {

    private final TaxRepository taxRepository;
    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final ZUSCalculator zusCalculator;
    Map<String, BigDecimal> fiscalValues;
    List<Tax> taxes;

    public PITCalculator(TaxRepository taxRepository,
                         FiscalValuesRepository fiscalValuesRepository,
                         LedgerAccountRepository ledgerAccountRepository,
                         ZUSCalculator zusCalculator) {
        this.taxRepository = taxRepository;
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.zusCalculator = zusCalculator;
    }

    /*TODO*
       6. trzeba wziac pod uwage to czy podatnik ma skonczone 26 lat
    */
    @Override
    public BigDecimal calculateTax(Salary transaction) {
        initTaxes(transaction.getTransactionDate().getYear());
        return calculateMonthlyPitTax(transaction);
    }

    private BigDecimal calculateMonthlyPitTax(Salary salary) {
        List<Salary> salaries = ledgerAccountRepository.findByAccountNameWithTransactions("UOP").getTransactions()
                .stream()
                .filter(transaction -> transaction instanceof Salary && Objects.equals(((Salary) transaction).getEmployeeId(), salary.getEmployeeId()))
                .map(transaction -> (Salary) transaction)
                .toList();

        BigDecimal currentIncome = BigDecimal.ZERO;
        BigDecimal currentZusBaseSummary = BigDecimal.ZERO;


        BigDecimal predictedYearlyPitTax = BigDecimal.ZERO;
        int monthNo = salary.getTransactionDate().getMonthValue();
        BigDecimal currentIncomeBasedSummary = BigDecimal.ZERO;
        for (Salary s : salaries) {
            currentIncome = currentIncome.add(BigDecimal.valueOf(s.getAmount()));
            predictedYearlyPitTax = predictedYearlyPitTax.add(getMonthlyPit(currentZusBaseSummary, currentIncomeBasedSummary, BigDecimal.valueOf(salary.getAmount()), s.getTransactionDate().getMonthValue()));
            currentIncomeBasedSummary = currentIncomeBasedSummary.add(
                    BigDecimal.valueOf(s.getAmount())
                            .subtract(zusCalculator.getEmployeeZusTax(BigDecimal.valueOf(s.getAmount()),
                                    currentZusBaseSummary,
                                    s.getTransactionDate().getMonthValue()))
                    //.subtract(fiscalValues.get("employee_costs"))
            );
            currentZusBaseSummary = currentZusBaseSummary.add(BigDecimal.valueOf(s.getAmount()));
        }

        for (int i = monthNo; i <= 12; i++) {
            currentIncome = currentIncome.add(BigDecimal.valueOf(salary.getAmount()));

            BigDecimal monthlyPit = getMonthlyPit(currentZusBaseSummary, currentIncomeBasedSummary, BigDecimal.valueOf(salary.getAmount()), i);
            predictedYearlyPitTax = predictedYearlyPitTax.add(monthlyPit);
            currentIncomeBasedSummary = currentIncomeBasedSummary.add(
                    BigDecimal.valueOf(salary.getAmount())
                            .subtract(zusCalculator.getEmployeeZusTax(BigDecimal.valueOf(salary.getAmount()),
                                    currentZusBaseSummary,
                                    i))
                    //.subtract(fiscalValues.get("employee_costs"))

            );
            currentZusBaseSummary = currentZusBaseSummary.add(BigDecimal.valueOf(salary.getAmount()));

        }

        return predictedYearlyPitTax.divide(BigDecimal.valueOf(12), 0, RoundingMode.HALF_UP);

    }

    private void initTaxes(int year) {
        fiscalValues = fiscalValuesRepository.findByFiscalYear(year)
                .stream()
                .collect(Collectors.toMap(FiscalValue::getFiscalValueSubtype, fiscalValue -> BigDecimal.valueOf(fiscalValue.getLimitValue())));
        taxes = taxRepository.findByFiscalYear(year);
    }

    private BigDecimal getMonthlyPit(BigDecimal currentZusSummary, BigDecimal currentIncomeBasedSummary, BigDecimal incomeInCurrentMonth, int currentMonthNo) {

        BigDecimal zusTax = zusCalculator.getEmployeeZusTax(incomeInCurrentMonth, currentZusSummary, currentMonthNo);
        BigDecimal adjustedIncome = incomeInCurrentMonth.subtract(zusTax)/*.subtract(fiscalValues.get("employee_costs"))*/;
        /*BigDecimal monthlyDeduction = fiscalValues.get("maximum_deduction").divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);*/
        BigDecimal calculatedTax;

        BigDecimal firstBracketPercentage = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.PIT) && tax.getTaxSubcategory().equals("first_bracket"))
                .map(Tax::getPercentage)
                .map(BigDecimal::valueOf)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal secondBracketPercentage = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.PIT) && tax.getTaxSubcategory().equals("second_bracket"))
                .map(Tax::getPercentage)
                .map(BigDecimal::valueOf)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal nextIncomeBasedSummary = currentIncomeBasedSummary.add(adjustedIncome);

        if (currentIncomeBasedSummary.compareTo(fiscalValues.get("first_bracket")) > 0) {
            calculatedTax = adjustedIncome
                    .multiply(secondBracketPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (nextIncomeBasedSummary.compareTo(fiscalValues.get("first_bracket")) > 0) {
            BigDecimal splitFirstBracket = fiscalValues.get("first_bracket").subtract(currentIncomeBasedSummary);
            BigDecimal difference = nextIncomeBasedSummary.subtract(fiscalValues.get("first_bracket"));

            calculatedTax = splitFirstBracket
                    .multiply(firstBracketPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    .add(difference.multiply(secondBracketPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else {
            calculatedTax = adjustedIncome
                    .multiply(firstBracketPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        return calculatedTax/*.subtract(monthlyDeduction)*/.setScale(0, RoundingMode.HALF_UP);
    }


}
