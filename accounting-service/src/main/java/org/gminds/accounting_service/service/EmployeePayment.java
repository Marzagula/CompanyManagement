package org.gminds.accounting_service.service;

import org.gminds.accounting_service.model.*;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.service.taxes.PITCalculator;
import org.gminds.accounting_service.service.taxes.TaxDeduction;
import org.gminds.accounting_service.service.taxes.ZUSCalculator;
import org.gminds.accounting_service.service.taxes.builder.TaxTransactionBuilder;
import org.gminds.accounting_service.service.taxes.factory.DeductionFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayment implements Payment {
    /*rozlozenie placy na zus, podatek dochodowy, vat i pozostala czesc(netto)
     * stworzenie osobnych transakcji dla kazdej z tych czesci,
     * kazda powinna byc obsluzona na osobnym ledger account
     * transakcje do zus i US powinny byc poprawnie obsluzone
     * */
    private final PITCalculator pitCalculator;
    private final ZUSCalculator zusCalculator;
    private final DeductionFactory<Salary> deductionFactory;
    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private LedgerAccount ledgerAccount;

    public EmployeePayment(PITCalculator pitCalculator,
                           ZUSCalculator zusCalculator,
                           DeductionFactory<Salary> deductionFactory,
                           FiscalValuesRepository fiscalValuesRepository,
                           LedgerAccountRepository ledgerAccountRepository) {
        this.pitCalculator = pitCalculator;
        this.zusCalculator = zusCalculator;
        this.deductionFactory = deductionFactory;
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;

    }

    @Override
    public void makePayment(Transaction transaction) {
        //int fiscalYear = transaction.getTransactionDate().getYear();
        if (transaction instanceof Salary) {
            ledgerAccount = ledgerAccountRepository.findByAccountNameWithTransactions("UOP");
            createPitTransaction((Salary) transaction);
            createZusTransaction((Salary) transaction);
            ledgerAccountRepository.save(ledgerAccount);
        }
        /*TODO to tu beda sie odbywac wszelkie odliczenia od podatku ktory zostanie najpierw policzony w calosci*/

    }

    void createPitTransaction(Salary transaction) {
        BigDecimal totalDeduction = BigDecimal.ZERO;
        BigDecimal pitTax;
        List<FiscalValue> deductionFiscalValues = fiscalValuesRepository.findByFiscalYear(transaction.getTransactionDate().getYear())
                .stream()
                .filter(fiscalValue -> fiscalValue.getFiscalValueType().equals(FiscalValueType.TAX_DEDUCTION))
                .toList();
        List<TaxDeduction<Salary>> taxDeductions = new ArrayList<>();
        deductionFiscalValues.forEach(fiscalValue -> {
            taxDeductions.add(deductionFactory.getProcessor(fiscalValue.getFiscalValueType(), fiscalValue.getFiscalValueSubtype()));
        });

        totalDeduction = taxDeductions.stream()
                .map(salaryTaxDeduction -> salaryTaxDeduction.calculateDeduction(transaction))
                .reduce(totalDeduction, BigDecimal::add);

        pitTax = pitCalculator.calculateTax(transaction);

        BigDecimal pitTaxAmount = pitTax.compareTo(totalDeduction) >= 0 ? pitTax.subtract(totalDeduction) : BigDecimal.ZERO;
        String pitDescription = "PIT tax for employee with ID: " + transaction.getEmployeeId();
        BigDecimal transAmount = BigDecimal.valueOf(transaction.getAmount());
        BigDecimal taxBase = transAmount.subtract(zusCalculator.getEmployeeZusPart().multiply(transAmount));
        TaxTransaction pitTransaction = new TaxTransactionBuilder()
                .setTransactionDate(transaction.getTransactionDate())
                .setEmployeeId(transaction.getEmployeeId())
                .setAmount(pitTaxAmount.doubleValue())
                .setDescription(pitDescription)
                .setTaxBase(taxBase.doubleValue())
                .setTaxCategory(TaxCategory.PIT)
                .getTaxTransaction();

        ledgerAccount.getTransactions().add(pitTransaction);
    }

    void createZusTransaction(Salary transaction) {

        BigDecimal zusTaxAmount = zusCalculator.calculateTax(transaction);
        String zusDescription = "Full ZUS + HEALTH tax for employee with ID: " + transaction.getEmployeeId();

        TaxTransaction zusTransaction = new TaxTransactionBuilder()
                .setTransactionDate(transaction.getTransactionDate())
                .setEmployeeId(transaction.getEmployeeId())
                .setAmount(zusTaxAmount.doubleValue())
                .setDescription(zusDescription)
                .setTaxBase(transaction.getAmount())
                .setTaxCategory(TaxCategory.PIT)
                .getTaxTransaction();

        ledgerAccount.getTransactions().add(zusTransaction);
    }
}
