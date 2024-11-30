package org.gminds.accounting_service.model.enums;

public enum LedgerAccountPlan {
    EXPENSES("EXPENSES"), REVENUE("REVENUE");

    private final String name;

    LedgerAccountPlan(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
