package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, Long> {
    @Query("SELECT la FROM LedgerAccount la JOIN FETCH la.transactions WHERE la.id = :id")
    LedgerAccount findByIdWithTransactions(Long id);

    @Query("SELECT la FROM LedgerAccount la JOIN FETCH la.transactions WHERE la.accountName = :accountName")
    LedgerAccount findByAccountNameWithTransactions(String accountName);
}
