package org.srs.outputs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DbmsOutput
{
    private CallableStatement enable_stmt;
    private CallableStatement disable_stmt;
    private CallableStatement show_stmt;


//    Rules and limits
//    The maximum line size is 32767 bytes.
//    ref : https://asktom.oracle.com/ords/f?p=100:11:0::::P11_QUESTION_ID:45027262935845
    public DbmsOutput( Connection conn ) throws SQLException
    {
        enable_stmt = conn.prepareCall( "begin dbms_output.enable(:1); end;" );
        disable_stmt = conn.prepareCall( "begin dbms_output.disable; end;" );

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
                        "end;" );
    }

    //    The default buffer size is 20000 bytes. The minimum size is 2000 bytes and the maximum is unlimited.
    public void enable( int size ) throws SQLException
    {
        enable_stmt.setInt( 1, size );
        enable_stmt.executeUpdate();
    }


    public void disable() throws SQLException
    {
        disable_stmt.executeUpdate();
    }


    public void show() throws SQLException
    {
        int done = 0;

        show_stmt.registerOutParameter( 2, java.sql.Types.INTEGER );
        show_stmt.registerOutParameter( 3, java.sql.Types.VARCHAR );

        for(;;)
        {
            show_stmt.setInt( 1, 32000 );
            show_stmt.executeUpdate();
            System.out.print( show_stmt.getString(3) );
            if ( (done = show_stmt.getInt(2)) == 1 ) break;
        }
    }


    public void close() throws SQLException
    {
        enable_stmt.close();
        disable_stmt.close();
        show_stmt.close();
    }
}