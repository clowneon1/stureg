package com.srs.Dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
    private int logNumber;
    private String userName;
    private Date operationTime;
    private String tableName;
    private String operation;
    private String tupleKeyValue;

    public static List<LogDTO> mapFromSQL(List<String> sqlOutputs) throws ParseException {
        List<LogDTO> logs = new ArrayList<>();
        System.out.println("************" + sqlOutputs.size());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        for (String sqlOutput : sqlOutputs) {
            System.out.println("sqlOutput = " + sqlOutput);
            String[] columns = sqlOutput.split(",");

            Integer logNumber = null;
            String userName = null, tableName = null, operation = null, tupleKeyValue = null;
            Date operationTime = null;


            for (String col : columns) {
                String[] parts = col.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "Log#":
                            logNumber = Integer.parseInt(value);
                            break;
                        case "User Name":
                            userName = value;
                            break;
                        case "Table Name":
                            tableName = value;
                            break;
                        case "Operation":
                            operation = value;
                            break;
                        case "Tuple Key Value":
                            tupleKeyValue = value;
                            break;
                        case "Operation Time":
                            try {
                                operationTime = dateFormat.parse(value);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            // Handle unknown keys or ignore them
                            break;
                    }
                }
            }

            logs.add(new LogDTO(logNumber, userName, operationTime, tableName, operation, tupleKeyValue));
        }

        return logs;
    }
}
