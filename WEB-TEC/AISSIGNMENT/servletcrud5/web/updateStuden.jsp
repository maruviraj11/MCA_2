<%-- 
    Document   : updateStuden
    Created on : 8 Mar, 2026, 11:53:50 AM
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
       
        <h2>Update Student</h2>

<form action="UpdateStudentServlet" method="post" onsubmit="return validateForm()">

    ID: <input type="text" name="id" onkeypress="return noSpace(event)" maxlength="10"><br><br>

    Name: <input type="text" name="name"onkeypress="return noSpace(event)" maxlength="20"><br><br>

    Email: <input type="text" name="email" onkeypress="return noSpace(event)" maxlength="30"><br><br>

<input type="submit" value="Update">

</form>

    </body>
</html>
