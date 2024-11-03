package org.gminds.accounting_service.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_item_type", discriminatorType = DiscriminatorType.STRING)
public abstract class TransactionItem extends Auditable<String> {
    protected LocalDate transactionDate;
    protected String description;
    protected String counterparty;
    protected Double amount;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
