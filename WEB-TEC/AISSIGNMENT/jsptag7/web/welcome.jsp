<%-- 
    Document   : welcome
    Created on : 13 Mar, 2026, 9:12:56 AM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
                <%
        String name = request.getParameter("name");
        %>

        <h2>Welcome <%= name %></h2>
    </body>
</html>
