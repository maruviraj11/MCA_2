<%-- 
    Document   : implicit
    Created on : 13 Mar, 2026, 9:11:38 AM
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
        
<h2>JSP Implicit Object Example</h2>

        <%
        String name = request.getParameter("username");

        if(name != null)
        {
        session.setAttribute("user", name);
        }
        %>

        Welcome : <%= session.getAttribute("user") %> <br>

        Server Name : <%= request.getServerName() %>
    </body>
</html>
