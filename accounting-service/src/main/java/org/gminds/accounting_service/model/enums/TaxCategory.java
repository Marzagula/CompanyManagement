package org.gminds.accounting_service.model.enums;

public enum TaxCategory {
    ZUS("ZUS"), VAT("VAT"), PIT("PIT"), HEALTH("HEALTH");

    private final String name;

    TaxCategory(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
