<%@page import="java.util.List"%>
<%@page import="com.library.model.BookRequest"%>
<%@page import="com.library.dao.RequestDAO"%>
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
    <title>Manage Book Requests</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky librarian">
    <div class="container">
        <div class="header">
            <h2>Manage Book Requests</h2>
            <div class="header-actions">
                <a href="librarian_dashboard.jsp" class="back-btn">&#8592; Back to Dashboard</a>
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
                    <th>User Name</th>
                    <th>Book Title</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    RequestDAO reqDao = new RequestDAO();
                    List<BookRequest> requests = reqDao.getAllRequests();
                    for(BookRequest r : requests) {
                %>
                <tr>
                    <td><%= r.getId() %></td>
                    <td><%= r.getUserName() %></td>
                    <td><%= r.getBookTitle() %></td>
                    <td><%= r.getRequestDate() %></td>
                    <td><span class="badge badge--<%= r.getStatus() %>"><%= r.getStatus().toUpperCase() %></span></td>
                    <td>
                        <% if(r.getStatus().equals("pending")) { %>
                            <div class="table-actions">
                                <form action="LibrarianServlet" method="POST">
                                    <input type="hidden" name="action" value="updateRequest">
                                    <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                    <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                    <input type="hidden" name="status" value="issued">
                                    <input type="hidden" name="source" value="requests">
                                    <button type="submit" class="btn btn-small btn-success">Issue</button>
                                </form>
                                <form action="LibrarianServlet" method="POST">
                                    <input type="hidden" name="action" value="updateRequest">
                                    <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                    <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                    <input type="hidden" name="status" value="rejected">
                                    <input type="hidden" name="source" value="requests">
                                    <button type="submit" class="btn btn-small btn-danger">Reject</button>
                                </form>
                            </div>
                        <% } else if(r.getStatus().equals("issued")) { %>
                            <form action="LibrarianServlet" method="POST">
                                <input type="hidden" name="action" value="updateRequest">
                                <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                <input type="hidden" name="status" value="returned">
                                <input type="hidden" name="source" value="requests">
                                <button type="submit" class="btn btn-small btn-secondary">Mark Returned</button>
                            </form>
                        <% } else { %>
                            -
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
</body>
</html>
