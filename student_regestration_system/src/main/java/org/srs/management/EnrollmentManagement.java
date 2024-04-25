package org.srs.management;

import org.srs.outputs.DbmsOutput;
import org.srs.utility.MenuStrings;

import java.sql.*;
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
                        viewEnrollments(conn);
                        break;
                    case 2:
                        addEnrollment(conn, scanner);
                        break;
                    case 3:
                        deleteEnrollment(conn, scanner);
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

    public void viewEnrollments(Connection conn){
        String VIEW_ENROLLMENTS_PROCEDURE = "{ call student_management_package.show_g_enrollments }";


        try {
            Statement stmt = conn.createStatement();
            org.srs.outputs.DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute(VIEW_ENROLLMENTS_PROCEDURE);

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEnrollment(Connection conn, Scanner scanner) throws SQLException {
        String ADD_ENROLLMENT_QUERY = "{call student_management_package.enroll_student_into_class(?, ?)}";
        CallableStatement stmt = conn.prepareCall(ADD_ENROLLMENT_QUERY);
        DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        System.out.println("Enter student B#:");
        String bNumber = scanner.nextLine();
        System.out.println("Enter class ID:");
        String classId = scanner.nextLine();

        stmt.setString(1, bNumber);
        stmt.setString(2, classId);
        stmt.execute();
        stmt.close();
        dbmsOutput.show();
        dbmsOutput.close();
    }

    public void deleteEnrollment(Connection conn, Scanner scanner) throws SQLException {
        String DELETE_ENROLLMENT_QUERY = "{call student_management_package.drop_student_from_class(?, ?)}";
        CallableStatement stmt = conn.prepareCall(DELETE_ENROLLMENT_QUERY);
        DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);

        System.out.println("Enter student B#:");
        String bNumber = scanner.nextLine();
        System.out.println("Enter class ID:");
        String classId = scanner.nextLine();

        stmt.setString(1, bNumber);
        stmt.setString(2, classId);
        stmt.execute();
        stmt.close();
        dbmsOutput.show();
        dbmsOutput.close();

    }
}
