package org.gminds.accounting_service.model;

import jakarta.persistence.*;
import org.gminds.accounting_service.model.enums.InvoiceType;

@Entity
@DiscriminatorValue("INVOICE")
public class Invoice extends TransactionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private Double vatTax;
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getVatTax() {
        return vatTax;
    }

    public void setVatTax(Double vatTax) {
        this.vatTax = vatTax;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }
}
