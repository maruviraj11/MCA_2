import java.sql.*;

public class DatabaseUtil {

    private static final String URL =
        "jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() 
    throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

 
    public static void insertStudent(
            String name, String address, String email,
            String phone, String course, int age, String type) {

        String sql =
            "INSERT INTO student(name,address,email,phone,course,age,student_type) " +
            "VALUES(?,?,?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, course);
            ps.setInt(6, age);
            ps.setString(7, type);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    public static ResultSet getAllStudents(Connection con) throws SQLException {
        return con.createStatement().executeQuery("SELECT * FROM student");
    }

   
    public static void updateStudent(
            int id,
            String name, String address, String email,
            String phone, String course, int age, String type) {

        String sql =
            "UPDATE student SET name=?,address=?,email=?,phone=?,course=?,age=?,student_type=? " +
            "WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, course);
            ps.setInt(6, age);
            ps.setString(7, type);
            ps.setInt(8, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    public static void deleteStudent(int id) {

        try (Connection con = getConnection();
             PreparedStatement ps =
                 con.prepareStatement("DELETE FROM student WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
