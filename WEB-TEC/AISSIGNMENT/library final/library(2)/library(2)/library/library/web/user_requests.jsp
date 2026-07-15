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

        <div class="table-scroll">
            <table>
                <thead>
                    <tr>
                        <th>Req ID</th>
                        <th>Book Title</th>
                        <th>Requested</th>
                        <th>Issued</th>
                        <th>Due</th>
                        <th>Returned</th>
                        <th>Fine</th>
                        <th>Return</th>
                        <th>Pay</th>
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
                    <td><%= (r.getIssueDate() == null) ? "-" : r.getIssueDate() %></td>
                    <td><%= (r.getDueDate() == null) ? "-" : r.getDueDate() %></td>
                    <td><%= (r.getReturnDate() == null) ? "-" : r.getReturnDate() %></td>
                    <td>
                        <%
                            java.math.BigDecimal fineAmount = (r.getFineAmount() == null) ? java.math.BigDecimal.ZERO : r.getFineAmount();
                            java.math.BigDecimal paidAmount = (r.getPaidAmount() == null) ? java.math.BigDecimal.ZERO : r.getPaidAmount();
                            java.math.BigDecimal outstanding = fineAmount.subtract(paidAmount);
                            if (outstanding.compareTo(java.math.BigDecimal.ZERO) < 0) outstanding = java.math.BigDecimal.ZERO;
                            java.text.DecimalFormat rupee = new java.text.DecimalFormat("₹#,##0.00");

                            if (fineAmount.compareTo(java.math.BigDecimal.ZERO) > 0) {
                                out.print(rupee.format(fineAmount) + " (Paid: " + rupee.format(paidAmount) + ", Due: " + rupee.format(outstanding) + ")");
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <%
                            if ("issued".equals(r.getStatus())) {
                        %>
                            <form action="UserServlet" method="POST" style="display:flex;gap:8px;flex-wrap:wrap;align-items:center;">
                                <input type="hidden" name="action" value="returnBook">
                                <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                <input type="datetime-local" name="returnAt" required>
                                <button type="submit" class="btn btn-small btn-secondary">Return</button>
                            </form>
                        <%
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <%
                            if (outstanding.compareTo(java.math.BigDecimal.ZERO) > 0) {
                        %>
                            <a class="btn btn-small btn-secondary" href="PaymentServlet?action=start&requestId=<%= r.getId() %>">Pay Online</a>
                        <%
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <span class="badge badge--<%= r.getStatus() %>"><%= r.getStatus().toUpperCase() %></span>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
    <script src="app.js"></script>
    <script>
        (function () {
            function pad(n) { return n < 10 ? "0" + n : "" + n; }
            function nowLocal() {
                var d = new Date();
                return d.getFullYear() + "-" + pad(d.getMonth() + 1) + "-" + pad(d.getDate()) + "T" + pad(d.getHours()) + ":" + pad(d.getMinutes());
            }

            window.addEventListener("DOMContentLoaded", function () {
                var inputs = document.querySelectorAll("input[type='datetime-local'][name='returnAt']");
                var minVal = nowLocal();
                for (var i = 0; i < inputs.length; i++) {
                    inputs[i].min = minVal;
                    if (!inputs[i].value) inputs[i].value = minVal;
                }
            });
        })();
    </script>
</body>
</html>
