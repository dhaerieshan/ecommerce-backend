package com.borneo.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/ecommerce}")
    private String datasourceUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:postgres}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:postgres}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        String jdbcUrl = datasourceUrl;

        if (datasourceUrl.startsWith("postgresql://")) {
            jdbcUrl = datasourceUrl.replace("postgresql://", "jdbc:postgresql://");
        } else if (datasourceUrl.startsWith("postgres://")) {
            jdbcUrl = datasourceUrl.replace("postgres://", "jdbc:postgresql://");
        }

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}