package org.gminds.accounting_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Transaction extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    LocalDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name = "ledger_account_id")
    private LedgerAccount ledgerAccount;
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    List<TransactionItem> transactionItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LedgerAccount getLedgerAccount() {
        return ledgerAccount;
    }

    public void setLedgerAccount(LedgerAccount ledgerAccount) {
        this.ledgerAccount = ledgerAccount;
    }
}
