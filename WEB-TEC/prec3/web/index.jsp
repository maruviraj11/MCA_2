<%-- 
    Document   : index
    Created on : 7 Mar, 2026, 10:06:03 AM
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
        <h1>THIS IS REGISTER PAGE</h1>
        
        <form action="formservlet" method="post" name="rform">
            FIRST NAME:<input type="text" name="fname" id="fname" mexlenght="20" required><br><br>
            LAST NAME:<input type="text" name="lname" id="lname" mexlength="20" required><br><br>
            PASSWORD:<input type="password" name="password" id="password"><br><br>
            CONFIRM_PASSWORD<input type="password" name="cpassword" id="cpassword"><br><br>
            EMAIL<input type="text" name="email" id="email"><br><br>
            ADDRESS<textarea name="address" id="address"></textarea><br><br>
            ROLE<select name="role" id="role"><br><br>
                <option>admin</option>
                <option>registrar</option>
                <option>user</option>
                
            </select><br><br>
            <input type="submit" value="submit" name="submit"><br><br>
            
        </form>
    </body>
</html>
