package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.LedgerAccount;
import org.gminds.accounting_service.model.Salary;
import org.gminds.accounting_service.model.TaxTransaction;
import org.gminds.accounting_service.model.enums.FiscalValueType;
import org.gminds.accounting_service.model.enums.TaxCategory;
import org.gminds.accounting_service.repository.FiscalValuesRepository;
import org.gminds.accounting_service.repository.LedgerAccountRepository;
import org.gminds.accounting_service.service.CachedEmployeeService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeCostDeduction implements TaxDeduction<Salary> {

    private final FiscalValuesRepository fiscalValuesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final ZUSCalculator zusCalculator;
    private final CachedEmployeeService cachedEmployeeService;

    public EmployeeCostDeduction(FiscalValuesRepository fiscalValuesRepository,
                                 LedgerAccountRepository ledgerAccountRepository,
                                 ZUSCalculator zusCalculator,
                                 CachedEmployeeService cachedEmployeeService) {
        this.fiscalValuesRepository = fiscalValuesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.zusCalculator = zusCalculator;
        this.cachedEmployeeService = cachedEmployeeService;
    }

    @Override
    public BigDecimal calculateDeduction(Salary transaction) {
        List<FiscalValueType> fiscalValueTypes = List.of(FiscalValueType.TAX_DEDUCTION, FiscalValueType.TAX_BRACKET);
        Map<String, BigDecimal> fiscalValues = new HashMap<>();

        fiscalValuesRepository.findByFiscalYear(2024)
                .stream()
                .filter(fiscalValue -> fiscalValueTypes.contains(fiscalValue.getFiscalValueType()))
                .forEach(fiscalValue -> {
                    String key = fiscalValue.getFiscalValueSubtype();
                    BigDecimal value = BigDecimal.valueOf(fiscalValue.getLimitValue());

                    int suffix = 1;
                    while (fiscalValues.containsKey(key)) {
                        key = fiscalValue.getFiscalValueSubtype() + "_" + (++suffix);
                    }
                    fiscalValues.put(key, value);
                });

        LedgerAccount ledgerAccount = ledgerAccountRepository.findByAccountNameWithTransactions("UOP");
        BigDecimal incomeBaseSummary = ledgerAccount.getTransactions()
                .stream()
                .filter(trans -> trans instanceof Salary && ((Salary) trans).getEmployeeId().equals(transaction.getEmployeeId()))
                .map(trans -> (Salary) trans)
                .map(trans -> BigDecimal.valueOf(trans.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)/*.add(BigDecimal.valueOf(transaction.getAmount()))*/;
        BigDecimal zusTaxSummary = ledgerAccount.getTransactions()
                .stream()
                .filter(trans -> trans instanceof TaxTransaction && ((TaxTransaction) trans).getEmployeeId().equals(transaction.getEmployeeId()))
                .map(trans -> (TaxTransaction) trans)
                .filter(taxTransaction -> taxTransaction.getTaxCategory().equals(TaxCategory.ZUS))
                .map(trans -> getEmployeeZusPart(trans))
                .reduce(BigDecimal.ZERO, BigDecimal::add)/*.add((BigDecimal.valueOf(transaction.getAmount())).multiply(zusCalculator.getEmployeeZusPart()))*/;

        BigDecimal taxPercentage = fiscalValues.get("first_bracket");
        if (incomeBaseSummary.subtract(zusTaxSummary).compareTo(fiscalValues.get("first_bracket")) > 0) {
            taxPercentage = fiscalValues.get("second_bracket");
        }

        BigDecimal employeeCost = fiscalValues.get("employee_costs");
        if (checkIfOutsideClauseInForce(transaction))
            employeeCost = fiscalValues.get("employee_costs_2");

        return taxPercentage.multiply(employeeCost);
    }

    @Override
    public FiscalValueType getFiscalValueType() {
        return FiscalValueType.TAX_DEDUCTION;
    }

    @Override
    public String getFiscalValueSubtype() {
        return "employee_costs";
    }

    private BigDecimal getEmployeeZusPart(TaxTransaction taxTransaction) {
        return BigDecimal.valueOf(taxTransaction.getTaxBase()).multiply(zusCalculator.getEmployeeZusPart());
    }

    private boolean checkIfOutsideClauseInForce(Salary transaction) {
        return cachedEmployeeService.getCachedEmployees()
                .stream()
                .filter(employeeDTO -> employeeDTO.id().equals(transaction.getEmployeeId()))
                .flatMap(employeeDTO -> employeeDTO.agreements().stream())
                .flatMap(employeeAgreementDTO -> employeeAgreementDTO.clauses().stream())
                .anyMatch(employeeAgreementClauseDTO ->
                        employeeAgreementClauseDTO.clauseId().equals(1L) &&
                                !employeeAgreementClauseDTO.startDate().isAfter(transaction.getTransactionDate())
                                && (employeeAgreementClauseDTO.endDate() == null || employeeAgreementClauseDTO.endDate().isAfter(transaction.getTransactionDate()))
                );

    }
}
