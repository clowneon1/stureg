package org.srs;

import org.srs.management.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        showMenu();
        LogsManagement logsManagement = new LogsManagement();
        logsManagement.displayLogs();
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        ClassesManagement classesManagement = new ClassesManagement();
        CoursesManagement coursesManagement = new CoursesManagement();
        EnrollmentManagement enrollmentManagement = new EnrollmentManagement();
        PrerequisitesManagement prerequisitesManagement = new PrerequisitesManagement();
        StudentsManagement studentsManagement = new StudentsManagement();
        LogsManagement logsManagement = new LogsManagement();

        boolean exit = false;

        while (!exit) {
            System.out.println("Main Menu:");
            System.out.println("1. Classes");
            System.out.println("2. Courses");
            System.out.println("3. Enrollment");
            System.out.println("4. Prerequisites");
            System.out.println("5. Students Management");
            System.out.println("6. Show Logs");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    classesManagement.displayMenu(scanner);
                    break;
                case 2:
                    coursesManagement.displayMenu(scanner);
                    break;
                case 3:
                    enrollmentManagement.displayMenu(scanner);
                    break;
                case 4:
                    prerequisitesManagement.displayMenu(scanner);
                    break;
                case 5:
                    studentsManagement.displayMenu(scanner);
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