<%@page import="com.library.model.BookRequest"%>
<%@page import="com.library.dao.RequestDAO"%>
<%@page import="java.util.List"%>
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
    <title>My Requests</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky user">
    <div class="container">
        <div class="header">
            <h2>My Requests</h2>
            <div class="header-actions">
                <a href="user_dashboard.jsp" class="back-btn">&#8592; Back to Dashboard</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>
        
        <% 
            String msg = request.getParameter("msg");
            if (msg != null) out.println("<p class='msg'>" + msg + "</p>");
            String error = request.getParameter("error");
            if (error != null) out.println("<p class='error'>" + error + "</p>");
        %>

        <table>
            <thead>
                <tr>
                    <th>Req ID</th>
                    <th>Book Title</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    RequestDAO reqDao = new RequestDAO();
                    List<BookRequest> myRequests = reqDao.getRequestsByUser(user.getId());
                    for(BookRequest r : myRequests) {
                %>
                <tr>
                    <td><%= r.getId() %></td>
                    <td><%= r.getBookTitle() %></td>
                    <td><%= r.getRequestDate() %></td>
                    <td>
                        <span class="badge badge--<%= r.getStatus() %>"><%= r.getStatus().toUpperCase() %></span>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
</body>
</html>
