package org.srs.management;

import org.srs.config.DatabaseConnection;
import org.srs.outputs.DbmsOutput;

import java.sql.*;

public class LogsManagement {
    public void displayLogs() {
        try {
            Connection conn = DatabaseConnection.getConnectionInstance();
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_logs}");

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayPrerequisites() {
        try {
            Connection conn = DatabaseConnection.getConnectionInstance();
            CallableStatement stmt = conn.prepareCall("{ call student_management_package.show_prerequisites(?, ?) }");

            // Set the parameters for the stored procedure
            stmt.setString(1, "CS"); // Replace "dept_code_value" with the actual department code
            stmt.setInt(2, 999); // Replace 123 with the actual course number

            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);

            // Execute the stored procedure
            stmt.execute();

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
