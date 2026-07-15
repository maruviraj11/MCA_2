<%@ page import="java.sql.*" %>

<%
String uname = request.getParameter("username");
String pass = request.getParameter("password");

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    // Load Driver
    Class.forName("com.mysql.cj.jdbc.Driver");

    // Database Connection
    con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/userdb", "root", "root"
    );

    // Simple Query (No JOIN)
    ps = con.prepareStatement(
        "SELECT * FROM users WHERE username=? AND password=?"
    );

    ps.setString(1, uname);
    ps.setString(2, pass);

    rs = ps.executeQuery();

    if(!rs.next()) {
%>
        <h3>Invalid Username or Password</h3>
        <a href="login.jsp">Try Again</a>
<%
    } else {

        String role = rs.getString("role");

        if(role != null && role.equalsIgnoreCase("admin")) {
%>
            <h2>Welcome Admin: <%= uname %></h2>
<%
        } else {
%>
            <h2>Welcome User: <%= uname %></h2>
<%
        }
    }

} catch(Exception e) {
    out.println("Error: " + e.getMessage());
} finally {
    try { if(rs != null) rs.close(); } catch(Exception e) {}
    try { if(ps != null) ps.close(); } catch(Exception e) {}
    try { if(con != null) con.close(); } catch(Exception e) {}
}
%>