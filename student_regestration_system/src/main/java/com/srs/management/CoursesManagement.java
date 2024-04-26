package com.srs.management;



import com.srs.Dto.CourseDTO;
import com.srs.utility.MenuStrings;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

//@Service
public class CoursesManagement {

    private Connection conn;
    private Scanner scanner;

    public CoursesManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu(){
        try {
            while (true) {
                System.out.print(MenuStrings.COURSE_MENU);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewCourses();
                        break;
                    case 2:
                        viewCourseByID();
                        break;
                    case 3:
                        addCourse();
                        break;
                    case 4:
                        deleteCourse();
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
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<CourseDTO> viewCourses() {
        String VIEW_COURSES_PROCEDURE = "{ call student_management_package.show_courses }";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

        DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        stmt.execute(VIEW_COURSES_PROCEDURE);

        stmt.close();
        List<String> list =  dbmsOutput.show();
        dbmsOutput.close();
        conn.close();
        return CourseDTO.mapFromSQL(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  void viewCourseByID() {
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

    public void addCourse() throws SQLException {
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

    public void deleteCourse() throws SQLException {
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

