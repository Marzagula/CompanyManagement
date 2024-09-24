package org.gminds.accounting_service.service.taxes.builder;

import org.gminds.accounting_service.model.TaxTransaction;
import org.gminds.accounting_service.model.enums.TaxCategory;

import java.time.LocalDate;

public class TaxTransactionBuilder {
    private String description;
    private String counterparty;
    private Double amount;
    private TaxCategory taxCategory;
    private Double taxBase;
    private Long employeeId;
    private LocalDate transactionDate;

    public TaxTransactionBuilder setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public TaxTransactionBuilder setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
        return this;
    }

    public TaxTransactionBuilder setTaxBase(Double taxBase) {
        this.taxBase = taxBase;
        return this;
    }

    public TaxTransactionBuilder setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public TaxTransactionBuilder setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public TaxTransactionBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaxTransactionBuilder setCounterparty(String counterparty) {
        this.counterparty = counterparty;
        return this;
    }

    public TaxTransaction getTaxTransaction() {
        return new TaxTransaction(taxCategory, taxBase, employeeId, description, counterparty, amount, transactionDate);
    }
}
