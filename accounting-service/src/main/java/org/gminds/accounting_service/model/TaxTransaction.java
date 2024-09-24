package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.TaxCategory;

import java.time.LocalDate;

@Entity
public class TaxTransaction extends Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TaxCategory taxCategory;
    private Double taxBase;
    private Long employeeId;

    public TaxTransaction() {
    }

    ;

    public TaxTransaction(TaxCategory taxCategory,
                          Double taxBase,
                          Long employeeId,
                          String description,
                          String counterparty,
                          Double amount,
                          LocalDate transactionDate) {
        this.taxCategory = taxCategory;
        this.employeeId = employeeId;
        this.description = description;
        this.taxBase = taxBase;
        this.amount = amount;
        this.counterparty = counterparty;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }

    public Double getTaxBase() {
        return taxBase;
    }

    public void setTaxBase(Double taxBase) {
        this.taxBase = taxBase;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
