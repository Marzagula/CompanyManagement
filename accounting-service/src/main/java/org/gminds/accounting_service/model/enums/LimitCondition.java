package org.gminds.accounting_service.model.enums;

public enum LimitCondition {
    LESS("LESS"), LESS_EQUAL("LESS_EQUAL"), EQUAL("EQUAL"), MORE_EQUAL("MORE_EQUAL"), MORE("MORE"),;

    private final String name;

    LimitCondition(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
