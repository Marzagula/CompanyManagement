package org.gminds.accounting_service.model.enums.employee;

public enum EmplAgreementType {
    B2B("B2B"),
    EMPLOYMENT("EMPLOYMENT");

    private final String name;

    EmplAgreementType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
