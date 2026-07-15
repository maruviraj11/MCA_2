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

        <%
            RequestDAO reqDao = new RequestDAO();
            List<BookRequest> requests = reqDao.getAllRequests();
        %>

        <div class="table-scroll">
            <table>
                <thead>
                    <tr>
                        <th>Req ID</th>
                        <th>User Name</th>
                        <th>Book Title</th>
                        <th>Requested</th>
                        <th>Issued</th>
                        <th>Due</th>
                        <th>Returned</th>
                        <th>Fine</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                <% if (requests.isEmpty()) { %>
                <tr>
                    <td colspan="9">
                        <div class="empty-state">
                            <strong>No book requests found.</strong>
                            <span>New user requests will appear here automatically.</span>
                        </div>
                    </td>
                </tr>
                <% } %>
                <% for(BookRequest r : requests) { %>
                <tr>
                    <td><%= r.getId() %></td>
                    <td><%= r.getUserName() %></td>
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

                            if (fineAmount.compareTo(java.math.BigDecimal.ZERO) > 0) {
                                out.print(fineAmount + " (Paid: " + paidAmount + ", Due: " + outstanding + ")");
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>
                    <td>
                        <div class="status-cell">
                            <span class="badge badge--<%= r.getStatus() %>"><%= r.getStatus().toUpperCase() %></span>
                        <% if(r.getStatus().equals("pending")) { %>
                            <div class="action-stack status-actions">
                                <form action="LibrarianServlet" method="POST" class="action-form" data-request-action="approve">
                                    <input type="hidden" name="action" value="updateRequest">
                                    <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                    <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                    <input type="hidden" name="status" value="issued">
                                    <input type="hidden" name="source" value="requests">
                                    <input type="date" name="dueDate" class="js-due-date inline-date" required>
                                    <button type="submit" class="btn btn-small btn-success">Approve</button>
                                </form>
                                <form action="LibrarianServlet" method="POST" class="action-form" data-request-action="reject">
                                    <input type="hidden" name="action" value="updateRequest">
                                    <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                    <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                    <input type="hidden" name="status" value="rejected">
                                    <input type="hidden" name="source" value="requests">
                                    <button type="submit" class="btn btn-small btn-danger">Reject</button>
                                </form>
                            </div>
                        <% } else if(r.getStatus().equals("issued")) { %>
                            <form action="LibrarianServlet" method="POST" class="action-form status-actions" data-request-action="return">
                                <input type="hidden" name="action" value="updateRequest">
                                <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                <input type="hidden" name="bookId" value="<%= r.getBookId() %>">
                                <input type="hidden" name="status" value="returned">
                                <input type="hidden" name="source" value="requests">
                                <button type="submit" class="btn btn-small btn-secondary">Mark Returned</button>
                            </form>
                        <% } else if(r.getStatus().equals("returned")) { %>
                            <%
                                if (outstanding.compareTo(java.math.BigDecimal.ZERO) > 0) {
                            %>
                                <form action="LibrarianServlet" method="POST" class="action-form status-actions" data-request-action="finePaid">
                                    <input type="hidden" name="action" value="markFinePaid">
                                    <input type="hidden" name="requestId" value="<%= r.getId() %>">
                                    <input type="hidden" name="source" value="requests">
                                    <button type="submit" class="btn btn-small btn-success">Mark Fine Paid</button>
                                </form>
                            <% } %>
                        <% } %>
                        </div>
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
            function addDays(d, days) {
                var x = new Date(d.getTime());
                x.setDate(x.getDate() + days);
                return x;
            }
            function toDateInputValue(d) {
                return d.getFullYear() + "-" + pad(d.getMonth() + 1) + "-" + pad(d.getDate());
            }

            window.addEventListener("DOMContentLoaded", function () {
                var inputs = document.querySelectorAll("input.js-due-date[type='date']");
                var min = toDateInputValue(new Date());
                var due = addDays(new Date(), 14);
                var val = toDateInputValue(due);
                for (var i = 0; i < inputs.length; i++) {
                    inputs[i].min = min;
                    if (!inputs[i].value) inputs[i].value = val;
                }
            });
        })();
    </script>
</body>
</html>
