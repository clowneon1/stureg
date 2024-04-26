package com.srs.controller;

import com.srs.config.DatabaseConnection;
import com.srs.management.LogsManagement;
import com.srs.management.StudentsManagement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    private final LogsManagement logsManagement;
    private final Connection connection;

    public LogsController() {
        try {
            this.connection = DatabaseConnection.getConnectionInstance();
            this.logsManagement = new LogsManagement(connection, new Scanner(System.in));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLogs(){
        return new ResponseEntity<>(logsManagement.displayLogs(), HttpStatus.OK);
    }
}
