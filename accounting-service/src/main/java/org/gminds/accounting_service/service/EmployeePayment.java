package org.gminds.accounting_service.service;

import org.gminds.accounting_service.model.Transaction;

public class EmployeePayment implements Payment {
    /*rozlozenie placy na zus, podatek dochodowy, vat i pozostala czesc(netto)
     * stworzenie osobnych transakcji dla kazdej z tych czesci,
     * kazda powinna byc obsluzona na osobnym ledger account
     * transakcje do zus i US powinny byc poprawnie obsluzone
     * */
    @Override
    public void makePayment(Transaction transaction) {
        int fiscalYear = transaction.getTransactionDate().getYear();

    }
}
