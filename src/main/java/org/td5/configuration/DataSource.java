package org.td5.configuration;

import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@AllArgsConstructor
public class DataSource {
    private final String JDBC_URL;
    private final String USERNAME;
    private final String PASSWORD;

    public Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}