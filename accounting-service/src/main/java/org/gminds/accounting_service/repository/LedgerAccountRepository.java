package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount,Long> {
}
