<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || !"user".equals(user.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky user">
    <div class="container">
        <div class="header">
            <h2>Welcome, <%= user.getEmail() %></h2>
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
            <a href="user_books.jsp" class="hub-card">
                <h3>Available Books</h3>
                <p>Browse the library catalog, search by title, and request books.</p>
            </a>
            
            <a href="user_requests.jsp" class="hub-card">
                <h3>My Requests</h3>
                <p>Track the status of your requested, issued, and returned books.</p>
            </a>

            <a href="change_password.jsp" class="hub-card">
                <h3>Change Password</h3>
                <p>Update your account password anytime.</p>
            </a>
        </div>
    </div>
    <script src="app.js"></script>
</body>
</html>
