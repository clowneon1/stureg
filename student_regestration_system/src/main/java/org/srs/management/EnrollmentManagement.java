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
        String ADD_ENROLLMENT_QUERY = "INSERT INTO g_enrollments (g_B#, classid, score) VALUES (?, ?, ?)";

        System.out.println("Enter student B#:");
        String bNumber = scanner.nextLine();
        System.out.println("Enter class ID:");
        String classId = scanner.nextLine();
        System.out.println("Enter score:");
        double score = scanner.nextDouble();

        try (PreparedStatement ps = conn.prepareStatement(ADD_ENROLLMENT_QUERY)) {
            ps.setString(1, bNumber);
            ps.setString(2, classId);
            ps.setDouble(3, score);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Enrollment added successfully.");
            } else {
                System.out.println("Failed to add enrollment.");
            }
        }
    }

    public void deleteEnrollment(Connection conn, Scanner scanner) throws SQLException {
        String DELETE_ENROLLMENT_QUERY = "DELETE FROM g_enrollments WHERE g_B# = ? AND classid = ?";

        System.out.println("Enter student B#:");
        String bNumber = scanner.nextLine();
        System.out.println("Enter class ID:");
        String classId = scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(DELETE_ENROLLMENT_QUERY)) {
            ps.setString(1, bNumber);
            ps.setString(2, classId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Enrollment deleted successfully.");
            } else {
                System.out.println("Failed to delete enrollment. Enrollment not found.");
            }
        }
    }
}
