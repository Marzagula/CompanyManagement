package org.gminds.accounting_service.model.enums;

public enum TaxType {
    ZUS("ZUS"),VAT("VAT"),PIT("PIT"),HEALTH("HEALTH");

    private final String name;
    TaxType(String s) {
        name = s;
    }
    public String toString() {
        return this.name;
    }
}
