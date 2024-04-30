package com.srs.management;

import com.srs.Dto.GEnrollmentDTO;
import com.srs.outputs.DbmsOutput;
import com.srs.utility.MenuStrings;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class EnrollmentManagement {

    private Connection conn;
    private Scanner scanner;


    public EnrollmentManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu()  {
        try {
            while (true) {
                System.out.print(MenuStrings.ENROLLMENT_MENU);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewEnrollments();
                        break;
                    case 2:
                        addEnrollmentCli();
                        break;
                    case 3:
                        deleteEnrollmentCli();
                        break;
                    case 4:
                        return;
                    case 5:
                        conn.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteEnrollmentCli() throws SQLException {
            System.out.println("Enter student B#:");
            String sId = scanner.nextLine();
            System.out.println("Enter class ID:");
            String classId = scanner.nextLine();
            deleteEnrollment(sId, classId);
    }

    private void addEnrollmentCli() throws SQLException {
            System.out.println("Enter student B#:");
            String sId = scanner.nextLine();
            System.out.println("Enter class ID:");
            String classId = scanner.nextLine();
            addEnrollment(sId, classId);
    }

    public List<GEnrollmentDTO> viewEnrollments(){
        String VIEW_ENROLLMENTS_PROCEDURE = "{ call student_management_package.show_g_enrollments }";

        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute(VIEW_ENROLLMENTS_PROCEDURE);

            stmt.close();
            List<String> result  = dbmsOutput.show();
            dbmsOutput.close();
            return GEnrollmentDTO.mapFromSQL(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String addEnrollment(String sId, String classId) throws SQLException {
        String ADD_ENROLLMENT_QUERY = "{call student_management_package.enroll_student_into_class(?, ?)}";
        CallableStatement stmt = conn.prepareCall(ADD_ENROLLMENT_QUERY);
        DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        stmt.setString(1, sId);
        stmt.setString(2, classId);
        stmt.execute();
        stmt.close();
        List<String> result = dbmsOutput.show();
        dbmsOutput.close();
        return result.get(0);

    }

    public String deleteEnrollment(String sId, String classId) throws SQLException {
        String DELETE_ENROLLMENT_QUERY = "{call student_management_package.drop_student_from_class(?, ?)}";
        CallableStatement stmt = conn.prepareCall(DELETE_ENROLLMENT_QUERY);
        DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        stmt.setString(1, sId);
        stmt.setString(2, classId);
        stmt.execute();
        stmt.close();
        List<String> result = dbmsOutput.show();
        dbmsOutput.close();
        return result.get(0);
    }
}
