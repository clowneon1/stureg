package com.srs.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GEnrollmentDTO {
    private String studentBNumber;
    private String classId;
    private double score;

    public static List<GEnrollmentDTO> mapFromSQL(List<String> sqlOutputs) {
        List<GEnrollmentDTO> enrollments = new ArrayList<>();
        System.out.println("sqlOutputs.size() = " + sqlOutputs.size());

        for (String sqlOutput : sqlOutputs) {
            String[] columns = sqlOutput.split(",");

            String studentBNumber = null, classId = null;
            double score = 0.0;

            for (String col : columns) {
                String[] parts = col.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "B#":
                            studentBNumber = value;
                            break;
                        case "Class ID":
                            classId = value;
                            break;
                        case "Score":
                            if(!value.isEmpty())
                                score = Double.parseDouble(value);
                            else score = 0;
                            break;
                        default:
                            // Handle unknown keys or ignore them
                            break;
                    }
                }
            }

            enrollments.add(new GEnrollmentDTO(studentBNumber, classId, score));
        }

        return enrollments;
    }
}
