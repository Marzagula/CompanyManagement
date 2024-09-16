package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Transaction;

public interface TaxCalculator {
    Double calculateTax(Transaction transaction);
}
