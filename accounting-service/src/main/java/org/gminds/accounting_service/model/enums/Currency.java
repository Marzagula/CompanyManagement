package org.gminds.accounting_service.model.enums;

public enum Currency {
    PLN("PLN"),USD("USD"),EUR("EUR");

    private final String name;

    Currency(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
