package enginetest;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
    private static Connection conn;
    private static String x;
    private static String s;

    public static void main(String[] a) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
       


        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "", "");
            Statement stmt = conn.createStatement();
//    stmt.executeUpdate("CREATE TABLE UserGames");
//    stmt.executeUpdate("ALTER TABLE UserGames ADD ID INT ");
            //tmt.executeUpdate("ALTER TABLE UserGames ADD Game_Name VARCHAR_IGNORECASE");
            ResultSet rs = stmt.executeQuery("SELECT Game_Name, Filename FROM AuroraCoverDB WHERE ID=3");
//    stmt.executeUpdate("ALTER TABLE AuroraCoverDB ALTER Game_Name SET NOT NULL");

    while (rs.next()) {
         x = rs.getString("Game_Name");
         s = rs.getString("Filename");

    }
    
    System.out.println(x + " " + s);




        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
}