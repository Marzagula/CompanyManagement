package org.gminds.accounting_service.service;

import org.gminds.accounting_service.model.TransactionItem;

public interface Payment {
    void makePayment(TransactionItem transactionItem);
}
