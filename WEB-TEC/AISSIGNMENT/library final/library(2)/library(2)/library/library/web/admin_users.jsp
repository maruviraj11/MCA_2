<%@page import="java.util.List"%>
<%@page import="com.library.dao.UserDAO"%>
<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User admin = (User) session.getAttribute("user");
    if (admin == null || !"admin".equals(admin.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Users</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky admin">
    <div class="container">
        <div class="header">
            <h2>Manage Users</h2>
            <div class="header-actions">
                <a href="admin_dashboard.jsp" class="back-btn">&#8592; Back to Dashboard</a>
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
                <h3>Add New User</h3>
                <form action="AdminServlet" method="POST" autocomplete="off">
                    <input type="hidden" name="action" value="addUser">
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="pass" required minlength="8">
                    </div>
                    <button type="submit" class="btn">Add User</button>
                </form>
            </div>

            <div class="panel">
                <h3>Remove User</h3>
                <form action="AdminServlet" method="POST" autocomplete="off">
                    <input type="hidden" name="action" value="removeUser">
                    <div class="form-group">
                        <label>User ID</label>
                        <input type="number" name="userId" required>
                    </div>
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Remove this user?');">Remove User</button>
                </form>
            </div>

            <div class="panel">
                <h3>Edit User</h3>
                <p class="muted" style="margin-top:-10px;">Fill ID and update Email and/or Password.</p>
                <form action="AdminServlet" method="POST" autocomplete="off" id="editUserForm">
                    <input type="hidden" name="action" value="editUser">
                    <div class="form-group">
                        <label>User ID</label>
                        <input type="number" id="editUserId" name="userId" required>
                    </div>
                    <div class="form-group">
                        <label>New Email (optional)</label>
                        <input type="email" id="editUserEmail" name="email" placeholder="example@mail.com">
                    </div>
                    <div class="form-group">
                        <label>New Password (optional)</label>
                        <input type="password" id="editUserPass" name="pass" placeholder="Leave blank to keep same" minlength="8">
                    </div>
                    <button type="submit" class="btn">Update User</button>
                </form>
            </div>
        </div>

        <h3>Current Users</h3>
        <table>
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    UserDAO userDao = new UserDAO();
                    List<User> users = userDao.getAllUsers();
                    for (User u : users) {
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getEmail() %></td>
                    <td>
                        <div class="table-actions">
                            <button
                                type="button"
                                class="btn btn-small btn-secondary js-edit-user"
                                data-id="<%= u.getId() %>"
                                data-email="<%= u.getEmail() %>">
                                Edit
                            </button>
                            <form action="AdminServlet" method="POST" class="action-form" onsubmit="return confirm('Remove this user?');">
                                <input type="hidden" name="action" value="removeUser">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <button type="submit" class="btn btn-small btn-danger">Remove</button>
                            </form>
                        </div>
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
                var idInput = $("editUserId");
                var emailInput = $("editUserEmail");
                var passInput = $("editUserPass");
                var editForm = $("editUserForm");
                if (!idInput || !emailInput || !passInput || !editForm) return;

                var buttons = document.querySelectorAll(".js-edit-user");
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
