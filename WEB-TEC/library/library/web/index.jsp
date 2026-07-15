<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Library Management System - Login</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="auth">
    <div class="container login-container">
        <center>
            <h2>Library Login</h2>
            <% 
                String error = request.getParameter("error");
                String success = request.getParameter("success");
                if (error != null) {
                    out.println("<p class='error'>Invalid credentials or session expired.</p>");
                }
                if ("registered".equals(success)) {
                    out.println("<p class='msg'>Registration successful! Please login.</p>");
                }
            %>
        </center>
        <form action="LoginServlet" method="POST">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="pass">Password</label>
                <input type="password" id="pass" name="pass" required>
            </div>
            <button type="submit" class="btn" style="width:100%;">Login</button>
        </form>
        <p class="muted" style="text-align:center; margin-top:15px;">
            Don't have an account? <a href="register.jsp">Register here</a>
        </p>
    </div>
    <script src="app.js"></script>
</body>
</html>
