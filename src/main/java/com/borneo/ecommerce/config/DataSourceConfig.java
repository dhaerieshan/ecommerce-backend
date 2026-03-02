package com.borneo.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
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
        String jdbcUrl = normalizePostgresUrl(datasourceUrl);
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }

    private String normalizePostgresUrl(String url) {
        // Step 1: Convert scheme to jdbc:postgresql://
        String normalized = url;
        if (url.startsWith("postgresql://")) {
            normalized = url.replace("postgresql://", "jdbc:postgresql://");
        } else if (url.startsWith("postgres://")) {
            normalized = url.replace("postgres://", "jdbc:postgresql://");
        }

        // Step 2: Strip embedded credentials (user:pass@host → host)
        String prefix = "jdbc:postgresql://";
        String rest = normalized.substring(prefix.length());
        if (rest.contains("@")) {
            rest = rest.substring(rest.indexOf("@") + 1);
        }
        normalized = prefix + rest;

        // Step 3: Ensure port 5432 is present
        rest = normalized.substring(prefix.length());
        int slashIndex = rest.indexOf("/");
        if (slashIndex != -1) {
            String hostPart = rest.substring(0, slashIndex);
            String pathPart = rest.substring(slashIndex);
            if (!hostPart.contains(":")) {
                hostPart = hostPart + ":5432";
            }
            normalized = prefix + hostPart + pathPart;
        }

        return normalized;
    }
}