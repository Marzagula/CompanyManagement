package org.gminds.accounting_service.model.enums;

public enum InvoiceType {
    SALES("SALES"),PURCHASE("PURCHASE");

    private final String name;

    InvoiceType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
