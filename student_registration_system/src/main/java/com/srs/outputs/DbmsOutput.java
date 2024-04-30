package com.srs.outputs;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbmsOutput {

    private CallableStatement enable_stmt;
    private CallableStatement disable_stmt;
    private CallableStatement show_stmt;


    public DbmsOutput(Connection conn) throws SQLException {
        enable_stmt = conn.prepareCall("begin dbms_output.enable(:1); end;");
        disable_stmt = conn.prepareCall("begin dbms_output.disable; end;");

        show_stmt = conn.prepareCall(
                "declare " +
                        " l_line varchar2(255); " +
                        " l_done number; " +
                        " l_buffer long; " +
                        "begin " +
                        " loop " +
                        " exit when length(l_buffer)+255 > :maxbytes OR l_done = 1; " +
                        " dbms_output.get_line( l_line, l_done ); " +
                        " l_buffer := l_buffer || l_line || chr(10); " +
                        " end loop; " +
                        " :done := l_done; " +
                        " :buffer := l_buffer; " +
                        "end;");
    }

    public void enable(int size) throws SQLException {
        enable_stmt.setInt(1, size);
        enable_stmt.executeUpdate();
    }

    public void disable() throws SQLException {
        disable_stmt.executeUpdate();
    }


    public List<String> show() throws SQLException {
        int done = 0;
        List<String> list = new ArrayList<>();
        show_stmt.registerOutParameter(2, Types.INTEGER);
        show_stmt.registerOutParameter(3, Types.VARCHAR);
        for (;;) {
            show_stmt.setInt(1, 8);
            show_stmt.executeUpdate();
            String str = show_stmt.getString(3);
            list.add(str);
            System.out.print(str);
            if ((done = show_stmt.getInt(2)) == 1)
                break;
        }
        list.remove(list.size()-1);
        return list;
    }


    public void close() throws SQLException {
        enable_stmt.close();
        disable_stmt.close();
        show_stmt.close();
    }
}
