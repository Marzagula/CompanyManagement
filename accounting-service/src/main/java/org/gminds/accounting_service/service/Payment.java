package org.gminds.accounting_service.service;

import org.gminds.accounting_service.model.Transaction;

public interface Payment {
    void makePayment(Transaction transaction);
}
