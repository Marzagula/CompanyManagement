package com.gminds.employee_service.model.enums;

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
