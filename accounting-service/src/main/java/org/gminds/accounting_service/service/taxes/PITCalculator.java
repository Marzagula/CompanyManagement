package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.Tax;
import org.gminds.accounting_service.model.Transaction;
import org.gminds.accounting_service.model.enums.TaxType;
import org.gminds.accounting_service.repository.TaxRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PITCalculator implements TaxCalculator {

    private final TaxRepository taxRepository;

    public PITCalculator(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    /*TODO*
       1. first/second bracket powinien byc wybierany w ramach zarobkow przewidywanych na podstawie
       istniejacych juz Transakcji + istniejaca
       2. progi podatkowe nie powinny byc zaharcodowane, trzeba bedzie umiescic je w bazie
       3. obecnie mozna odliczyc 7,75 z 9 procent skladki zdrowotnej(stąd odjęcie 1.25 w healthTax) ale ta wartosc sie bedzie zmieniac, trzeba to jakos obsluzyc
       4. subtypy skladek moga byc inne w kolejnych latach podatkowych, trzeba to jakos obsluzyc
    */
    @Override
    public Double calculateTax(Transaction transaction) {
        Salary salary = null;
        if (transaction instanceof Salary) {
            salary = (Salary) transaction;
        } else {
            throw new RuntimeException("Transaction should be Salary type but it's not.");
        }
        List<Tax> taxes = taxRepository.findByFiscalYear(transaction.getTransactionDate().getYear());
        List<Double> zusPercentages = taxes.stream()
                .filter(tax ->
                        tax.getTaxType().equals(TaxType.ZUS) &&
                                (tax.getTaxSubtype().equals("emerytalna pracownika")
                                        || tax.getTaxSubtype().equals("rentowa pracownika")
                                        || tax.getTaxSubtype().equals("chorobowa")
                                )
                )
                .map(Tax::getPercentage)
                .toList();

        BigDecimal zusTax = BigDecimal.valueOf(zusPercentages.stream()
                .mapToDouble(zusPercentage -> transaction.getAmount() * zusPercentage / 100.0)
                .sum()).setScale(2, RoundingMode.UP);
        BigDecimal predictedZUSYearlyTax = zusTax.multiply(new BigDecimal(12));
        BigDecimal predictedYearlyIncome = predictionOfYearyIncome(salary);
        Double pitPercentage = taxes.stream()
                .filter(tax ->
                        tax.getTaxType().equals(TaxType.PIT) &&
                                (
                                        tax.getTaxSubtype() != null &&
                                                (transaction.getAmount()*12-predictedZUSYearlyTax.doubleValue() <= 120000.0 ? "first_bracket" : "second_bracket").equals(tax.getTaxSubtype())
                                                //(predictedYearlyIncome.compareTo(BigDecimal.valueOf(120000))<=0 ? "first_bracket" : "second_bracket").equals(tax.getTaxSubtype()) //docelowa linijka
                                )
                )
                .map(Tax::getPercentage)
                .findFirst().orElseThrow();

        Double healthTaxPercentage = (taxes.stream()
                .filter(tax -> tax.getTaxType().equals(TaxType.HEALTH))
                .findFirst().orElseThrow()
                .getPercentage());

        BigDecimal healthTax = BigDecimal.valueOf(healthTaxPercentage - 1.25)
                .multiply(BigDecimal.valueOf(transaction.getAmount()).subtract(zusTax)).setScale(2, RoundingMode.UP)
                .divide(new BigDecimal(100)).setScale(2, RoundingMode.UP);


        return (BigDecimal.valueOf(transaction.getAmount()).subtract(zusTax))
                .multiply(BigDecimal.valueOf(pitPercentage).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UP))
                .subtract(healthTax).doubleValue();
    }


    /*TODO tu bedzie logika odpowiadajaca za sprawdzenie wszystkich wyplat pracownika z tego roku i przewidzenie rocznego dochodu*/
    BigDecimal predictionOfYearyIncome(Salary salary){
        return new BigDecimal(0);
    }
}
