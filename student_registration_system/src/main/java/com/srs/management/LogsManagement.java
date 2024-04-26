package com.srs.management;


import com.srs.Dto.LogsDTO;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

//@Service
public class LogsManagement {
    private Connection conn;
    private Scanner scanner;

    public LogsManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public List<LogsDTO> displayLogs() {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_logs}");

            stmt.close();
            List result = dbmsOutput.show();
            dbmsOutput.close();
            return LogsDTO.mapFromSQL(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
