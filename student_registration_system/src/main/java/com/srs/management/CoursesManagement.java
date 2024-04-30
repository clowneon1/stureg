package com.srs.management;


import com.srs.Dto.CourseDTO;
import com.srs.outputs.DbmsOutput;
import com.srs.utility.MenuStrings;

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

    public void displayMenu() {
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
                        addCourseCli();
                        break;
                    case 4:
                        deleteCourseCli();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCourseCli() throws SQLException {
        System.out.println("Enter department code of the course to delete:");
        String deptCode = scanner.nextLine();
        System.out.println("Enter course number of the course to delete:");
        String courseNumber = scanner.nextLine();
        deleteCourse(deptCode, courseNumber);
    }

    private void addCourseCli() throws SQLException {
        CourseDTO courseDTO = new CourseDTO();
        System.out.println("Enter department code:");
        courseDTO.setDeptCode(scanner.nextLine());
        System.out.println("Enter course number:");
        courseDTO.setCourseNumber(Integer.parseInt(scanner.nextLine()));
        System.out.println("Enter course title:");
        courseDTO.setTitle(scanner.nextLine());
        addCourse(courseDTO);
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
            List<String> list = dbmsOutput.show();
            dbmsOutput.close();
            return CourseDTO.mapFromSQL(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewCourseByID() {
        System.out.println("Enter the course Dept Code:");
        String deptCode = scanner.nextLine();
        System.out.println("Enter the course ID:");
        String courseId = scanner.nextLine();

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

    public String addCourse(CourseDTO courseDTO) throws SQLException {
        String ADD_COURSE_QUERY = "INSERT INTO courses (dept_code, course#, title) VALUES (?, ?, ?)";


        try (PreparedStatement ps = conn.prepareStatement(ADD_COURSE_QUERY)) {
            ps.setString(1, courseDTO.getDeptCode());
            ps.setString(2, String.valueOf(courseDTO.getCourseNumber()));
            ps.setString(3, courseDTO.getTitle());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                String response = "Course added successfully.";
                System.out.println(response);
                return response;
            } else {
                String response = "Course not added successfully.";
                System.out.println(response);
                return response;
            }
        }
    }

    public String deleteCourse(String deptCode, String courseNumber) throws SQLException {
        String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE dept_code = ? AND course# = ?";
        String response;


        try (PreparedStatement ps = conn.prepareStatement(DELETE_COURSE_QUERY)) {
            ps.setString(1, deptCode);
            ps.setString(2, courseNumber);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                response = "Course deleted successfully.";
                System.out.println(response);
                return response;
            } else {
                response = "Course not deleted successfully.";
                System.out.println(response);
                return response;
            }
        }
    }

}

