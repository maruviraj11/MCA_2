<%-- 
    Document   : index
    Created on : 7 Mar, 2026, 9:09:05 PM
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
        <h1>THIS IS REGISTERPAGE </h1>
        
        <form action="project" method="post" name="form">
            FIRST NAME:<input type="text" name="fname" id="fname" mexlength="30"><br><br>
            LAST NAME:<input type="text" name="lname" id="lname"  mexlength="30"><br><br>
            PASSWORD:<input type="password" name="password" id="password" maxlength="20"><br><br>
            CONFIRM PASSWORD:<input type="password" name="cpassword" id="cpassword" maxlength="20"><br><br>
            EMAIL:<input type="text" name="email" id="email"><br><br>
            <!--MOBILE:<input type="number" name="mobile" id="mobile"><br><br>-->
            ADDRESS:<textarea name="address" id="address"></textarea><br><br>
            ROLE:<select name="role" id="role">
                <option>admin</option>
                <option>register</option>
                <option>user</option>
            </select><br><br>
            <input type="submit" name="submit" value="submit"><br><br>
        </form>
    </body>
</html>
