<%-- 
    Document   : viewbook
    Created on : 04-Apr-2026, 5:16:01?pm
    Author     : admin
--%>

<%@ page import= "java.sql.*"%>
<%@ page import= "com.db.DBConnection" %>
<html>
    <head>
        <title>
            VIEW BOOK
        </title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>

        <table border ="1">
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Author</th>
                <th>Action</th>
            </tr>
            <%
                Connection con = DBConnection.getConnnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Book");
                while(rs.next())
                {
            %>
            <tr>
                <td><%= rs.getInt("bookId")%></td>
                <td><%= rs.getString("title")%></td>
                <td><%= rs.getString("author")%></td>
                <td>
                    <a href="editbook.jsp?id=<%=rs.getInt("bookId")%>">Edit</a>
                    <a href="deletebook.jsp?id=<%=rs.getInt("bookId")%>">Delete</a>

                </td>
            </tr>
            <%
                }
            %>
        </table>
            <div style="text-align: center; margin-top: 20px;">
                <a href="addbook.jsp" class="add-btn">+ Add Book</a>
            </div>
    </body>
</html>
