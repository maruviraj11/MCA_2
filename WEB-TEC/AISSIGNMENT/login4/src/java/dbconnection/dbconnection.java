package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbconnection {

public static Connection getConnection() throws Exception{

Class.forName("com.mysql.cj.jdbc.Driver");

Connection con=DriverManager.getConnection(
"jdbc:mysql://localhost:3306/userdb",
"root",
""
);

return con;

}
}