package org.srs.management;

import oracle.jdbc.util.ThreadSafeCache;
import org.srs.config.DatabaseConnection;
import org.srs.outputs.DbmsOutput;

import java.sql.*;
import java.util.Scanner;

public class LogsManagement {
    private Connection conn;
    private Scanner scanner;

    public LogsManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayLogs() {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_logs}");

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
