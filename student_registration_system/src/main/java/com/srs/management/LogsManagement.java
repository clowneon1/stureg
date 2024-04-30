package com.srs.management;


import com.srs.Dto.LogDTO;
import com.srs.outputs.DbmsOutput;

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

    public List<LogDTO> displayLogs() {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_logs}");

            stmt.close();
            List result = dbmsOutput.show();
            dbmsOutput.close();
            return LogDTO.mapFromSQL(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
