package org.srs.config;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection conn;
    private DatabaseConnection() {

    }
    public static synchronized Connection getConnectionInstance() throws SQLException {
        if (conn == null || conn.isClosed()) {
            initializeConnection();
        }
        return conn;
    }
    private static void initializeConnection() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@localhost:1521:FREE");
        conn = ds.getConnection("system", "root");
    }
}
