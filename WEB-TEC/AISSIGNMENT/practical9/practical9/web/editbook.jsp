<%-- 
    Document   : editbook
    Created on : 04-Apr-2026, 5:16:11?pm
    Author     : admin
--%>


<%@ page import="java.sql.*" %>
<%@ page import= "com.db.DBConnection"%>
<link rel="stylesheet" href="style.css">
<script src="script.js"></script>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    Connection con = DBConnection.getConnnection();
    if (request.getMethod().equals("POST")) {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Book SET title=?, author=?, price=?, quantity=?, ISBN=?, publisher=?, edition_year=?, catalogueId=? WHERE bookId=?");
        ps.setString(1, request.getParameter("title"));
        ps.setString(2, request.getParameter("author"));
        ps.setDouble(3, Double.parseDouble(request.getParameter("price")));
        ps.setInt(4, Integer.parseInt(request.getParameter("title")));
        ps.setString(5, request.getParameter("isbn"));
        ps.setString(6, request.getParameter("publisher"));
        ps.setInt(7, Integer.parseInt(request.getParameter("year")));
        ps.setInt(8, Integer.parseInt(request.getParameter("cid")));
        ps.executeUpdate();
        out.println("update Successfully");
        
    }
    PreparedStatement ps2 = con.prepareStatement("SELECT * FROM Book WHERE bookId=?");
    ps2.setInt(1,id);
    ResultSet rs = ps2.executeQuery();
    rs.next();   
%>
<form method="post">
Title: <input type="text" name="title" required maxlength="100" oninput="onlyText(this)" value="<%=rs.getString("title")%>"><br>

Author: <input type="text" name="author" required maxlength="100" oninput="onlyText(this)" value="<%=rs.getString("author")%>"><br>

Price: <input type="number" name="price" required min="1" max="99999" step="0.01"  oninput="onlyNumber(this)" value="<%=rs.getDouble("price")%>"><br>

Quantity: <input type="text" name="quantity" required min="1" max="2000" oninput="onlyNumber(this)" value="<%=rs.getInt("quantity")%>"><br>

ISBN: <input type="text" name="isbn" required maxlength="13" value="<%=rs.getString("ISBN")%>"><br>
 
Publisher: <input type="text" name="publisher" required maxlength="100" oninput="onlyText(this)" value="<%=rs.getString("publisher")%>"><br>

Edition Year: <input type="text" name="year" required min="1900" max="2099"  oninput="onlyNumber(this)" value="<%=rs.getInt("edition_year")%>"><br>

Catalogue ID: <input type="text" name="cid" required min="1"  oninput="onlyNumber(this)" value="<%=rs.getInt("catalogueId")%>"><br>

<input type="submit" value="Update Book">
</form>