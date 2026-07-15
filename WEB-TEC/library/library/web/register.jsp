<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Library Management System - Register</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="auth">
    <div class="container login-container">
        <center>
            <h2>User Registration</h2>
            <% 
                String error = request.getParameter("error");
                if ("exists".equals(error)) {
                    out.println("<p class='error'>Email already exists!</p>");
                } else if ("mismatch".equals(error)) {
                    out.println("<p class='error'>Passwords do not match!</p>");
                } else if ("failed".equals(error)) {
                    out.println("<p class='error'>Registration failed. Please try again.</p>");
                }
            %>
        </center>
        <form action="RegisterServlet" method="POST">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="pass">Password</label>
                <input type="password" id="pass" name="pass" required minlength="8">
            </div>
            <div class="form-group">
                <label for="cpass">Confirm Password</label>
                <input type="password" id="cpass" name="cpass" required minlength="8">
            </div>
            <button type="submit" class="btn" style="width:100%;">Register</button>
        </form>
        <p class="muted" style="text-align:center; margin-top:15px;">
            Already have an account? <a href="index.jsp">Login here</a>
        </p>
    </div>
    <script src="app.js"></script>
</body>
</html>
