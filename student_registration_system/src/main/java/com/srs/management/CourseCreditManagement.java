package com.srs.management;
import com.srs.outputs.DbmsOutput;
import com.srs.utility.MenuStrings;

import java.sql.*;
import java.util.Scanner;

public class CourseCreditManagement {
    private Connection conn;
    private Scanner scanner;

    public CourseCreditManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu() {
        try {
            while (true) {
                System.out.print(MenuStrings.COURSE_CREDIT_MENU);

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addCourseCredit();
                        break;
                    case 2:
                        viewAllCourseCredits();
                        break;
                    case 3:
                        deleteCourseCredit();
                        break;
                    case 4:
                        System.out.println("Exiting Course Credit Management. Goodbye!");
                        return;
                    case 5:
                        conn.close();
                        System.out.println("Exiting Course Credit Management System. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewAllCourseCredits() {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_course_credit}");
            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCourseCredit() {
        try {
            System.out.print("Enter Course Number (between 100 and 799): ");
            int courseNum = scanner.nextInt();
            if (courseNum < 100 || courseNum > 799) {
                System.out.println("Course number must be between 100 and 799.");
                return;
            }

            System.out.print("Enter Credits (3 or 4): ");
            int credits = scanner.nextInt();
            if (credits != 3 && credits != 4) {
                System.out.println("Credits must be either 3 or 4.");
                return;
            }

            if ((courseNum < 500 && credits != 4) || (courseNum >= 500 && credits != 3)) {
                System.out.println("Credits must be 4 for course numbers less than 500 and 3 for course numbers greater than or equal to 500.");
                return;
            }

            String sql = "INSERT INTO course_credit (course#, credits) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, courseNum);
                stmt.setInt(2, credits);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Course credit added successfully.");
                } else {
                    System.out.println("Failed to add course credit.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCourseCredit() throws SQLException {
        System.out.print("Enter Course Number to delete: ");
        int courseNum = scanner.nextInt();

        String sql = "DELETE FROM course_credit WHERE course# = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseNum);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course credit deleted successfully.");
            } else {
                System.out.println("No matching course credit found to delete.");
            }
        }
    }
}
