package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Invoice, Long> {
}
