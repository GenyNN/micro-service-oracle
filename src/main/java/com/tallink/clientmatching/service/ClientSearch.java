package com.tallink.clientmatching.service;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

public class ClientSearch {

    private final NamedParameterJdbcTemplate database;

    private final String queryText;

    private final String name;

    private final MapSqlParameterSource params;

    public ClientSearch(NamedParameterJdbcTemplate database, String name, String queryText, MapSqlParameterSource params) {
        this.database = database;
        this.queryText = queryText;
        this.params = params;
        this.name = name;
    }

    public String getQueryName() {
        return this.name;
    }

    public List<Map<String, Object>> run() {
        return database.queryForList(queryText, params);
    }

}
