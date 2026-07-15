<%-- 
    Document   : login
    Created on : 10 Apr, 2026, 2:54:39 PM
    Author     : VIRAJ
--%>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>

    <script>
        function validateForm() {
            let uname = document.forms["loginForm"]["username"].value;
            let pass = document.forms["loginForm"]["password"].value;

            if(uname == "") {
                alert("Username required");
                return false;
            }

            if(pass == "") {
                alert("Password required");
                return false;
            }

            return true;
        }
    </script>

    <style>
        body { font-family: Arial; background: #f2f2f2; }
        .box {
            width: 300px;
            margin: 100px auto;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0px 0px 10px gray;
        }
        input { width: 100%; padding: 10px; margin: 10px 0; }
        button { width: 100%; padding: 10px; background: blue; color: white; }
    </style>
</head>

<body>

<div class="box">
    <h2>Login</h2>

    <form name="loginForm" action="checkLogin.jsp" method="post" onsubmit="return validateForm()">
        <input type="text" name="username" placeholder="Enter Username">
        <input type="password" name="password" placeholder="Enter Password">
        <button type="submit">Login</button>
    </form>
</div>

</body>
</html>