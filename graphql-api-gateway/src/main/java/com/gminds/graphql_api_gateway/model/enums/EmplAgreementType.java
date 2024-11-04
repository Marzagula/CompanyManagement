package com.gminds.graphql_api_gateway.model.enums;

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
