package com.srs.config;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection conn;
    DatabaseConnection() {

    }
    public static synchronized Connection getConnectionInstance() throws SQLException {
        if (conn == null || conn.isClosed()) {
            initializeConnection();
            System.out.println("Database Connected. --------------->" );
        }
        return conn;
    }
    private static void initializeConnection() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@yclown.ddns.net:1521:FREE");
        conn = ds.getConnection("system", "root");
    }
}
