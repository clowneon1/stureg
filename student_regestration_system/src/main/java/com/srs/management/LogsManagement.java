package com.srs.management;


import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;

//@Service
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
