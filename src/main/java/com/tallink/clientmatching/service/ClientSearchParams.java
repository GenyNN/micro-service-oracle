package com.tallink.clientmatching.service;

public enum ClientSearchParams {
    EMAIL("email"), CLUB_ONE("club_one"), PHONE_NUMBER("phone_number"), COUNTRY_CODE("country_code"), FULL_NAME("full_name"),
    FIRST_NAME("first_name"), LAST_NAME("last_name"), EMAILS("emails"), PHONES("phones");

    private final String paramValue;

    ClientSearchParams(String value) {
        this.paramValue = value;
    }

    public String  getParamValue() {
        return this.paramValue;
    }
}
