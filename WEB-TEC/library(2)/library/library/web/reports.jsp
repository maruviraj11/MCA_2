<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || (!"admin".equals(user.getRole()) && !"librarian".equals(user.getRole()))) {
        response.sendRedirect("index.jsp");
        return;
    }

    String back = "admin".equals(user.getRole()) ? "admin_dashboard.jsp" : "librarian_dashboard.jsp";
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reports</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky <%= user.getRole() %>">
    <div class="container">
        <div class="header">
            <h2>Reports (CSV)</h2>
            <div class="header-actions">
                <a href="<%= back %>" class="back-btn">&#8592; Back</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>

        <div class="hub-grid">
            <a class="hub-card" href="ReportServlet?type=issued">
                <h3>Issued Books</h3>
                <p>Download all currently issued books.</p>
            </a>
            <a class="hub-card" href="ReportServlet?type=overdue">
                <h3>Overdue Books</h3>
                <p>Download only overdue issued books.</p>
            </a>
            <a class="hub-card" href="ReportServlet?type=fines">
                <h3>Fines</h3>
                <p>Download all returned books with fines.</p>
            </a>
        </div>
    </div>
    <script src="app.js"></script>
</body>
</html>
