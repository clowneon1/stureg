package com.srs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class StudentRegestrationSystemApplication {

	public static void main(String[] args){
//		init(args);
		SpringApplication.run(StudentRegestrationSystemApplication.class, args);
	}

	public static void init(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.println("1. for cli Interface");
		System.out.println("2. for web Interface");

		Integer choice = sc.nextInt();
		switch (choice) {
			case 1:
                try {
                    SrsCli.cliInit();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
			case 2:
				SpringApplication.run(StudentRegestrationSystemApplication.class, args);
				break;
			default:
				sc.close();
				System.out.println("Invalid choice");
		}
	}

}
