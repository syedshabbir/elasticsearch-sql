package com.elastic.sql.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class EsSearch
{
    private static final Logger logger = LoggerFactory.getLogger(EsSearch.class);

    private final WebClient webClient;

    @Autowired private RestClient esRestClient;

    private static final String QUERY = "{"
            + "\"query\": \"select id, name, SUBSTRING(text, 1,100) as text, score() as score from topic where match('name^2,text', ?) ORDER BY SCORE() DESC\","
            + "\"fetch_size\": 5,"
            + "\"params\": [\"ebola\"]"
            + "}";



    public EsSearch(WebClient.Builder webClientBuilder, RestClient esRestClient)
    {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9200/")
                .filter(logRequest())
                .filter(logResponse())
                .build();
        this.esRestClient = esRestClient;
    }

    public JsonNode searchEsRestClient() throws IOException
    {
        Request request = new Request(
                "GET",
                "/_sql");
        request.addParameter("format", "json");
        request.setJsonEntity(QUERY);
        Response response = esRestClient.performRequest(request);
        String jsonString = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }

    public JsonNode searchEsRestClient(String query) throws IOException
    {

        final String QUERY = "{"
                + "\"query\": \"select id, name, SUBSTRING(text, 1,100) as text, score() as score from topic where match('name^2,text', ?) ORDER BY SCORE() DESC\","
                + "\"fetch_size\": 5,"
                + "\"params\": [\""
                + query
                + "\"]}";
        Request request = new Request(
                "GET",
                "/_sql");
        request.addParameter("format", "json");
        request.setJsonEntity(QUERY);
        Response response = esRestClient.performRequest(request);
        String jsonString = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }

    public Mono<JsonNode> json()
    {
        final Mono<JsonNode> resp = this.webClient
                .post()
                .uri("_sql?format=json")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(QUERY)
                .exchange()
                .flatMap(response -> response.bodyToMono(JsonNode.class));

        return resp;
    }

    public JsonNode json(String query)
    {

        final String QUERY = "{"
                + "\"query\": \"select id, name, SUBSTRING(text, 1,100) as text, score() as score from topic where match('name^2,text', ?) ORDER BY SCORE() DESC\","
                + "\"fetch_size\": 5,"
                + "\"params\": [\""
                + query
                + "\"]}";

        final JsonNode resp = this.webClient
                .post()
                .uri("_sql?format=json")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(QUERY))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                {
                    logger.error(response.statusCode().toString());
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, response ->
                {
                    logger.error(response.statusCode().toString());
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToMono(JsonNode.class)
                .block();

        return resp;
    }

    private ExchangeFilterFunction logResponse()
    {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse ->
        {
            logger.info("Response: {}", clientResponse.statusCode());
            Mono<ResponseEntity<String>> s = clientResponse.toEntity(String.class);
            clientResponse.headers()
                    .asHttpHeaders()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    private ExchangeFilterFunction logRequest()
    {
        return (clientRequest, next) ->
        {
            logger.info("Request: {} {} {}", clientRequest.method(), clientRequest.url(), clientRequest.body());
            clientRequest.headers().forEach(this::logHeader);
            clientRequest.cookies().forEach(this::logHeader);
            return next.exchange(clientRequest);
        };
    }

    private void logHeader(String name, List<String> values)
    {
        values.forEach(value -> logger.info("{}={}", name, value));
    }
}
