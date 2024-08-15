package com.gminds.employee_service.model.enums;

public enum EmplAgreementType {
    B2B("B2B"),
    EMPLOYMENT("FULL_TIME");

    private final String name;

    private EmplAgreementType(String s) {
        name = s;
    }
    public String toString() {
        return this.name;
    }
}
