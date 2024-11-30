package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.TransactionItem;

import java.math.BigDecimal;

public interface TaxCalculator<T extends TransactionItem> {
    BigDecimal calculateTax(T transaction);
}
