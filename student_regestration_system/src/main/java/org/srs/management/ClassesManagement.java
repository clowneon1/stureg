package org.srs.management;

import org.srs.outputs.DbmsOutput;
import org.srs.utility.MenuStrings;

import java.sql.*;
import java.util.Scanner;

public class ClassesManagement {

    private Connection conn;
    private Scanner scanner;


    public ClassesManagement(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void displayMenu(){
        try{
            while (true) {
                System.out.print(MenuStrings.CLASS_MENU);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewClasses(conn);
                        break;
                    case 2:
                        viewClassByID(conn, scanner);
                        break;
                    case 3:
                        addClass(conn, scanner);
                        break;
                    case 4:
                        deleteClass(conn, scanner);
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

    private void viewClasses(Connection conn){
        String VIEW_CLASSES_PROCEDURE = "{ call student_management_package.show_classes }";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

        org.srs.outputs.DbmsOutput dbmsOutput = new DbmsOutput(conn);
        dbmsOutput.enable(1000000);
        stmt.execute(VIEW_CLASSES_PROCEDURE);

        stmt.close();
        dbmsOutput.show();
        dbmsOutput.close();
        }
         catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void viewClassByID(Connection conn, Scanner scanner) {
        System.out.println("Enter the class ID:");
        String classId = scanner.nextLine();

        String query = "SELECT * FROM classes WHERE classid = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("Class ID: %s,", rs.getString("classid"));
                System.out.printf("Department Code: %s,", rs.getString("dept_code"));
                System.out.printf("Course Number: %d,", rs.getInt("course#"));
                System.out.printf("Section Number: %d,", rs.getInt("sect#"));
                System.out.printf("Year: %d,", rs.getInt("year"));
                System.out.printf("Semester: %s,", rs.getString("semester"));
                System.out.printf("Limit: %d,", rs.getInt("limit"));
                System.out.printf("Class Size: %d,", rs.getInt("class_size"));
                System.out.printf("Room: %s%n", rs.getString("room"));
            } else {
                System.out.println("Class not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addClass(Connection conn, Scanner scanner) throws SQLException {
        String ADD_CLASS_QUERY = "INSERT INTO classes (classid, dept_code, course#, sect#, year, semester, limit, class_size, room) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Enter class ID:");
        String classId = scanner.nextLine();
        System.out.println("Enter department code:");
        String deptCode = scanner.nextLine();
        System.out.println("Enter course number:");
        int courseNumber = scanner.nextInt();
        System.out.println("Enter section number:");
        int sectionNumber = scanner.nextInt();
        System.out.println("Enter year:");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter semester:");
        String semester = scanner.nextLine();
        System.out.println("Enter limit:");
        int limit = scanner.nextInt();
        System.out.println("Enter class size:");
        int classSize = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter room:");
        String room = scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(ADD_CLASS_QUERY)) {
            ps.setString(1, classId);
            ps.setString(2, deptCode);
            ps.setInt(3, courseNumber);
            ps.setInt(4, sectionNumber);
            ps.setInt(5, year);
            ps.setString(6, semester);
            ps.setInt(7, limit);
            ps.setInt(8, classSize);
            ps.setString(9, room);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Class added successfully.");
            } else {
                System.out.println("Failed to add class.");
            }
        }
    }

    private void deleteClass(Connection conn, Scanner scanner) throws SQLException {
        String DELETE_CLASS_QUERY = "DELETE FROM classes WHERE classid = ?";

        System.out.println("Enter class ID of the class to delete:");
        String classId = scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(DELETE_CLASS_QUERY)) {
            ps.setString(1, classId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Class deleted successfully.");
            } else {
                System.out.println("Failed to delete class. Class not found.");
            }
        }
    }

}
