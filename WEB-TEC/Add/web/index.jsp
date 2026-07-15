<%-- 
    Document   : index
    Created on : 13 Feb, 2026, 12:21:01 PM
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
        <h1>THIS IS ADDITION PROGRAM</h1>
        
        <form action="addServlet" method="post">
            <label>Enter num1</label>
            <input type="number" name="num1">
            
            <label>Enter num2</label>
            <input type="number" name="num2">
            
            <input type="submit" value="add">
        </form>
    </body>
</html>
