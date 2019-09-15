package com.tallink.clientmatching.service;

public enum ClientSearchSql {
    BASE_REQUEST("classpath:sql/client-matching-base-request.sql"),
    EMAIL("classpath:sql/client-matching-email.sql"),
    EMAILS("classpath:sql/client-matching-emails.sql"),
    PHONE("classpath:sql/client-matching-phone.sql"),
    PHONES("classpath:sql/client-matching-phones.sql"),
    CLUBONE("classpath:sql/client-matching-clubone.sql"),
    CLUBONE_AND_NAME("classpath:sql/client-matching-clubone-and-name.sql"),
    FULLNAME("classpath:sql/client-matching-fullname.sql");

    private final String sqlFile;

    ClientSearchSql(String sqlFile) {
        this.sqlFile = sqlFile;
    }

    public String getSqlFile() {
        return this.sqlFile;
    }
}
