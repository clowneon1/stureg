package org.srs.management;


import org.srs.outputs.DbmsOutput;

import java.sql.*;
import java.util.Scanner;

public class CoursesManagement {

    private Connection conn;
    private Scanner scanner;
    private static final String MENU = """
            ---------------------------------------------------------------------------------------
            Course Management Menu:
            1. View Courses
            2. View Course By Course Id
            3. Add Course
            4. Delete Course
            5. Return to previous menu
            6. Exit
            Enter your choice:""";

    public CoursesManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu() throws SQLException {

        while (true) {
            System.out.println(MENU);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewCourses(conn);
                    break;
                case 2:
                    viewCourseByID(conn, scanner);
                    break;
                case 3:
                    addCourse(conn, scanner);
                    break;
                case 4:
                    deleteCourse(conn, scanner);
                    break;
                case 5:
                    return;
                case 6:
                    conn.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

    }


    private void viewCourses(Connection conn) {
        String VIEW_COURSES_PROCEDURE = "{ call student_management_package.show_courses }";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

        org.srs.outputs.DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        stmt.execute(VIEW_COURSES_PROCEDURE);

        stmt.close();
        dbmsOutput.show();
        dbmsOutput.close();
        conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewCourseByID(Connection conn, Scanner scanner) {
            System.out.println("Enter the course ID:");
            String courseId = scanner.nextLine();
            System.out.println("Enter the course Dept Code:");
            String deptCode = scanner.nextLine();

            String query = "SELECT * FROM courses WHERE course# = ? and dept_code = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, courseId);
                ps.setString(2, deptCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.printf("Department Code: %s,", rs.getString("dept_code"));
                    System.out.printf("Course Number: %s,", rs.getString("course#"));
                    System.out.printf("Title: %s%n", rs.getString("title"));
                } else {
                    System.out.println("Course not found.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    private void addCourse(Connection conn, Scanner scanner) throws SQLException {
        String ADD_COURSE_QUERY = "INSERT INTO courses (dept_code, course#, title) VALUES (?, ?, ?)";

        System.out.println("Enter department code:");
        String deptCode = scanner.nextLine();
        System.out.println("Enter course number:");
        String courseNumber = scanner.nextLine();
        System.out.println("Enter course title:");
        String title = scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(ADD_COURSE_QUERY)) {
            ps.setString(1, deptCode);
            ps.setString(2, courseNumber);
            ps.setString(3, title);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course added successfully.");
            } else {
                System.out.println("Failed to add course.");
            }
        }
    }

    private void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE dept_code = ? AND course# = ?";

        System.out.println("Enter department code of the course to delete:");
        String deptCode = scanner.nextLine();
        System.out.println("Enter course number of the course to delete:");
        String courseNumber = scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(DELETE_COURSE_QUERY)) {
            ps.setString(1, deptCode);
            ps.setString(2, courseNumber);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course deleted successfully.");
            } else {
                System.out.println("Failed to delete course. Course not found.");
            }
        }
    }

}

