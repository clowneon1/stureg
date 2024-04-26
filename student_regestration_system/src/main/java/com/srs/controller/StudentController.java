package com.srs.controller;


import com.srs.Dto.StudentDto;
import com.srs.config.DatabaseConfig;
import com.srs.config.DatabaseConnection;
import com.srs.management.EnrollmentManagement;
import com.srs.management.StudentsManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentsManagement studentsManagement;
    private final Connection connection;

    public StudentController(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnectionInstance();
        this.studentsManagement = new StudentsManagement(connection, new Scanner(System.in));
    }


    @GetMapping
    public List<StudentDto> getAllStudent() throws ParseException {
        return studentsManagement.viewAllStudents();
    }

    @GetMapping("/id")
    public ResponseEntity<?> getStudentById(@RequestParam String id) throws SQLException {
        Object response = studentsManagement.viewStudentByID(id);

        if (response instanceof StudentDto) {
            return ResponseEntity.ok().body((StudentDto) response);
        } else if (response instanceof String) {
            return ResponseEntity.ok().body((String) response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected response type");
        }
    }


}
