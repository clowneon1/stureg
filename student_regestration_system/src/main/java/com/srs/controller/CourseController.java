package com.srs.controller;

import com.srs.Dto.CourseDTO;
import com.srs.config.DatabaseConnection;
import com.srs.management.CoursesManagement;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public String addCourse(@RequestBody CourseDTO courseDTO ) throws SQLException {
        return coursesManagement.addCourse(courseDTO);
    }

    @DeleteMapping("/deptCode/courseNumber")
    public String deleteCourse(@RequestParam String deptCode, String courseNumber) throws SQLException {
        return coursesManagement.deleteCourse(deptCode, courseNumber);
    }
}
