package com.srs.management;



import com.srs.Dto.ClassDTO;
import com.srs.outputs.DbmsOutput;
import com.srs.utility.MenuStrings;

import java.sql.*;
import java.util.List;
import java.util.Scanner;


//@Service
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
                        viewClasses();
                        break;
                    case 2:
                        viewClassByID();
                        break;
                    case 3:
                        addClassCli();
                        break;
                    case 4:
                        deleteClassCli();
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



    public List<ClassDTO> viewClasses(){
        String VIEW_CLASSES_PROCEDURE = "{ call student_management_package.show_classes }";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput(conn);
            dbmsOutput.enable(1000000);
            stmt.execute(VIEW_CLASSES_PROCEDURE);

            stmt.close();
            List<String> result = dbmsOutput.show();
            dbmsOutput.close();
            return ClassDTO.mapFromSQL(result);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewClassByID() {
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

    public String addClass(ClassDTO classInput) throws SQLException {
        String ADD_CLASS_QUERY = "INSERT INTO classes (classid, dept_code, course#, sect#, year, semester, limit, class_size, room) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(ADD_CLASS_QUERY)) {
            ps.setString(1, classInput.getClassId());
            ps.setString(2, classInput.getDeptCode());
            ps.setInt(3, classInput.getCourseNumber());
            ps.setInt(4, classInput.getSectionNumber());
            ps.setInt(5, classInput.getYear());
            ps.setString(6, classInput.getSemester());
            ps.setInt(7, classInput.getLimit());
            ps.setInt(8, classInput.getClassSize());
            ps.setString(9, classInput.getRoom());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                String response = "Class added successfully.";
                System.out.println(response);
                return response;
            } else {
                String response = "Failed to add class.";
                System.out.println(response);
                return response;
            }
        }
    }
    private void deleteClassCli() throws SQLException {
        System.out.println("Enter class ID of the class to delete:");
        String classId = scanner.nextLine();
        deleteClass(classId);
    }
    public String deleteClass(String classId) throws SQLException {
        String DELETE_CLASS_QUERY = "DELETE FROM classes WHERE classid = ?";

        try (PreparedStatement ps = conn.prepareStatement(DELETE_CLASS_QUERY)) {
            ps.setString(1, classId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                String response = "Class deleted successfully.";
                System.out.println(response);
                return response;
            } else {
                String response = "Failed to delete class.";
                System.out.println("Failed to delete class. Class not found.");
                return response;
            }
        }
    }


    private void addClassCli() throws SQLException {
        ClassDTO classDTO = new ClassDTO();
        System.out.println("Enter class ID:");
        classDTO.setClassId(scanner.nextLine());
        System.out.println("Enter department code:");
        classDTO.setDeptCode(scanner.nextLine());
        System.out.println("Enter course number:");
        classDTO.setCourseNumber(scanner.nextInt());
        System.out.println("Enter section number:");
        classDTO.setSectionNumber(scanner.nextInt());
        System.out.println("Enter year:");
        classDTO.setYear(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.println("Enter semester:");
        classDTO.setSemester(scanner.nextLine());
        System.out.println("Enter limit:");
        classDTO.setLimit(scanner.nextInt());
        System.out.println("Enter class size:");
        classDTO.setClassSize(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.println("Enter room:");
        classDTO.setRoom(scanner.nextLine());

        addClass(classDTO);
    }

}
