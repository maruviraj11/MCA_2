<%-- 
    Document   : db
    Created on : 10 Apr, 2026, 2:54:07 PM
    Author     : VIRAJ
--%>

<%@ page import="java.sql.*" %>

<%
Connection con = null;

try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    con = DriverManager.getConnection(
        "jdbc:mysql://127.0.0.1:3306/userdb", 
        "root",
        ""
    );

    // DEBUG
    out.println("DB Connected<br>");

} catch(Exception e) {
    out.println("DB Error: " + e);
}
%>