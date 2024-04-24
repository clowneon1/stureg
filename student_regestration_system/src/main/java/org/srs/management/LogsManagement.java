package org.srs.management;

import org.srs.config.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleTypes;


public class LogsManagement {
    public void displayLogs() {
        try {
            Connection conn = DatabaseConnection.getConnectionInstance();
            CallableStatement cstmt = conn.prepareCall("{call show_logs(?)}");

            // Register the OUT parameter to capture the cursor
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);

            // Execute the stored procedure
            cstmt.execute();

            // Retrieve the result set from the cursor
            ResultSet rs = (ResultSet) cstmt.getObject(1);

            // Process the result set and display logs
            System.out.println("Logs:");
            while (rs.next()) {
                int logNumber = rs.getInt("log#");
                String userName = rs.getString("user_name");
                String opTime = rs.getString("op_time");
                String tableName = rs.getString("table_name");
                String operation = rs.getString("operation");
                String tupleKeyValue = rs.getString("tuple_keyvalue");

                System.out.println("Log#: " + logNumber + ", User Name: " + userName + ", Operation Time: " + opTime
                        + ", Table Name: " + tableName + ", Operation: " + operation + ", Tuple Key Value: "
                        + tupleKeyValue);
            }

            // Close resources
            rs.close();
            cstmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
