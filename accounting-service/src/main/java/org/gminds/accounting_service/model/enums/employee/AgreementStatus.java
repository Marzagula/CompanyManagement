package org.gminds.accounting_service.model.enums.employee;

public enum AgreementStatus {
    ACTIVE("ACTIVE"),
    FINISHED("FINISHED"),
    FUTURE("FUTURE");

    private final String name;

    AgreementStatus(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
