package com.srs.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrerequisiteDTO {
    private String deptCode;
    private int courseNumber;
    private String preDeptCode;
    private int preCourseNumber;

    public static List<PrerequisiteDTO> mapFromSQL(List<String> sqlOutputs) {
        List<PrerequisiteDTO> prerequisites = new ArrayList<>();

        for (String sqlOutput : sqlOutputs) {
            String[] columns = sqlOutput.split(",");

            String deptCode = null, courseNum = null, preDeptCode = null, preCourseNum = null;

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
                            courseNum = value;
                            break;
                        case "Pre-Dept Code":
                            preDeptCode = value;
                            break;
                        case "Pre-Course#":
                            preCourseNum = value;
                            break;
                        default:
                            // Handle unknown keys or ignore them
                            break;
                    }
                }
            }

            // Handle potential null values after parsing
            if (deptCode != null && courseNum != null && preDeptCode != null && preCourseNum != null) {
                prerequisites.add(new PrerequisiteDTO(deptCode, Integer.parseInt(courseNum), preDeptCode, Integer.parseInt(preCourseNum)));
            }
        }

        return prerequisites;
    }
}
