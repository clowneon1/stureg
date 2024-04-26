package com.srs.controller;

import com.srs.Dto.CourseDTO;
import com.srs.Dto.GEnrollmentDTO;
import com.srs.config.DatabaseConfig;
import com.srs.config.DatabaseConnection;
import com.srs.management.CoursesManagement;
import com.srs.management.EnrollmentManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentManagement enrollmentManagement;
    private final Connection connection;

    public EnrollmentController(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnectionInstance();
        this.enrollmentManagement = new EnrollmentManagement(connection, new Scanner(System.in));
    }

    @GetMapping
    public List<GEnrollmentDTO> getAllEnrollments() throws ParseException {
        return enrollmentManagement.viewEnrollments();
    }

    @PostMapping("/studentId/classId")
    public String enrollStudentIntoClass(@RequestParam String studentId,String classId) throws SQLException {
        return enrollmentManagement.addEnrollment(studentId, classId);
    }

    @DeleteMapping("/studentId/classId")
    public String dropStudentFromClass(@RequestParam String studentId,String classId) throws SQLException {
        return enrollmentManagement.deleteEnrollment(studentId, classId);
    }
}
