package org.srs.management;

import org.srs.config.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class LogsManagement {
    public void displayLogs() {
        try {
            Connection conn = DatabaseConnection.getConnectionInstance();
            CallableStatement cstmt = conn.prepareCall("BEGIN student_management_package.show_logs; END;");

            // Execute the PL/SQL block
            cstmt.execute();

            // Retrieve and display the DBMS_OUTPUT
            String output = "";
            boolean isOutput = true;
            while (isOutput) {
                try {
                    output = cstmt.getString(1);
                    if (output != null) {
                        System.out.println(output);
                    } else {
                        isOutput = false;
                    }
                } catch (SQLException e) {
                    // No more output available
                    isOutput = false;
                }
            }

            // Close resources
            cstmt.close();
            conn.close();
            System.out.println("this ran");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
