package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.Currency;
import org.gminds.accounting_service.model.enums.LedgerAccountPlan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class LedgerAccount extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String accountNumber;
    String accountName;
    @Enumerated(EnumType.STRING)
    LedgerAccountPlan plan;
    @OneToMany(mappedBy = "ledgerAccount", cascade = CascadeType.ALL)
    List<Transaction> transactions;
    @Enumerated(EnumType.STRING)
    Currency currency;
    BigDecimal balance;
    boolean isActive = true;
    LocalDate startDate;
    LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LedgerAccountPlan getPlan() {
        return plan;
    }

    public void setPlan(LedgerAccountPlan plan) {
        this.plan = plan;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
