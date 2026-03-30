package org.td5.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = System.getenv("JDBC_URL");
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new IllegalStateException("JDBC_URL is not set");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("USERNAME is not set");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("PASSWORD is not set");
        }

        return new DataSource(jdbcUrl, username, password);
    }
}