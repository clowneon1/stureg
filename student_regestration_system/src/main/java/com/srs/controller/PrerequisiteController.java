package com.srs.controller;

import com.srs.Dto.CourseDTO;
import com.srs.Dto.PrerequisiteDTO;
import com.srs.config.DatabaseConnection;
import com.srs.management.CoursesManagement;
import com.srs.management.PrerequisitesManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api/prerequisite")
public class PrerequisiteController {

    private final PrerequisitesManagement prerequisitesManagement;
    private final Connection connection;

    public PrerequisiteController(DatabaseConnection databaseConnection) throws SQLException {
        this.connection = databaseConnection.getConnectionInstance();
        this.prerequisitesManagement = new PrerequisitesManagement(connection, new Scanner(System.in));
    }

    @GetMapping
    public List<PrerequisiteDTO> getAllPrerequisite() throws ParseException {
        return prerequisitesManagement.viewAllPrerequisites();
    }
}
