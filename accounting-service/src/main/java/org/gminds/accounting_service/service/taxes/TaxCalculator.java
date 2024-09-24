package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Transaction;

import java.math.BigDecimal;

public interface TaxCalculator<T extends Transaction> {
    BigDecimal calculateTax(T transaction);
}
