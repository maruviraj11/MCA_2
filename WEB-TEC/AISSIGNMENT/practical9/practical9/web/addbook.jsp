<%-- 
    Document   : addbook
    Created on : 04-Apr-2026, 5:15:44?pm
    Author     : admin
--%>
<%@ page import = "java.sql.*" %> 
<%@ page import = "com.db.DBConnection"%>
<html>
    <head>
        <title>ADD BOOK</title>
        <link rel="stylesheet" href="style.css">
        <script src="script.js"></script>
    </head>
    <body>
        <form method="post" onsubmit="return validateForm()">
            Title : <input type="text" name="title" required maxlength="100" oninput="onlyText(this)"><br>
            Author : <input type="text" name="author"  required maxlength="100" oninput="onlyText(this)"><br>
            Price: <input type="number" name="price"  required min="1" max="99999" step="0.01"  oninput="onlyNumber(this)"><br>
            Quantity : <input type="number" name="quantity" required min="1" max="2000" oninput="onlyNumber(this)"><br>
            ISBN : <input type="text" name="isbn"  required maxlength="13"  ><br>
            Publisher : <input type="text" name="publisher" required maxlength="100" oninput="onlyText(this)"><br>
            Year : <input type= number name="year" required min="1900" max="2099"  oninput="onlyNumber(this)"><br>
            Catalogue ID: <input type="number" name="cid" required min="1"  oninput="onlyNumber(this)"><br>

            <input type="submit" value=" Add Book">
            <%
                if (request.getMethod().equals("POST")) {
                    Connection con = DBConnection.getConnnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO Book(title,author,price,quantity,ISBN,publisher,edition_year,catalogueId) VALUES(?,?,?,?,?,?,?,?)");
                    ps.setString(1, request.getParameter("title"));
                    ps.setString(2, request.getParameter("author"));
                    ps.setDouble(3, Double.parseDouble(request.getParameter("price")));
                    ps.setInt(4, Integer.parseInt(request.getParameter("quantity")));
                    ps.setString(5, request.getParameter("isbn"));
                    ps.setString(6, request.getParameter("publisher"));
                    ps.setInt(7, Integer.parseInt(request.getParameter("year")));
                    ps.setInt(8, Integer.parseInt(request.getParameter("cid")));
                    ps.executeUpdate();
                    out.println("Book Added Successfully");
                }
            %>
           
        </form>
    </body>
</html>
