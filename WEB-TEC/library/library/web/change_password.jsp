<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    String dashboard = "user_dashboard.jsp";
    if ("admin".equals(user.getRole())) dashboard = "admin_dashboard.jsp";
    else if ("librarian".equals(user.getRole())) dashboard = "librarian_dashboard.jsp";

    String msg = request.getParameter("msg");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Change Password</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky">
    <div class="container">
        <div class="header">
            <h2>Change Password</h2>
            <div class="header-actions">
                <a href="<%= dashboard %>" class="btn btn-secondary">Back</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>

        <% if (msg != null) { %>
            <p class="msg"><%= msg %></p>
        <% } %>
        <% if (error != null) { %>
            <p class="error"><%= error %></p>
        <% } %>

        <div class="panel-grid">
            <div class="panel">
                <h3>Update your password</h3>
                <form action="ProfileServlet" method="POST" autocomplete="off">
                    <input type="hidden" name="action" value="changePassword">
                    <div class="form-group">
                        <label>Current Password</label>
                        <input type="password" name="currentPass" required>
                    </div>
                    <div class="form-group">
                        <label>New Password</label>
                        <input type="password" name="newPass" required minlength="8">
                    </div>
                    <div class="form-group">
                        <label>Confirm New Password</label>
                        <input type="password" name="confirmPass" required minlength="8">
                    </div>
                    <button type="submit" class="btn">Change Password</button>
                </form>
                <p class="muted" style="margin-top:12px;">Tip: choose a password you don’t use anywhere else.</p>
            </div>
        </div>
    </div>
    <script src="app.js"></script>
</body>
</html>
