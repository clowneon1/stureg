package com.srs.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Bean
    public Connection getConnection() {
        try {
            return DatabaseConnection.getConnectionInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection();
    }
}