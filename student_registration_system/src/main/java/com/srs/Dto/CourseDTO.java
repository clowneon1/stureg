package com.srs.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    private String deptCode;
    private int courseNumber;
    private String title;

    public static List<CourseDTO> mapFromSQL(List<String> sqlOutputs) {
        List<CourseDTO> courses = new ArrayList<>();

        for (String sqlOutput : sqlOutputs) {
            String[] columns = sqlOutput.split(",");

            String deptCode = null, title = null;
            int courseNumber = 0;

            for (String col : columns) {
                String[] parts = col.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "Dept Code":
                            deptCode = value;
                            break;
                        case "Course#":
                            courseNumber = Integer.parseInt(value);
                            break;
                        case "Title":
                            title = value;
                            break;
                        default:
                            // Handle unknown keys or ignore them
                            break;
                    }
                }
            }

            courses.add(new CourseDTO(deptCode, courseNumber, title));
        }

        return courses;
    }
}

