// import java.sql.Connection;
// import java.sql.DriverManager;
import jva.sql.*;

public class jdbcconnection {

    public static void main(String[] args) {

        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

           
            String url = "jdbc:mysql://localhost:3306/testdb";
            String user = "root";
            String password = "";

           
            Connection con = DriverManager.getConnection(url, user, password);

            
            System.out.println("Database Connected Successfully!");

           
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
