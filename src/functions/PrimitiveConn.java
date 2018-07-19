/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;

/**
 *
 * @author mlei
 */
public class PrimitiveConn {

    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;

    public static ResultSet generateRecordThrows(String schema, String sql, boolean isLocal) {
        ResultSet result = null;
        try {
            if (null != con && !con.isClosed()) {
                con.close();
            }
            if (isLocal) {
                con = DriverManager.getConnection(LOCALHOST + schema, LOCAL_USER, LOCAL_PASSWORD);
            } else {
                con = DriverManager.getConnection(SERVER + schema, USER_NAME, PASSWORD);
            }
//here sonoo is database name, root is username and password
            stmt = con.createStatement();
            stmt.closeOnCompletion();
            result = stmt.executeQuery(sql); //
//            while (rs.next()) {
//                System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//            }
//            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(PrimitiveConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static int updateRecordThrows(String schema, String sql, boolean isLocal) {
        int result=-1;
        try {
            if (null != con && !con.isClosed()) {
                con.close();
            }
            if (isLocal) {
                con = DriverManager.getConnection(LOCALHOST + schema, LOCAL_USER, LOCAL_PASSWORD);
            } else {
                con = DriverManager.getConnection(SERVER + schema, USER_NAME, PASSWORD);
            }
//here sonoo is database name, root is username and password
            stmt = con.createStatement();
            stmt.closeOnCompletion();
            result = stmt.executeUpdate(sql); //
//            while (rs.next()) {
//                System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//            }
//            con.close();

        } catch (SQLException ex) {
            System.out.println("last query..."+sql);
            
            Logger.getLogger(PrimitiveConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static final String VIBRANT_TEST_TRACKING = "vibrant_test_tracking";
    public static final String ASSEMBLE_SCH = "assemble";
    public static final String SERVER = "jdbc:mysql://192.168.10.121:3306/";
    public static final String PASSWORD = "000028";
    public static final String USER_NAME = "hamilton";

    public static final String LOCAL_SCHEMA = "pillar_plate_info";
    public static final String LOCALHOST = "jdbc:mysql://localhost:3306/";
    public static final String LOCAL_PASSWORD = ("LM&L".equals(FolderInitializer.USER) ? "traveler" : "000028");   //"000028";   //"traveler"
    public static final String LOCAL_USER = "root";

}
