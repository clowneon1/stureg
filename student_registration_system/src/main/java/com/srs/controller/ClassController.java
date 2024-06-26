package com.srs.controller;


import com.srs.Dto.ClassDTO;
import com.srs.Dto.CourseDTO;
import com.srs.config.DatabaseConnection;
import com.srs.management.ClassesManagement;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    private final ClassesManagement classesManagement;
    private final Connection connection;

    public ClassController(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnectionInstance();
        this.classesManagement = new ClassesManagement(connection, new Scanner(System.in));
    }


    @GetMapping
    public List<ClassDTO> getAllClasses() {
        return classesManagement.viewClasses();
    }

    @PostMapping
    public String createClass(@RequestBody ClassDTO classDTO) throws SQLException {
        Boolean input = true;
        return classesManagement.addClass(classDTO);
    }

    @DeleteMapping("/classId")
    public String deleteClass(@RequestParam String classId) throws SQLException {
        return classesManagement.deleteClass(classId);
    }
}
