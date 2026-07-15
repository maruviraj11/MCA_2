<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || !"librarian".equals(user.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Librarian Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky librarian">
    <div class="container">
        <div class="header">
            <h2>Librarian Dashboard</h2>
            <div class="header-actions">
                <a href="change_password.jsp" class="btn btn-secondary">Change Password</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>
        
        <% 
            String msg = request.getParameter("msg");
            if (msg != null) out.println("<p class='msg'>" + msg + "</p>");
            String error = request.getParameter("error");
            if (error != null) out.println("<p class='error'>" + error + "</p>");
        %>

        <div class="hub-grid">
            <a href="librarian_books.jsp" class="hub-card">
                <h3>Manage Books</h3>
                <p>Add new books to the system and manage current inventory copies.</p>
            </a>
            
            <a href="librarian_requests.jsp" class="hub-card">
                <h3>Manage Requests</h3>
                <p>Issue pending book requests, mark books returned, and reject orders.</p>
            </a>

            <a href="change_password.jsp" class="hub-card">
                <h3>Change Password</h3>
                <p>Update your account password to keep your access secure.</p>
            </a>
        </div>
    </div>
    <script src="app.js"></script>
</body>
</html>
