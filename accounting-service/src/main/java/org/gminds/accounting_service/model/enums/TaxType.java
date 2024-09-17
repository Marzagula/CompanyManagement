package org.gminds.accounting_service.model.enums;

public enum TaxType {
    TAX_BRACKET("TAX_BRACKET"), TAX_DEDUCTION("TAX_DEDUCTION"), COST("COST"), LIMIT("LIMIT");

    private final String name;

    TaxType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
