package org.gminds.accounting_service.service.taxes.deduction;

import org.gminds.accounting_service.model.SalaryTransactionItem;
import org.gminds.accounting_service.model.TaxTransactionItem;
import org.gminds.accounting_service.model.Transaction;
import org.gminds.accounting_service.model.TransactionItem;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.model.enums.employee.AgreementStatus;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.repository.TaxRepository;
import org.gminds.accounting_service.service.CachedEmployeeService;
import org.gminds.accounting_service.service.taxes.PITCalculator;
import org.gminds.accounting_service.service.taxes.TaxDeduction;
import org.gminds.accounting_service.service.taxes.ZUSCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class Under26AgeDeduction implements TaxDeduction<SalaryTransactionItem> {

    private final CachedEmployeeService cachedEmployeeService;
    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final PITCalculator pitCalculator;
    private final ZUSCalculator zusCalculator;
    private final TaxRepository taxRepository;
    private final String fiscalValueSubtype = "tax_free_under26";

    public Under26AgeDeduction(CachedEmployeeService cachedEmployeeService,
                               FiscalValuesRepository fiscalValuesRepository,
                               LedgerAccountRepository ledgerAccountRepository,
                               PITCalculator pitCalculator,
                               ZUSCalculator zusCalculator,
                               TaxRepository taxRepository) {
        this.cachedEmployeeService = cachedEmployeeService;
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.pitCalculator = pitCalculator;
        this.zusCalculator = zusCalculator;
        this.taxRepository = taxRepository;
    }

    @Override
    public BigDecimal calculateDeduction(SalaryTransactionItem transaction) {
        Map<String, BigDecimal> taxes = taxRepository.findByFiscalYear(transaction.getTransactionDate().getYear())
                .stream()
                .filter(tax -> tax.getTaxCategory().equals(TaxCategory.PIT))
                .collect(Collectors.toMap(tax -> tax.getTaxSubcategory(), tax -> BigDecimal.valueOf(tax.getPercentage())));

        Map<String, BigDecimal> fiscalValues = fiscalValuesRepository.findByFiscalYear(transaction.getTransactionDate().getYear())
                .stream()
                .collect(Collectors.toMap(
                        fiscalValue -> fiscalValue.getFiscalValueSubtype(),
                        fiscalValue -> BigDecimal.valueOf(fiscalValue.getLimitValue())
                ));
        List<Transaction> transactionItems = ledgerAccountRepository.findByAccountNameWithTransactions("UOP").getTransactions();

        List<SalaryTransactionItem> salaries = transactionItems
                .stream()
                .filter(trans -> trans instanceof SalaryTransactionItem && Objects.equals(((SalaryTransactionItem) trans).getEmployeeId(), transaction.getEmployeeId()))
                .map(trans -> (SalaryTransactionItem) trans)
                .toList();
        List<TaxTransactionItem> zusTransaction = transactionItems
                .stream()
                .filter(trans -> trans instanceof TaxTransactionItem && Objects.equals(((TaxTransactionItem) trans).getEmployeeId(), transaction.getEmployeeId()))
                .map(trans -> (TaxTransactionItem) trans)
                .filter(taxTransaction -> taxTransaction.getTaxCategory().equals(TaxCategory.ZUS))
                .toList();
        BigDecimal zusBaseSummary = zusTransaction.stream()
                .map(zus -> BigDecimal.valueOf(zus.getTaxBase()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal prevSum = BigDecimal.valueOf(salaries.stream().mapToDouble(salary -> salary.getAmount()).sum());
        BigDecimal incomeSum = prevSum.add(BigDecimal.valueOf(transaction.getAmount()));
        BigDecimal under26AgeDeduction = fiscalValues.get(fiscalValueSubtype);

        if (checkIfUnder26ClauseInForce(transaction, transaction.getTransactionDate())
        ) {
            if (incomeSum.compareTo(under26AgeDeduction) <= 0) {
                return pitCalculator.calculateTax(transaction);
            } else if (prevSum.compareTo(under26AgeDeduction) <= 0
                    && incomeSum.compareTo(under26AgeDeduction) > 0) {
                zusBaseSummary = zusBaseSummary.add(BigDecimal.valueOf(transaction.getAmount()).multiply(zusCalculator.getEmployeeZusPart())/*.multiply(fiscalValues.get("maximum_deduction"))*/.multiply(BigDecimal.valueOf(12)));
                BigDecimal pitPercentage = zusBaseSummary.compareTo(fiscalValues.get("first_bracket")) <= 0 ? taxes.get("first_bracket") : taxes.get("second_bracket");
                BigDecimal difference = incomeSum.subtract(under26AgeDeduction);
                return pitCalculator.calculateTax(transaction).subtract((difference.multiply(zusCalculator.getEmployeeZusPart())).multiply(pitPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }
        return BigDecimal.ZERO;
    }

    boolean checkIfUnder26ClauseInForce(SalaryTransactionItem salaryTransactionItem, LocalDate transactionDate) {
        return cachedEmployeeService.getCachedEmployees()
                .stream()
                .filter(employeeDTO -> Objects.equals(employeeDTO.id(), salaryTransactionItem.getEmployeeId()))
                .flatMap(employeeDTO -> employeeDTO.agreements().stream())
                .filter(employeeAgreementDTO -> employeeAgreementDTO.status().equals(AgreementStatus.ACTIVE))
                .flatMap(employeeAgreementDTO -> employeeAgreementDTO.clauses().stream())
                .anyMatch(employeeAgreementClauseDTO ->
                        employeeAgreementClauseDTO.clauseId().equals(1L) &&
                                !employeeAgreementClauseDTO.startDate().isAfter(transactionDate)
                                && (employeeAgreementClauseDTO.endDate() == null || employeeAgreementClauseDTO.endDate().isAfter(transactionDate))
                );
    }

    @Override
    public FiscalValueType getFiscalValueType() {
        return FiscalValueType.TAX_DEDUCTION;
    }

    @Override
    public String getFiscalValueSubtype() {
        return fiscalValueSubtype;
    }
}
