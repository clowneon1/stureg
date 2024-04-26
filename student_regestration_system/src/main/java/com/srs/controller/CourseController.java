package com.srs.controller;

import com.srs.Dto.CourseDTO;
import com.srs.config.DatabaseConnection;
import com.srs.management.CoursesManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CoursesManagement coursesManagement;
    private final Connection connection;

    public CourseController(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnectionInstance();
        this.coursesManagement = new CoursesManagement(connection, new Scanner(System.in));
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() throws ParseException {
        return coursesManagement.viewCourses();
    }
}
