<%-- 
    Document   : index
    Created on : 21 Feb, 2026, 9:58:59 PM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            form {
                width: 300px;
                margin: 50px auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 10px;
                text-align: center;
            }
            input {
                width: 90%;
                margin: 10px 0;
                padding: 8px;
            }
            button {
                background-color: blue;
                color: white;
                padding: 10px 20px;
                border: none;
                cursor: pointer;
            }
        </style>
        <script>
                function validateForm()
                {
                    let username=document.getElementById("username").value;
                    let password=document.getElementById("password").value;
                    
                    if(username=="")
                    {
                        alert("Enter username dont skip");
                        return false;
                    }
                    if(password=="")
                    {
                        alert("Enter valid password dont skip");
                        return false;
                    }
                    if(password.length<6)
                    {
                        alert("Enter  atlilsts 6 digits");
                        return false;
                    }
                    return true;
                    
                }
        </script>
    </head>
    <body>
        <h1>LOGIN PAGE</h1>
        <form action="login" id='loginpage' method="post" onsubmit="return validateForm()">
            
            <div>
                <label>USERNAME</label><br>
                <input type='text' id='username' placeholder="username"><br>
                <p id="usererror" style="color:red;"></p><br>
            </div>
            <div>
                <label>PASSWORD</label><br>
                <input type="text" id="password" placeholder="password"><br>
                <p id="passworederror" style="color:red;"></p><br>
            </div>
            <button type="submit" value="submit">LOGIN</button>
            
        </form>
    </body>
</html>
