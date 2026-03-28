package org.td5.configuration;

import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Configuration
public class DataSource {
    private final String JDBC_URL;
    private final String USERNAME;
    private final String PASSWORD;

    public DataSource(){
        this.JDBC_URL = System.getenv("JDBC_URL");
        this.USERNAME = System.getenv("USERNAME");
        this.PASSWORD = System.getenv("PASSWORD");

        if (JDBC_URL == null || JDBC_URL.isBlank()) {
            throw new IllegalStateException("JDBC_URL is not set");
        }
        if (USERNAME == null || USERNAME.isBlank()) {
            throw new IllegalStateException("USERNAME is not set");
        }
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new IllegalStateException("PASSWORD is not set");
        }
    }

    public Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}