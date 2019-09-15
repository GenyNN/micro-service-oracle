package com.tallink.clientmatching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tallink.clientmatching.service.ClientSearchSql.*;
import static java.sql.Types.VARCHAR;

@Component
public class ClientSearchFactory {

    private final NamedParameterJdbcTemplate database;

    private final ResourceLoader resourceLoader;

    private final String baseRequestText;

    private static final String SUBQUERY_VARIABLE = "$(specific.clause)";

    @Autowired
    public ClientSearchFactory(NamedParameterJdbcTemplate database, ResourceLoader resourceLoader) {

        this.database = database;
        this.resourceLoader = resourceLoader;
        this.baseRequestText = getQueryResourceText(BASE_REQUEST);
    }

    public ClientSearch searchByClubOne(String clubOne, String firstName, String lastName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ClientSearchParams.CLUB_ONE.getParamValue(), clubOne);
        params.put(ClientSearchParams.FIRST_NAME.getParamValue(), firstName);
        params.put(ClientSearchParams.LAST_NAME.getParamValue(), lastName);
        return new ClientSearch(database, CLUBONE_AND_NAME.name(), getQuery(CLUBONE_AND_NAME),
                    varcharParams(params));
    }

    public ClientSearch searchByPhone(String phoneNumber, Optional<String> countryCode) {
        Map<String, Object> params = new HashMap<>();
        params.put(ClientSearchParams.PHONE_NUMBER.getParamValue(), phoneNumber);
        params.put(ClientSearchParams.COUNTRY_CODE.getParamValue(), null);
        countryCode.ifPresent(code -> params.put(ClientSearchParams.COUNTRY_CODE.getParamValue(), code));
        return new ClientSearch(database, PHONE.name(), getQuery(PHONE), varcharParams(params));
    }

    public ClientSearch searchByEmail(String email) {
        return new ClientSearch(database, EMAIL.name(), getQuery(EMAIL),
                varcharParam(ClientSearchParams.EMAIL.getParamValue(), email));
    }

    public ClientSearch searchByFullName(String fullName) {
        return new ClientSearch(database, FULLNAME.name(), getQuery(FULLNAME),
                varcharParam(ClientSearchParams.FULL_NAME.getParamValue(), fullName));
    }

    public ClientSearch searchByEmails(Collection<String> emails, String firstName, String lastName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ClientSearchParams.EMAILS.getParamValue(), getEmailsUppercase(emails));
        params.put(ClientSearchParams.FIRST_NAME.getParamValue(), firstName);
        params.put(ClientSearchParams.LAST_NAME.getParamValue(), lastName);

        return new ClientSearch(database, EMAILS.name(), getQuery(EMAILS),
                namedParams(params));
    }

    public Collection<String> getEmailsUppercase(Collection<String> emails) {
        return emails.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    public ClientSearch searchByPhones(Collection<String> phones, String firstName, String lastName) {
        Map<String, Object> params = new HashMap<>();
        params.put(ClientSearchParams.PHONES.getParamValue(), phones);
        params.put(ClientSearchParams.FIRST_NAME.getParamValue(), firstName);
        params.put(ClientSearchParams.LAST_NAME.getParamValue(), lastName);

        return new ClientSearch(database, PHONES.name(), getQuery(PHONES),
                namedParams(params));
    }

    private static MapSqlParameterSource namedParam(String name, Object value) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return parameters.addValue(name, value);
    }

    private static MapSqlParameterSource varcharParam(String name, String value) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return parameters.addValue(name, value, VARCHAR);
    }

    private static MapSqlParameterSource namedParams(Map<String, Object> params) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        for (Map.Entry<String, Object> entry : params.entrySet())
            parameters.addValue(entry.getKey(), entry.getValue());
        return parameters;
    }

    private static MapSqlParameterSource varcharParams(Map<String, Object> params) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        for (Map.Entry<String, Object> entry : params.entrySet())
            parameters.addValue(entry.getKey(), entry.getValue(), VARCHAR);
        return parameters;
    }

    private String getQuery(ClientSearchSql specificRequest) {
        String subqueryText = getQueryResourceText(specificRequest);
        return baseRequestText.replace(SUBQUERY_VARIABLE, subqueryText);
    }

    private String getQueryResourceText(ClientSearchSql specificRequestResource) {
        try {
            File resourceFile = resourceLoader.getResource(specificRequestResource.getSqlFile()).getFile();
            return String.join(" ", Files.readAllLines(resourceFile.toPath()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
