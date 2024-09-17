package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.FiscalValue;
import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PITCalculator implements TaxCalculator<Salary> {

    private final TaxRepository taxRepository;
    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;

    public PITCalculator(TaxRepository taxRepository,
                         FiscalValuesRepository fiscalValuesRepository,
                         LedgerAccountRepository ledgerAccountRepository) {
        this.taxRepository = taxRepository;
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
    }

    /*TODO*
       1. first/second bracket powinien byc wybierany w ramach zarobkow przewidywanych na podstawie
       istniejacych juz Transakcji + istniejaca
       2. progi podatkowe nie powinny byc zaharcodowane, trzeba bedzie umiescic je w bazie
       3. subtypy skladek moga byc inne w kolejnych latach podatkowych, trzeba to jakos obsluzyc
       4. algorytm podatkowy musi dzialac progresywnie (do 120000 17% a i powyzej 32%) obecnie jest staly
       5. trzeba wziac pod uwage kwote wolna od podatku
       6. trzeba wziac pod uwage to czy podatnik ma skonczone 26 lat
    */
    @Override
    public Double calculateTax(Salary transaction) {
        return predictYearlyPITTax(transaction).doubleValue();
    }


    /*TODO tu bedzie logika odpowiadajaca za sprawdzenie wszystkich wyplat pracownika z tego roku i przewidzenie rocznego dochodu*/
    BigDecimal predictYearlyPITTax(Salary salary) {


        Map<String, Double> fiscalValues = fiscalValuesRepository.findByFiscalYear(2024)
                .stream()
                .collect(Collectors.toMap(FiscalValue::getTaxSubtype, FiscalValue::getLimitValue));

        List<Salary> salaries = ledgerAccountRepository.findByAccountNameWithTransactions("UOP").getTransactions()
                .stream()
                .filter(transaction -> Objects.equals(((Salary) transaction).getEmployeeId(), salary.getEmployeeId()))
                .map(transaction -> (Salary) transaction)
                .toList();


        List<Tax> taxes = taxRepository.findByFiscalYear(salary.getTransactionDate().getYear());
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
        Map<String, Double> pitPercentages = taxes.stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.PIT))
                .collect(Collectors.toMap(Tax::getTaxSubcategory, Tax::getPercentage));

        BigDecimal currentIncome = BigDecimal.valueOf(salaries.stream().mapToDouble(Salary::getAmount).sum());
        BigDecimal predictedMonthlyIncome = currentIncome.add(BigDecimal.valueOf(salary.getAmount())).divide(BigDecimal.valueOf((salaries.size() + 1)), 2, RoundingMode.HALF_UP);
        BigDecimal predictedTax = new BigDecimal(0);
        int monthNo = salary.getTransactionDate().getMonthValue();
        for (Salary s : salaries) {
            BigDecimal zusTaxBase = currentIncome.add(predictedMonthlyIncome).compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) > 0
                    ? BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit")).subtract(currentIncome)
                    : predictedMonthlyIncome;

            BigDecimal zusTax = BigDecimal.valueOf(zusPercentages.stream()
                    .mapToDouble(zusPercentage -> zusTaxBase.multiply(BigDecimal.valueOf(zusPercentage / 100.0)).doubleValue())
                    .sum()).setScale(2, RoundingMode.HALF_UP);
            predictedTax = predictedTax.add(predictedMonthlyIncome.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs")))
                    .multiply(BigDecimal.valueOf(pitPercentages.get("first_bracket")))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                            .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))).setScale(0, RoundingMode.HALF_UP);
        }

        for (int i = monthNo - 1; i < 12; i++) {
            BigDecimal zusTaxBase = BigDecimal.ZERO;
            if (currentIncome.doubleValue() <= fiscalValues.get("retirement_contribution_limit"))
                zusTaxBase = currentIncome.add(predictedMonthlyIncome).compareTo(BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit"))) > 0
                        ? BigDecimal.valueOf(fiscalValues.get("retirement_contribution_limit")).subtract(currentIncome)
                        : predictedMonthlyIncome;

            BigDecimal finalZusTaxBase = zusTaxBase;
            BigDecimal zusTax = BigDecimal.valueOf(zusPercentages.stream()
                    .mapToDouble(zusPercentage -> finalZusTaxBase.multiply(BigDecimal.valueOf(zusPercentage / 100.0)).doubleValue())
                    .sum()).setScale(2, RoundingMode.HALF_UP);


            if (predictedMonthlyIncome.multiply(BigDecimal.valueOf(i)).compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) <= 0) {
                predictedTax = predictedTax.add(predictedMonthlyIncome.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs")))
                        .multiply(BigDecimal.valueOf(pitPercentages.get("first_bracket")))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))).setScale(0, RoundingMode.HALF_UP);
            } else if (predictedMonthlyIncome.multiply(BigDecimal.valueOf(i)).compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) >= 0 && i > 0 && predictedMonthlyIncome.multiply(BigDecimal.valueOf(i - 1)).compareTo(BigDecimal.valueOf(fiscalValues.get("first_bracket"))) < 0) {
                BigDecimal difference = predictedMonthlyIncome.multiply(BigDecimal.valueOf(i - 1))
                        .add(predictedMonthlyIncome.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs"))))
                        .subtract(BigDecimal.valueOf(fiscalValues.get("first_bracket"))).setScale(0, RoundingMode.HALF_UP);

                String test0 = "";
                predictedTax = predictedTax
                        .add(predictedMonthlyIncome.subtract(zusTax).subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs"))).subtract(difference)
                                .multiply(BigDecimal.valueOf(pitPercentages.get("first_bracket")))
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        ).setScale(0, RoundingMode.HALF_UP)

                        .add(difference
                                .multiply(BigDecimal.valueOf(pitPercentages.get("second_bracket"))
                                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                        .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction"))
                                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
                String test = "";
            } else {
                predictedTax = predictedTax.add(
                        (predictedMonthlyIncome
                                .subtract(zusTax)
                                .subtract(BigDecimal.valueOf(fiscalValues.get("employee_costs"))))
                                .multiply(BigDecimal.valueOf(pitPercentages.get("second_bracket")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                                .subtract(BigDecimal.valueOf(fiscalValues.get("maximum_deduction")).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))).setScale(0, RoundingMode.HALF_UP);
            }
            String test = "";

        }


        return predictedTax.divide(BigDecimal.valueOf(12), 0, RoundingMode.HALF_UP);

    }


}
