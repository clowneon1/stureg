package org.srs.management;

import org.srs.outputs.DbmsOutput;
import org.srs.utility.MenuStrings;

import java.sql.*;
import java.util.Scanner;

public class PrerequisitesManagement {
    private Connection conn;
    private Scanner scanner;




    public PrerequisitesManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu() {
        try  {
            while (true) {
                System.out.println(MenuStrings.PREREQUISITE_MENU);

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline


                switch (choice) {
                    case 1:
                        addPrerequisite();
                        break;
                    case 2:
                        viewAllPrerequisites();
                        break;
                    case 3:
                        viewPrerequisitesByDeptAndCourse();
                        break;
                    case 4:
                        deletePrerequisite();
                        break;
                    case 5:
                        System.out.println("Exiting Prerequisite Management. Goodbye!");
                        return;
                    case 6:
                        conn.close();
                        System.out.println("Exiting Student Management System. Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewAllPrerequisites() {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_prerequisites}");

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addPrerequisite() throws SQLException {
        System.out.print("Enter Department Code: ");
        String deptCode = scanner.nextLine().trim();
        System.out.print("Enter Course Number: ");
        String courseNum = scanner.nextLine().trim();
        System.out.print("Enter Prerequisite Department Code: ");
        String preDeptCode = scanner.nextLine().trim();
        System.out.print("Enter Prerequisite Course Number: ");
        String preCourseNum = scanner.nextLine().trim();

        String sql = "INSERT INTO Prerequisites (dept_code, course#, pre_dept_code, pre_course#) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, deptCode);
            stmt.setString(2, courseNum);
            stmt.setString(3, preDeptCode);
            stmt.setString(4, preCourseNum);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Prerequisite added successfully.");
            } else {
                System.out.println("Failed to add prerequisite.");
            }
        }
    }
    private void viewPrerequisitesByDeptAndCourse() throws SQLException {
        System.out.print("Enter Department Code: ");
        String deptCode = scanner.nextLine().trim();
        System.out.print("Enter Course Number: ");
        int courseNum = scanner.nextInt();

        try {
            CallableStatement stmt = conn.prepareCall("{ call student_management_package.show_prerequisites(?, ?) }");

            // Set the parameters for the stored procedure
            stmt.setString(1, deptCode); // Replace "dept_code_value" with the actual department code
            stmt.setInt(2, courseNum); // Replace 123 with the actual course number

            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);

            // Execute the stored procedure
            stmt.execute();

            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private  void deletePrerequisite() throws SQLException {
        System.out.print("Enter Department Code: ");
        String deptCode = scanner.nextLine().trim();
        System.out.print("Enter Course Number: ");
        String courseNum = scanner.nextLine().trim();
        System.out.print("Enter Prerequisite Department Code: ");
        String preDeptCode = scanner.nextLine().trim();
        System.out.print("Enter Prerequisite Course Number: ");
        String preCourseNum = scanner.nextLine().trim();

        String sql = "DELETE FROM Prerequisites WHERE dept_code = ? AND course# = ? AND pre_dept_code = ? AND pre_course# = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, deptCode);
            stmt.setString(2, courseNum);
            stmt.setString(3, preDeptCode);
            stmt.setString(4, preCourseNum);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Prerequisite deleted successfully.");
            } else {
                System.out.println("No matching prerequisite found to delete.");
            }
        }
    }
}
