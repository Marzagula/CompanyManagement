package org.gminds.accounting_service.repository;

import org.gminds.accounting_service.model.Invoice;
import org.gminds.accounting_service.model.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByInvoiceType(InvoiceType invoiceType);
}
