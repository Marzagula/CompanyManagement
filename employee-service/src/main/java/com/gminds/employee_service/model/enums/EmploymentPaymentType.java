package com.gminds.employee_service.model.enums;

public enum EmploymentPaymentType {
    PER_HOUR("PER_HOUR"), PER_DAY("PER_DAY"), PER_MONTH("PER_MONTH");

    private final String name;

    EmploymentPaymentType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
