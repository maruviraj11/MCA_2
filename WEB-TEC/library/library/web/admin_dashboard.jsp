<%@page import="java.util.List"%>
<%@page import="com.library.dao.UserDAO"%>
<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky admin">
    <div class="container">
        <div class="header">
            <h2>Welcome Admin, <%= user.getEmail() %></h2>
            <div class="header-actions">
                <a href="admin_users.jsp" class="btn btn-secondary">Manage Users</a>
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

        <div class="panel-grid">
            <div class="panel">
                <h3>Add New Librarian</h3>
                <form action="AdminServlet" method="POST" autocomplete="off">
                    <input type="hidden" name="action" value="addLibrarian">
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="pass" required minlength="8">
                    </div>
                    <button type="submit" class="btn">Add Librarian</button>
                </form>
            </div>

            <div class="panel">
                <h3>Remove Librarian</h3>
                <form action="AdminServlet" method="POST" autocomplete="off">
                    <input type="hidden" name="action" value="removeLibrarian">
                    <div class="form-group">
                        <label>Librarian ID</label>
                        <input type="number" name="librarianId" required>
                    </div>
                    <button type="submit" class="btn btn-danger">Remove Librarian</button>
                </form>
            </div>

            <div class="panel">
                <h3>Edit Librarian</h3>
                <p class="muted" style="margin-top:-10px;">Fill ID and update Email and/or Password.</p>
                <form action="AdminServlet" method="POST" autocomplete="off" id="editLibrarianForm">
                    <input type="hidden" name="action" value="editLibrarian">
                    <div class="form-group">
                        <label>Librarian ID</label>
                        <input type="number" id="editLibrarianId" name="librarianId" required>
                    </div>
                    <div class="form-group">
                        <label>New Email (optional)</label>
                        <input type="email" id="editLibrarianEmail" name="email" placeholder="example@mail.com">
                    </div>
                    <div class="form-group">
                        <label>New Password (optional)</label>
                        <input type="password" id="editLibrarianPass" name="pass" placeholder="Leave blank to keep same" minlength="8">
                    </div>
                    <button type="submit" class="btn">Update Librarian</button>
                </form>
            </div>

            <div class="panel">
                <h3>Security</h3>
                <p class="muted">Update your own admin password.</p>
                <a href="change_password.jsp" class="btn btn-secondary">Open Change Password</a>
            </div>
        </div>

        <h3>Current Librarians</h3>
        <table>
            <thead>
                <tr>
                    <th>Librarian ID</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    UserDAO userDao = new UserDAO();
                    List<User> librarians = userDao.getAllLibrarians();
                    for(User lib : librarians) {
                %>
                <tr>
                    <td><%= lib.getId() %></td>
                    <td><%= lib.getEmail() %></td>
                    <td>
                        <button
                            type="button"
                            class="btn btn-small btn-secondary js-edit-librarian"
                            data-id="<%= lib.getId() %>"
                            data-email="<%= lib.getEmail() %>">
                            Edit
                        </button>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
    <script>
        (function () {
            function $(id) { return document.getElementById(id); }

            window.addEventListener("DOMContentLoaded", function () {
                var idInput = $("editLibrarianId");
                var emailInput = $("editLibrarianEmail");
                var passInput = $("editLibrarianPass");
                var editForm = $("editLibrarianForm");
                if (!idInput || !emailInput || !passInput || !editForm) return;

                var buttons = document.querySelectorAll(".js-edit-librarian");
                for (var i = 0; i < buttons.length; i++) {
                    buttons[i].addEventListener("click", function () {
                        idInput.value = this.getAttribute("data-id") || "";
                        emailInput.value = this.getAttribute("data-email") || "";
                        passInput.value = "";
                        passInput.focus();
                        editForm.scrollIntoView({ behavior: "smooth", block: "start" });
                    });
                }
            });
        })();
    </script>
</body>
</html>
