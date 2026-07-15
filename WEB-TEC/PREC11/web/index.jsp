<%-- 
    Document   : index
    Created on : 20 Feb, 2026, 10:13:27 PM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            function validationform()
            {
                let name=document.getElementById("name").value;
                let lastname=document.getElementById("lastname").value;
                let email=document.getElementById("email").value;
                let password=document.getElementById("password").value;
                
                if(name=="")
                {
                    alert("NAME REQIERD");
                    return false;
                }
                if(lastname=="")
                {
                    alert("LAST NAME REQIERD");
                    return false;
                }
                if(email=="")
                {
                    alert("EMAIL REQUIERD");
                    return false;
                }
                if(password == "")
                {
                    alert("ENTER PASSWORD");
                    return false;
                }
                if(password.length<6)
                {
                    alert("ENTER A PASSWORD REQIERED FILED");
                    return false;
                }
                alert("LOGIN SUUCCESSFULLY");
                return true;
            }
            </script>
    </head>
    <body>
        <h1>THIS IS A LOGIN PAGE</h1>
        
        <form action="prec"  method="post" onsubmit=" return validationform()">
            
            <label>ENTER A NAME</label><br><br>
            <input type="text" id="name"><br><br>
            <label>ENTER A LAST NAME</label><br><br>
            <input type="text" id="lastname"><br><br>
            <label>ENTER A EMAIL</label><br><br>
            <input type="text" id="email"><br><br>
            
            <label>PASSWORD</label><br><br>
            <input type="password" id="password"><br><br>
            
            <input type="submit" id="sbtn">
            
        </form>
    </body>
</html>
