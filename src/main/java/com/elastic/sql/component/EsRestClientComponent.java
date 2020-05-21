package com.elastic.sql.component;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EsRestClientComponent
{
    private static final Logger logger = LoggerFactory.getLogger(EsRestClientComponent.class);

    private RestClient esRestClient;

    @Bean
    public RestClient getEsRestClient()
    {
        logger.info("returning es client");
        return esRestClient;
    }

    @PostConstruct
    private void postConstruct()
    {
        logger.info("initialising es client");
        esRestClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http"))
                .build();
    }

    @PreDestroy
    public void preDestroy()
    {
        try
        {
            logger.info("closing es client");
            esRestClient.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
