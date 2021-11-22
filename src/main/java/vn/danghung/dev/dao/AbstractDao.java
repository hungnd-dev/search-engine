package vn.danghung.dev.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class AbstractDao {
    protected Logger logger;

    protected void releaseConnect(Connection conn, PreparedStatement stmt, ResultSet rs){
        try{
            if (conn != null) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
