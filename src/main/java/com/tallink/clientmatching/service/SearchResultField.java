package com.tallink.clientmatching.service;

import java.util.Arrays;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public enum SearchResultField {
    FULL_NAME, EMAIL, PHONE_NUMBER, CLUB_ACCOUNT, CLIENT_ID, CLUB_ACCOUNT_TYPE, ADDRESS, ZIP_CITY, BIRTHDAY, FIRST_NAME, LAST_NAME;

    static Collection<String> allSearchResultFieldNames() {
        return Arrays.asList(SearchResultField.values())
                .stream()
                .map(SearchResultField::name)
                .collect(toList());
    }
}
