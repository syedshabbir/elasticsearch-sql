package com.elastic.sql.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig
{
    @Bean
    public DataSource getDataSource()
    {
        final EsDataSource dataSource = new EsDataSource();
        final String address = "jdbc:es://http://localhost:9200";
        dataSource.setUrl(address);
        final Properties connectionProperties = new Properties();
        dataSource.setProperties(connectionProperties);
        return dataSource;
    }
}
