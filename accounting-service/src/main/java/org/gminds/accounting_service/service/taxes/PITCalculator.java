package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.FiscalValue;
import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.TaxTransaction;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.model.enums.TaxStage;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PITCalculator implements TaxCalculator<Salary> {

    private final TaxRepository taxRepository;
    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    Map<String, Double> fiscalValues;
    List<Tax> taxes = new ArrayList<>();

    public PITCalculator(TaxRepository taxRepository,
                         FiscalValuesRepository fiscalValuesRepository,
                         LedgerAccountRepository ledgerAccountRepository) {
        this.taxRepository = taxRepository;
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
    }

    /*TODO*
       3. subtypy skladek moga byc inne w kolejnych latach podatkowych, trzeba to jakos obsluzyc
       6. trzeba wziac pod uwage to czy podatnik ma skonczone 26 lat
    */
    @Override
    public Double calculateTax(Salary transaction) {
        initTaxes(transaction.getTransactionDate().getYear());
        return predictYearlyPITTax(transaction).doubleValue();
    }

    BigDecimal predictYearlyPITTax(Salary salary) {

        List<Salary> salaries = ledgerAccountRepository.findByAccountNameWithTransactions("UOP").getTransactions()
                .stream()
                .filter(transaction -> transaction instanceof Salary && Objects.equals(((Salary) transaction).getEmployeeId(), salary.getEmployeeId()))
                .map(transaction -> (Salary) transaction)
                .toList();

        BigDecimal currentIncome = BigDecimal.ZERO;
        BigDecimal currentZusBaseSummary = BigDecimal.ZERO;


        BigDecimal predictedYearlyPitTax = new BigDecimal(0);
        int monthNo = salary.getTransactionDate().getMonthValue();
        BigDecimal currentIncomeBasedSummary = BigDecimal.ZERO;
        for (Salary s : salaries) {
            currentIncome = currentIncome.add(BigDecimal.valueOf(s.getAmount()));
            predictedYearlyPitTax = predictedYearlyPitTax.add(getMonthlyPit( currentZusBaseSummary, currentIncomeBasedSummary, BigDecimal.valueOf(salary.getAmount()), s.getTransactionDate().getMonthValue()));
            currentIncomeBasedSummary = currentIncomeBasedSummary.add(
                    BigDecimal.valueOf(s.getAmount())
                            .subtract(getZusTax(BigDecimal.valueOf(s.getAmount()),
                                    currentZusBaseSummary,
                                    s.getTransactionDate().getMonthValue()))
                            .subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs")))
            );
            currentZusBaseSummary = currentZusBaseSummary.add(BigDecimal.valueOf(s.getAmount()));
        }

        for (int i = monthNo; i <= 12; i++) {
            currentIncome = currentIncome.add(BigDecimal.valueOf(salary.getAmount()));

            BigDecimal monthlyPit = getMonthlyPit(currentZusBaseSummary, currentIncomeBasedSummary, BigDecimal.valueOf(salary.getAmount()), i);
            predictedYearlyPitTax = predictedYearlyPitTax.add(monthlyPit);
            currentIncomeBasedSummary = currentIncomeBasedSummary.add(
                    BigDecimal.valueOf(salary.getAmount())
                            .subtract(getZusTax(BigDecimal.valueOf(salary.getAmount()),
                                    currentZusBaseSummary,
                                    i))
                            .subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs")))

            );
            currentZusBaseSummary = currentZusBaseSummary.add(BigDecimal.valueOf(salary.getAmount()));

        }

        return predictedYearlyPitTax.divide(BigDecimal.valueOf(12), 0, RoundingMode.HALF_UP);

    }

    private void initTaxes(int year) {
        fiscalValues = fiscalValuesRepository.findByFiscalYear(year)
                .stream()
                .collect(Collectors.toMap(FiscalValue::getTaxSubtype, FiscalValue::getLimitValue));
        taxes = taxRepository.findByFiscalYear(year);
    }

    BigDecimal getMonthlyPit(BigDecimal currentZusSummary, BigDecimal currentIncomeBasedSummary, BigDecimal incomeInCurrentMonth, int currentMontNo) {

        Map<String, Double> pitPercentages = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.PIT))
                .collect(Collectors.toMap(Tax::getTaxSubcategory, Tax::getPercentage));


        BigDecimal zusTax = getZusTax(incomeInCurrentMonth, currentZusSummary, currentMontNo);
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal nextIncomeBasedSummary = currentIncomeBasedSummary.add(incomeInCurrentMonth.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs"))));

        TaxStage taxStage = TaxStage.BEFORE_THRESHOLD;
        if (currentIncomeBasedSummary.compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) < 0
                && nextIncomeBasedSummary
                .compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) > 0)
            taxStage = TaxStage.EXCEEDING_THESHOLD;
        else if (currentIncomeBasedSummary.compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) > 0)
            taxStage = TaxStage.AFTER_THRESHOLD;


        if (taxStage.equals(TaxStage.BEFORE_THRESHOLD)) {
            tax = tax.add(incomeInCurrentMonth.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs")))
                    .multiply(BigDecimal.valueOf(pitPercentages.get("first_bracket")))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            ).subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
        } else if (taxStage.equals(TaxStage.EXCEEDING_THESHOLD)) {

            BigDecimal difference = nextIncomeBasedSummary.subtract(BigDecimal.valueOf(fiscalValues.get("first_bracket"))).setScale(0, RoundingMode.HALF_UP);
            BigDecimal splitFirst = BigDecimal.valueOf(fiscalValues.get("first_bracket")).subtract(currentIncomeBasedSummary);

            tax = tax
                    .add(splitFirst
                            .multiply(BigDecimal.valueOf(pitPercentages.get("first_bracket")))
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    )
                    .add(difference
                            .multiply(BigDecimal.valueOf(pitPercentages.get("second_bracket"))
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                    .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                            .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
                    .setScale(0, RoundingMode.HALF_UP);
        } else {
            tax = tax.add(
                            (incomeInCurrentMonth
                                    .subtract(zusTax)
                                    .subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs"))))
                                    .multiply(BigDecimal.valueOf(pitPercentages.get("second_bracket")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                    .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                            .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
        }

        return tax;
    }

    BigDecimal getZusTax(BigDecimal income, BigDecimal zusBaseSummary, int monthNo) {
        BigDecimal zusTaxBase;
        List<Double> zusPercentages = taxes.stream()
                .filter(tax ->
                        tax.getTaxCategory().equals(TaxCategory.ZUS) &&
                                (tax.getTaxSubcategory().equals("emerytalna pracownika")
                                        || tax.getTaxSubcategory().equals("rentowa pracownika")
                                        || tax.getTaxSubcategory().equals("chorobowa")
                                )
                )
                .map(Tax::getPercentage)
                .toList();


        if (zusBaseSummary.compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) <= 0
                && income.multiply(BigDecimal.valueOf(monthNo))
                .compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) == 1) {
            zusTaxBase = BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit")).subtract(zusBaseSummary);
        } else if (zusBaseSummary.compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) == 1
        ) {
            zusTaxBase = BigDecimal.ZERO;
        } else if (income.compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) == 1) {
            zusTaxBase = BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"));
        } else
            zusTaxBase = income;

        BigDecimal finalZusTaxBase = zusTaxBase;
        return BigDecimal.valueOf(zusPercentages.stream()
                        .mapToDouble(zusPercentage -> finalZusTaxBase.multiply(BigDecimal.valueOf(zusPercentage / 100.0)).doubleValue())
                        .sum()).setScale(2, RoundingMode.HALF_UP)
                .setScale(0, RoundingMode.HALF_UP);
    }


}
