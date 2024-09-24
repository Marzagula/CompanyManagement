package org.gminds.accounting_service.service.taxes;

import org.gminds.accounting_service.model.Transaction;
import org.gminds.accounting_service.model.enums.FiscalValueType;

import java.math.BigDecimal;

public interface TaxDeduction<T extends Transaction> {
    BigDecimal calculateDeduction(T transaction);

    FiscalValueType getFiscalValueType();

    String getFiscalValueSubtype();
}
