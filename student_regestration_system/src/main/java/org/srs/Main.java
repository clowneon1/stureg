package org.srs;

import org.srs.config.DatabaseConnection;
import org.srs.management.*;
import org.srs.utility.MenuStrings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnectionInstance();
            showMenu(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            conn.close();
        }
//      showMenu();

    }

    private static void showMenu(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        ClassesManagement classesManagement = new ClassesManagement(conn,scanner);
        CoursesManagement coursesManagement = new CoursesManagement(conn,scanner);
        EnrollmentManagement enrollmentManagement = new EnrollmentManagement(conn, scanner);
        PrerequisitesManagement prerequisitesManagement = new PrerequisitesManagement(conn, scanner);
        StudentsManagement studentsManagement = new StudentsManagement(conn, scanner);
        LogsManagement logsManagement = new LogsManagement(conn, scanner);

        boolean exit = false;

        while (!exit) {
            System.out.println(MenuStrings.MAIN_MENU);
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    classesManagement.displayMenu();
                    break;
                case 2:
                    coursesManagement.displayMenu();
                    break;
                case 3:
                    enrollmentManagement.displayMenu();
                    break;
                case 4:
                    prerequisitesManagement.displayMenu();
                    break;
                case 5:
                    studentsManagement.displayMenu();
                    break;
                case 6:
                    logsManagement.displayLogs();
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }

        System.out.println("Exiting program...");
        scanner.close();
    }
}