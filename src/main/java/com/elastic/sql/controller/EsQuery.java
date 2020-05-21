package com.elastic.sql.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elastic.sql.model.JdbcData;
import com.elastic.sql.repo.EsRepo;
import com.elastic.sql.service.EsSearch;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@RestController
public class EsQuery
{

    @Autowired
    private EsRepo esRepo;

    @Autowired
    private EsSearch esSearch;

    @RequestMapping("/jdbc-sql")
    public List<JdbcData> jdbcSql(@RequestParam String text) throws SQLException
    {
        return esRepo.getEntries(text);
    }

    @RequestMapping("/dsl-sql-json-web-client")
    public Mono<JsonNode> esSql() throws SQLException
    {
        return esSearch.json();
    }

    @RequestMapping("/dsl-sql-json-es-client")
    public JsonNode esClientSql() throws IOException
    {
        return esSearch.searchEsRestClient();
    }

    @RequestMapping("/dsl-sql-json-param-web-client")
    public JsonNode esSqlJson(@RequestParam String text) throws SQLException
    {
        return esSearch.json(text);
    }

    @RequestMapping("/dsl-sql-json-param-es-client")
    public JsonNode esClientSqlJson(@RequestParam String text) throws IOException
    {
        return esSearch.searchEsRestClient(text);
    }

}
