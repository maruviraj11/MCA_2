<%@ page import="java.sql.*" %>
<%@ page import= "com.db.DBConnection" %>

<link rel="stylesheet" href="style.css">
<%
int id = Integer.parseInt(request.getParameter("id"));

Connection con = DBConnection.getConnnection();
PreparedStatement ps = con.prepareStatement("DELETE FROM Book WHERE bookId=?");

ps.setInt(1, id);
ps.executeUpdate();

response.sendRedirect("viewbooks.jsp");
%>
