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
                        addStudentInputCLI();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        viewStudentByIDCli();
                        break;
                    case 4:
                        viewStudentsByCourseId();
                        break;
                    case 5:
                        deleteStudentInputCLI();
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

    private void viewStudentByIDCli() throws SQLException {
        System.out.print("Enter student ID: ");
        String studentId = scanner.next();
        viewStudentByID(studentId);
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

    public StudentDto addStudentInputCLI() {
        Scanner scanner = new Scanner(System.in);
        StudentDto studentDto = new StudentDto();

        System.out.print("Enter Student ID: ");
        studentDto.setStudentId(scanner.nextLine().trim());

        System.out.print("Enter First Name: ");
        studentDto.setFirstName(scanner.nextLine().trim());

        System.out.print("Enter Last Name: ");
        studentDto.setLastName(scanner.nextLine().trim());

        System.out.print("Enter Student Level: ");
        studentDto.setStudentLevel(scanner.nextLine().trim());

        System.out.print("Enter GPA: ");
        studentDto.setGpa(scanner.nextDouble());
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Email: ");
        studentDto.setEmail(scanner.nextLine().trim());

        System.out.print("Enter Birth Date (YYYY-MM-DD): ");
        String birthDateString = scanner.nextLine().trim();
        studentDto.setBirthDate(Date.valueOf(birthDateString));

        return studentDto;
    }



    public String addStudent(StudentDto studentDto) {
        try {
            String sql = "INSERT INTO Students (B#, first_name, last_name, st_level, gpa, email, bdate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, studentDto.getStudentId());
                stmt.setString(2, studentDto.getFirstName());
                stmt.setString(3, studentDto.getLastName());
                stmt.setString(4, studentDto.getStudentLevel());
                stmt.setDouble(5, studentDto.getGpa());
                stmt.setString(6, studentDto.getEmail());
                stmt.setDate(7, new Date(studentDto.getBirthDate().getTime()));

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student added successfully.");
                    return "Student added successfully.";
                } else {
                    System.out.println("Failed to add student.");
                    return "Failed to add student.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to add student.";
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

    public String deleteStudent(String studentId)  {

        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call student_management_package.delete_student(?)}");
            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.setString(1,studentId);
            stmt.execute();
            stmt.close();
            List<String> result = dbmsOutput.show();
            dbmsOutput.close();
            return result.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Could not delete Student";
        }

    }
    public void deleteStudentInputCLI() {
        System.out.print("Enter student ID to delete: ");
        String studentId = scanner.next();
        deleteStudent(studentId);
    }
}
