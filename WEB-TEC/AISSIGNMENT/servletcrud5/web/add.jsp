<%-- 
    Document   : add
    Created on : 8 Mar, 2026, 10:59:37 AM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>


function validateForm()
{
    var name = document.getElementById("name").value.trim();
    var email = document.getElementById("email").value.trim();

    if(name == "")
    {
        alert("Name cannot be blank");
        return false;
    }

    if(email == "")
    {
        alert("Email cannot be blank");
        return false;
    }
     var pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;

    if(!pattern.test(email))
    {
        alert("Enter valid email format");
        return false;
    }

    return true;

   // return true;
}


function noSpace(event)
{
    if(event.key === " ")
    {
        alert("Space is not allowed");
        return false;
    }
}

</script>

    </head>
    <body>
        <h1>ADD STUDENT</h1>
        
<form action="addstudentservlet" method="post" onsubmit="return validateForm()">
    NAME:<input type="text" name="name" id="name" onkeypress="return noSpace(event)" maxlength="20"><br><br>
    EMAIL:<input type="text" name="email" id="email" onkeypress="return noSpace(event)" maxlength="30"><br><br>
            
            <input type="submit" value="ADD STUDENT"><br><br>
            
            <a href="viewstudentservlet">VIEW STUDENT</a>
</form>
    </body>
</html>
