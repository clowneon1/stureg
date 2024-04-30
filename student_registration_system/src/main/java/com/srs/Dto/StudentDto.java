package com.srs.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {
        private String studentId;
        private String firstName;
        private String lastName;
        private String studentLevel;
        private double gpa;
        private String email;
        private Date birthDate;

        public static List<StudentDto> mapFromSQL(List<String> sqlOutputs) throws ParseException {
                List<StudentDto> students = new ArrayList<>();
                System.out.println("************" + sqlOutputs.size());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

                for (String sqlOutput : sqlOutputs) {
                        String[] columns = sqlOutput.split(",");

                        String bNumber = null, firstName = null, lastName = null, level = null,
                                email = null;
                        Date birthDate = null;
                        double gpa = 0.0;

                        for (String col : columns) {
                                String[] parts = col.split(": ");
                                if (parts.length == 2) {
                                        String key = parts[0].trim();
                                        String value = parts[1].trim();

                                        switch (key) {
                                                case "B#":
                                                        bNumber = value;
                                                        break;
                                                case "First Name":
                                                        firstName = value;
                                                        break;
                                                case "Last Name":
                                                        lastName = value;
                                                        break;
                                                case "Level":
                                                        level = value;
                                                        break;
                                                case "GPA":
                                                        gpa = Double.parseDouble(value);
                                                        break;
                                                case "Email":
                                                        email = value;
                                                        break;
                                                case "Birth Date":
                                                        try {
                                                                birthDate = dateFormat.parse(value);
                                                        } catch (ParseException e) {
                                                                System.out.println(e.getMessage());
                                                        }
                                                        break;
                                                default:
                                                        // Handle unknown keys or ignore them
                                                        break;
                                        }
                                }
                        }

                        students.add(new StudentDto(bNumber, firstName, lastName, level, gpa, email, birthDate));
                }

                return students;
        }
}