<%-- 
    Document   : action
    Created on : 13 Mar, 2026, 9:12:17 AM
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
        <h2>JSP Action Tag Example</h2>

            <jsp:include page="header.jsp"/>

            <form action="welcome.jsp">

            Enter Name :
            <input type="text" name="name">

            <input type="submit" value="Submit">

            </form>
    </body>
</html>
