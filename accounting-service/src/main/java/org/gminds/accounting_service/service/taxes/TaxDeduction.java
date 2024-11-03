package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.TransactionItem;
import org.gminds.accounting_service.model.enums.FiscalValueType;

import java.math.BigDecimal;

public interface TaxDeduction<T extends TransactionItem> {
    BigDecimal calculateDeduction(T transaction);

    FiscalValueType getFiscalValueType();

    String getFiscalValueSubtype();
}
