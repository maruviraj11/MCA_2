<%-- 
    Document   : index
    Created on : 25 Feb, 2026, 9:20:56 PM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REGISTER PAGE</title>
        <style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f2f2f2;
    }

    h1 {
        text-align: center;
    }

    form {
        width: 350px;
        margin: 20px auto;
        padding: 20px;
        background-color: white;
        border: 1px solid #ccc;
    }

    input[type="text"],
    input[type="password"],
    textarea,
    select {
        width: 100%;
        padding: 6px;
        margin-top: 5px;
        margin-bottom: 10px;
        border: 1px solid #ccc;
    }

    textarea {
        height: 60px;
        resize: none;
    }

    input[type="submit"] {
        padding: 8px 15px;
        background-color: #4CAF50;
        color: white;
        border: none;
        cursor: pointer;
    }

    input[type="submit"]:hover {
        background-color: #45a049;
    }
</style>
        <script>
function validateForm()
{
    var fname = document.forms["regform"]["fname"].value.trim();
    var lname = document.forms["regform"]["lname"].value.trim();
    var password = document.forms["regform"]["password"].value.trim();
    var conpassword = document.forms["regform"]["conpassword"].value.trim();
    var email = document.forms["regform"]["email"].value.trim();
    var mobile = document.forms["regform"]["mobile"].value.trim();
    var address = document.forms["regform"]["address"].value.trim();
    var role = document.forms["regform"]["role"].value;

   var namePattern = /^[A-Za-z]{3,20}$/;
    var emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    var mobilePattern = /^[6-9][0-9]{9}$/;
    var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&]).{8,}$/;


    if (fname=="") 
    {
        alert("do not blank first name filed...");
        return false;
    }
    
    if (!namePattern.test(fname)) {
        alert("First name must contain only alphabets and minimum 3 characters.");
        return false;
    }

  if (lname=="") 
    {
        alert("do not blank last name filed...");
        return false;
    }
    
    if (!namePattern.test(lname)) {
        alert("Last name must contain only alphabets and minimum 3 characters.");
        return false;
    }

    if (password=="") 
    {
        alert("do not blank password filed...");
        return false;
    }
    
    if (!passwordPattern.test(password)) {
        alert("Password must be minimum 8 characters and include uppercase, lowercase, number and special character.");
        return false;
    }

    if (conpassword=="") 
    {
        alert("do not blank conforim password filed...");
        return false;
    }
    if (password !== conpassword) {
        alert("Passwords do not match.");
        return false;
    }

    if (email=="") 
    {
        alert("do not blank email filed...");
        return false;
    }
  
    if (!emailPattern.test(email)) {
        alert("Enter valid email address.");
        return false;
    }

   if (mobile=="") 
    {
        alert("do not blank mobile filed...");
        return false;
    }
    if (!mobilePattern.test(mobile)) {
        alert("Enter valid 10-digit mobile number starting with 6-9.");
        return false;
    }

    if (address=="") 
    {
        alert("do not blank address filed...");
        return false;
    }
    if (address.length < 10) {
        alert("Address must be at least 10 characters.");
        return false;
    }

    
   
    if (role === "") {
        alert("Please select a role.");
        return false;
    }

    alert("Form submitted successfully!");
    return true;
}
</script>
    </head>
    <body>
        <h1>THIS IS A RAGISTER PAGE</h1>
        
        <form name="regform" action="registerservlet" method="post" onsubmit=" return validateForm()">
           
            FIRST NAME:<input type="text" name="fname" maxlength="20"  onkeypress="return /^[A-Za-z ]$/.test(event.key)" required><br><br>
            
           
            SECOUND NAME:<input type="text" name="lname" maxlength="20"  onkeypress="return /^[A-Za-z ]$/.test(event.key)" required><br><br>
            
            PASSWORD<input type="password" name="password"><br><br>
            
           
            CONFORM PASSWORD:<input type="password" name="conpassword"><br><br>
            
           
            EMAIL:<input type="text" name="email"><br><br>
            
            MOBILE<input type="number" name="mobile" maxlength="10" required><br><br>
            
            ADDRESS<textarea name="address"></textarea><br><br>
            
            ROLE<select name="role">
                <option value=""> select role</option>
                <option value="admin">admin</option>
                <option value="registerd">registered</option>
                <option value="guest">guest</option>
            </select>
            
            <br><br>
            
            <input type="submit" name="register" value="Register" onclick="return validateForm()">
            
            
            
        </form>
    </body>
</html>
