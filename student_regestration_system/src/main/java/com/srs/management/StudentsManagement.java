package com.srs.management;


import com.srs.Dto.StudentDto;
import com.srs.utility.MenuStrings;

import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.util.*;


//@Service
public class StudentsManagement {

    private Connection conn;
    private Scanner scanner;


    public StudentsManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu() {

        try {
            while (true) {
                System.out.println(MenuStrings.STUDENT_MENU);

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        System.out.print("Enter student ID: ");
                        String studentId = scanner.next();
                        viewStudentByID(studentId);
                        break;
                    case 4:
                        viewStudentsByCourseId();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        System.out.println("Exiting Student Management. Goodbye!");
                        return;
                    case 7:
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

    public void viewStudentsByCourseId() {
        System.out.print("Enter Class Id: ");
        String classId = scanner.nextLine().trim();


        try {
            CallableStatement stmt = conn.prepareCall("{ call student_management_package.list_students_in_class(?) }");

            // Set the parameters for the stored procedure
            stmt.setString(1, classId);

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

    public void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();
            System.out.print("Enter Student Level: ");
            String studentLevel = scanner.nextLine().trim();
            System.out.print("Enter GPA: ");
            double gpa = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter Birth Date (YYYY-MM-DD): ");
            String birthDateString = scanner.nextLine().trim();
            Date birthDate = Date.valueOf(birthDateString);

            String sql = "INSERT INTO Students (B#, first_name, last_name, st_level, gpa, email, bdate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, studentId);
                stmt.setString(2, firstName);
                stmt.setString(3, lastName);
                stmt.setString(4, studentLevel);
                stmt.setDouble(5, gpa);
                stmt.setString(6, email);
                stmt.setDate(7, birthDate);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student added successfully.");
                } else {
                    System.out.println("Failed to add student.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<StudentDto> viewAllStudents()  {
        try {
            Statement stmt = conn.createStatement();
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute("{call student_management_package.show_students}");
            stmt.close();
            List<String> result = dbmsOutput.show();
            dbmsOutput.close();
            return StudentDto.mapFromSQL(result);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public Object viewStudentByID(String studentId) throws SQLException {


        String sql = "SELECT * FROM Students WHERE B# = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Student ID: " + rs.getString("B#") +
                            ", First Name: " + rs.getString("first_name") +
                            ", Last Name: " + rs.getString("last_name") +
                            ", Student Level: " + rs.getString("st_level") +
                            ", GPA: " + rs.getDouble("gpa") +
                            ", Email: " + rs.getString("email") +
                            ", Birth Date: " + rs.getDate("bdate"));
                    return new StudentDto(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDouble(5),
                            rs.getString(6),
                            rs.getDate(7));
                } else {
                    String str = "No student found with ID: " + studentId;
                    System.out.println(str);
                    return str;
                }
            }
        }
    }

    public void deleteStudent()  {
        System.out.print("Enter student ID to delete: ");
        String studentId = scanner.next();

        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call student_management_package.delete_student(?)}");
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.setString(1,studentId);
            stmt.execute();
            stmt.close();
            dbmsOutput.show();
            dbmsOutput.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
