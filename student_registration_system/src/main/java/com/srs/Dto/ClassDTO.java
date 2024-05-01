package com.srs.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassDTO {
    private String classId;
    private String deptCode;
    private int courseNumber;
    private int sectionNumber;
    private int year;
    private String semester;
    private int limit;
    private int classSize;
    private String room;

    public static List<ClassDTO> mapFromSQL(List<String> sqlOutputs) {
        List<ClassDTO> classes = new ArrayList<>();

        for (String sqlOutput : sqlOutputs) {
            String[] columns = sqlOutput.split(",");

            String classId = null, deptCode = null, semester = null, room = null;
            int courseNumber = 0, sectionNumber = 0, year = 0, limit = 0, classSize = 0;

            for (String col : columns) {
                String[] parts = col.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "Class ID":
                            classId = value;
                            break;
                        case "Dept Code":
                            deptCode = value;
                            break;
                        case "Course#":
                            courseNumber = Integer.parseInt(value);
                            break;
                        case "Section#":
                            sectionNumber = Integer.parseInt(value);
                            break;
                        case "Year":
                            year = Integer.parseInt(value);
                            break;
                        case "Semester":
                            semester = value;
                            break;
                        case "Limit":
                            limit = Integer.parseInt(value);
                            break;
                        case "Class Size":
                            classSize = Integer.parseInt(value);
                            break;
                        case "Room":
                            room = value;
                            break;
                        default:
                            // Handle unknown keys or ignore them
                            break;
                    }
                }
            }

            classes.add(new ClassDTO(classId, deptCode, courseNumber, sectionNumber, year, semester, limit, classSize, room));
        }

        return classes;
    }
}
