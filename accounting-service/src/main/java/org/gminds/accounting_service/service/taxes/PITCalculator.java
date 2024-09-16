package org.gminds.accounting_service.service.taxes;

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

    @Override
    public Double calculateTax(Transaction transaction) {
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
                .sum()).setScale(2,RoundingMode.UP);
        BigDecimal predictedZUSYearlyTax = zusTax.multiply(new BigDecimal(12));

        Double pitPercentage = taxes.stream()
                .filter(tax ->
                        tax.getTaxType().equals(TaxType.PIT) &&
                                (
                                        tax.getTaxSubtype() != null &&
                                                (predictedZUSYearlyTax.doubleValue() <= 120000.0 ? "first_bracket" : "second_bracket").equals(tax.getTaxSubtype())
                                )
                )
                .map(Tax::getPercentage)
                .findFirst().orElseThrow();

        Double healthTaxPercentage = (taxes.stream()
                .filter(tax -> tax.getTaxType().equals(TaxType.HEALTH))
                .findFirst().orElseThrow()
                .getPercentage());

        BigDecimal healthTax = BigDecimal.valueOf(healthTaxPercentage-1.25)
                .multiply(BigDecimal.valueOf(transaction.getAmount()).subtract(zusTax)).setScale(2,RoundingMode.UP)
                .divide(new BigDecimal(100)).setScale(2, RoundingMode.UP);


        return (BigDecimal.valueOf(transaction.getAmount()).subtract(zusTax))
                .multiply(BigDecimal.valueOf(pitPercentage).divide(BigDecimal.valueOf(100)).setScale(2,RoundingMode.UP))
                .subtract(healthTax).doubleValue();
    }
}
