<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Map user = (Map) session.getAttribute("user");
    List departments = (List) request.getAttribute("departments");
    List hods = (List) request.getAttribute("hods");
    List clerks = (List) request.getAttribute("clerks");
    List recentActivities = (List) request.getAttribute("recentActivities");
    Integer notificationCount = (Integer) request.getAttribute("notificationCount");
    Integer totalComplaintCount = (Integer) request.getAttribute("totalComplaintCount");
    Integer pendingComplaintCount = (Integer) request.getAttribute("pendingComplaintCount");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="page">
<div class="shell">
    <div class="topbar">
        <div><span class="badge">ADMIN PANEL</span><h2>Welcome, <%=user.get("fullName")%></h2></div>
        <div class="topbar-actions">
            <div class="notif-wrap">
                <button type="button" class="notif-button" id="adminNotifBtn" aria-expanded="false" aria-controls="adminNotifPanel">
                    <span>Notifications</span>
                    <span class="notif-count <%=notificationCount != null && notificationCount.intValue() > 0 ? "has-count" : ""%>" id="adminNotifCount"><%=notificationCount%></span>
                </button>
                <div class="notif-panel hidden" id="adminNotifPanel">
                    <div class="notif-panel-head">
                        <h3>Admin Updates</h3>
                        <span class="muted">Recent activity</span>
                    </div>
                    <% if (recentActivities == null || recentActivities.isEmpty()) { %>
                    <div class="notif-empty">No recent admin updates available.</div>
                    <% } else { for (int i = 0; i < recentActivities.size(); i++) { Map row = (Map) recentActivities.get(i); %>
                    <div class="notif-item unread">
                        <strong><%=row.get("title")%></strong>
                        <div class="muted"><%=row.get("detail")%></div>
                        <span class="notif-time"><%=row.get("createdAt")%></span>
                    </div>
                    <% }} %>
                </div>
            </div>
            <a href="<%=request.getContextPath()%>/logout">Logout</a>
        </div>
    </div>
    <% if (request.getAttribute("success") != null) { %><div class="message success"><%=request.getAttribute("success")%></div><% } %>
    <% if (request.getAttribute("error") != null) { %><div class="message error"><%=request.getAttribute("error")%></div><% } %>
    <% if (request.getAttribute("credentialEmail") != null) { %>
    <div class="table-card">
        <h3>Login Credentials</h3>
        <table>
            <tr><th>Role</th><th>Email ID</th><th>Temporary Password</th></tr>
            <tr>
                <td><%=request.getAttribute("credentialRole")%></td>
                <td><%=request.getAttribute("credentialEmail")%></td>
                <td><strong><%=request.getAttribute("credentialPassword")%></strong></td>
            </tr>
        </table>
        <p class="muted" style="margin-top:12px;">Use this temporary password for first login, then change it immediately.</p>
    </div>
    <% } %>
    <div class="dashboard-hero">
        <div class="dashboard-head">
            <div>
                <h1>Dashboard Overview</h1>
                <p class="dashboard-subtitle">System-wide statistics and activity</p>
            </div>
        </div>
        <div class="summary-grid four">
            <div class="summary-card">
                <div class="summary-icon campus">&#127970;</div>
                <strong><%=departments.size()%></strong>
                <span>Departments</span>
                <small class="summary-note">System structure ready</small>
            </div>
            <div class="summary-card">
                <div class="summary-icon student">&#128105;</div>
                <strong><%=hods.size()%></strong>
                <span>HODs Assigned</span>
                <small class="summary-note">All departments covered</small>
            </div>
            <div class="summary-card">
                <div class="summary-icon pending">&#128203;</div>
                <strong><%=clerks.size()%></strong>
                <span>Clerks Active</span>
                <small class="summary-note">Department complaint desks</small>
            </div>
            <div class="summary-card">
                <div class="summary-icon complaint">&#128221;</div>
                <strong><%=totalComplaintCount == null ? 0 : totalComplaintCount.intValue()%></strong>
                <span>Total Complaints</span>
                <small class="summary-note"><%=pendingComplaintCount == null ? 0 : pendingComplaintCount.intValue()%> pending review</small>
            </div>
        </div>
    </div>
    <div class="hero legacy-hero">
        <h1>Department Control Center</h1>
        <p class="muted">Create departments, create HOD and clerk accounts, and assign them to the correct department.</p>
        <div class="stats">
            <div class="stat"><span>Total Departments</span><strong><%=departments.size()%></strong></div>
            <div class="stat"><span>Total HOD</span><strong><%=hods.size()%></strong></div>
            <div class="stat"><span>Total Clerks</span><strong><%=clerks.size()%></strong></div>
        </div>
    </div>
    <div class="grid three">
        <div class="panel" id="createDepartmentCard">
            <h3>Create Department</h3>
            <form method="post" action="<%=request.getContextPath()%>/admin" id="createDepartmentForm">
                <input type="hidden" name="action" value="createDepartment">
                <div style="margin-bottom:10px;"><label>Department Name</label><input type="text" name="name" required></div>
                <div style="margin-bottom:10px;"><label>Description</label><textarea name="description"></textarea></div>
                <div style="margin-bottom:10px;"><label>Assign HOD</label><select name="hodUserId"><option value="">Select</option><% for (int i = 0; i < hods.size(); i++) { Map row = (Map) hods.get(i); %><option value="<%=row.get("id")%>"><%=row.get("fullName")%></option><% } %></select></div>
                <div style="margin-bottom:12px;"><label>Assign Clerk</label><select name="clerkUserId"><option value="">Select</option><% for (int i = 0; i < clerks.size(); i++) { Map row = (Map) clerks.get(i); %><option value="<%=row.get("id")%>"><%=row.get("fullName")%></option><% } %></select></div>
                <button type="submit">Save Department</button>
            </form>
        </div>
        <div class="panel" id="createHodCard">
            <h3>Create HOD</h3>
            <form method="post" action="<%=request.getContextPath()%>/admin" id="createHodForm">
                <input type="hidden" name="action" value="createRoleUser">
                <input type="hidden" name="role" value="HOD">
                <div style="margin-bottom:10px;"><label>Full Name</label><input type="text" name="fullName" required></div>
                <div style="margin-bottom:10px;"><label>Email</label><input type="email" name="email" required></div>
                <button type="submit">Create HOD</button>
            </form>
        </div>
        <div class="panel">
            <h3>Create Clerk</h3>
            <form method="post" action="<%=request.getContextPath()%>/admin" id="createClerkForm">
                <input type="hidden" name="action" value="createRoleUser">
                <input type="hidden" name="role" value="CLERK">
                <div style="margin-bottom:10px;"><label>Full Name</label><input type="text" name="fullName" required></div>
                <div style="margin-bottom:10px;"><label>Email</label><input type="email" name="email" required></div>
                <button type="submit">Create Clerk</button>
            </form>
        </div>
    </div>
    <div class="table-card">
        <h3>Department Assignments</h3>
        <table>
            <tr><th>Department</th><th>Description</th><th>HOD</th><th>Clerk</th><th>Edit</th><th>Delete</th></tr>
            <% for (int i = 0; i < departments.size(); i++) { Map row = (Map) departments.get(i); %>
            <tr>
                <td><%=row.get("name")%></td>
                <td><%=row.get("description")%></td>
                <td><%=row.get("hodName")%></td>
                <td><%=row.get("clerkName")%></td>
                <td class="action-cell">
                    <form method="post" action="<%=request.getContextPath()%>/admin" class="stack-form department-edit-form">
                        <input type="hidden" name="action" value="updateDepartment">
                        <input type="hidden" name="departmentId" value="<%=row.get("id")%>">
                        <input type="text" name="name" value="<%=row.get("name")%>" required>
                        <input type="text" name="description" value="<%=row.get("description") == null ? "" : row.get("description")%>">
                        <select name="hodUserId">
                            <option value="">No HOD</option>
                            <% for (int j = 0; j < hods.size(); j++) { Map hod = (Map) hods.get(j); %>
                            <option value="<%=hod.get("id")%>" <%=String.valueOf(hod.get("id")).equals(String.valueOf(row.get("hodUserId"))) ? "selected" : ""%>><%=hod.get("fullName")%></option>
                            <% } %>
                        </select>
                        <select name="clerkUserId">
                            <option value="">No Clerk</option>
                            <% for (int j = 0; j < clerks.size(); j++) { Map clerk = (Map) clerks.get(j); %>
                            <option value="<%=clerk.get("id")%>" <%=String.valueOf(clerk.get("id")).equals(String.valueOf(row.get("clerkUserId"))) ? "selected" : ""%>><%=clerk.get("fullName")%></option>
                            <% } %>
                        </select>
                        <button type="submit">Edit</button>
                    </form>
                </td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/admin">
                        <input type="hidden" name="action" value="deleteDepartment">
                        <input type="hidden" name="departmentId" value="<%=row.get("id")%>">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
    <div class="grid two">
        <div class="table-card">
            <h3>Created HOD Details</h3>
            <table>
                <tr><th>Name</th><th>Email</th><th>Department</th><th>Edit</th><th>Delete</th></tr>
                <% for (int i = 0; i < hods.size(); i++) { Map row = (Map) hods.get(i); %>
                <tr>
                    <td><%=row.get("fullName")%></td>
                    <td><%=row.get("email")%></td>
                    <td><%=row.get("departmentName") == null ? "Not Assigned" : row.get("departmentName")%></td>
                    <td class="action-cell">
                        <form method="post" action="<%=request.getContextPath()%>/admin" class="stack-form role-edit-form" data-role-type="HOD" data-user-id="<%=row.get("id")%>">
                            <input type="hidden" name="action" value="updateRoleUser">
                            <input type="hidden" name="userId" value="<%=row.get("id")%>">
                            <input type="hidden" name="role" value="HOD">
                            <input type="text" name="fullName" value="<%=row.get("fullName")%>" required>
                            <input type="email" name="email" value="<%=row.get("email")%>" required>
                            <button type="submit">Edit</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/admin">
                            <input type="hidden" name="action" value="deleteRoleUser">
                            <input type="hidden" name="userId" value="<%=row.get("id")%>">
                            <input type="hidden" name="role" value="HOD">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
        <div class="table-card">
            <h3>Created Clerk Details</h3>
            <table>
                <tr><th>Name</th><th>Email</th><th>Department</th><th>Edit</th><th>Delete</th></tr>
                <% for (int i = 0; i < clerks.size(); i++) { Map row = (Map) clerks.get(i); %>
                <tr>
                    <td><%=row.get("fullName")%></td>
                    <td><%=row.get("email")%></td>
                    <td><%=row.get("departmentName") == null ? "Not Assigned" : row.get("departmentName")%></td>
                    <td class="action-cell">
                        <form method="post" action="<%=request.getContextPath()%>/admin" class="stack-form role-edit-form" data-role-type="CLERK" data-user-id="<%=row.get("id")%>">
                            <input type="hidden" name="action" value="updateRoleUser">
                            <input type="hidden" name="userId" value="<%=row.get("id")%>">
                            <input type="hidden" name="role" value="CLERK">
                            <input type="text" name="fullName" value="<%=row.get("fullName")%>" required>
                            <input type="email" name="email" value="<%=row.get("email")%>" required>
                            <button type="submit">Edit</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/admin">
                            <input type="hidden" name="action" value="deleteRoleUser">
                            <input type="hidden" name="userId" value="<%=row.get("id")%>">
                            <input type="hidden" name="role" value="CLERK">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>
</div>
<script>
(function () {
    var departmentNames = [
        <% for (int i = 0; i < departments.size(); i++) { Map row = (Map) departments.get(i); %>
        { id: "<%=row.get("id")%>", name: "<%=String.valueOf(row.get("name")).replace("\\", "\\\\").replace("\"", "\\\"")%>".toLowerCase() }<%=i < departments.size() - 1 ? "," : ""%>
        <% } %>
    ];
    var allEmails = [
        <% for (int i = 0; i < hods.size(); i++) { Map row = (Map) hods.get(i); %>
        { id: "<%=row.get("id")%>", email: "<%=String.valueOf(row.get("email")).replace("\\", "\\\\").replace("\"", "\\\"")%>".toLowerCase() }<%= (i < hods.size() - 1 || clerks.size() > 0) ? "," : ""%>
        <% } %>
        <% for (int i = 0; i < clerks.size(); i++) { Map row = (Map) clerks.get(i); %>
        { id: "<%=row.get("id")%>", email: "<%=String.valueOf(row.get("email")).replace("\\", "\\\\").replace("\"", "\\\"")%>".toLowerCase() }<%=i < clerks.size() - 1 ? "," : ""%>
        <% } %>
    ];

    function normalize(value) {
        return (value || "").replace(/\s+/g, " ").trim().toLowerCase();
    }

    function clearErrors(form) {
        Array.prototype.forEach.call(form.querySelectorAll(".field-error"), function (field) {
            field.classList.remove("field-error");
        });
        Array.prototype.forEach.call(form.querySelectorAll(".validation-text"), function (node) {
            node.parentNode.removeChild(node);
        });
    }

    function showError(field, message) {
        field.classList.add("field-error");
        var node = document.createElement("div");
        node.className = "validation-text";
        node.textContent = message;
        field.parentNode.appendChild(node);
    }

    function isValidEmail(value) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
    }

    function validateRoleForm(form, isEdit) {
        clearErrors(form);
        var valid = true;
        var nameField = form.querySelector('input[name="fullName"]');
        var emailField = form.querySelector('input[name="email"]');
        var nameValue = normalize(nameField.value);
        var emailValue = normalize(emailField.value);
        var userId = isEdit ? form.getAttribute("data-user-id") : null;

        if (nameValue.length < 3) {
            showError(nameField, "Name must be at least 3 characters.");
            valid = false;
        }
        if (!isValidEmail(emailValue)) {
            showError(emailField, "Enter a valid email address.");
            valid = false;
        }
        var duplicateEmail = allEmails.some(function (item) {
            return item.email === emailValue && (!userId || item.id !== userId);
        });
        if (duplicateEmail) {
            showError(emailField, "This email already exists in the system.");
            valid = false;
        }
        return valid;
    }

    function validateDepartmentForm(form, isEdit) {
        clearErrors(form);
        var valid = true;
        var nameField = form.querySelector('input[name="name"]');
        var descriptionField = form.querySelector('textarea[name="description"], input[name="description"]');
        var hodField = form.querySelector('select[name="hodUserId"]');
        var clerkField = form.querySelector('select[name="clerkUserId"]');
        var nameValue = normalize(nameField.value);
        var departmentId = isEdit ? form.querySelector('input[name="departmentId"]').value : null;

        if (nameValue.length < 2) {
            showError(nameField, "Department name must be at least 2 characters.");
            valid = false;
        }
        var duplicateName = departmentNames.some(function (item) {
            return item.name === nameValue && (!departmentId || item.id !== departmentId);
        });
        if (duplicateName) {
            showError(nameField, "This department name already exists.");
            valid = false;
        }
        if (descriptionField && normalize(descriptionField.value).length > 0 && normalize(descriptionField.value).length < 3) {
            showError(descriptionField, "Description must be at least 3 characters or leave it blank.");
            valid = false;
        }
        if (hodField && clerkField && hodField.value && clerkField.value && hodField.value === clerkField.value) {
            showError(clerkField, "HOD and Clerk cannot be the same person.");
            valid = false;
        }
        return valid;
    }

    function bindForm(selector, validator) {
        Array.prototype.forEach.call(document.querySelectorAll(selector), function (form) {
            form.addEventListener("submit", function (event) {
                if (!validator(form)) {
                    event.preventDefault();
                }
            });
        });
    }

    bindForm("#createHodForm", function (form) { return validateRoleForm(form, false); });
    bindForm("#createClerkForm", function (form) { return validateRoleForm(form, false); });
    bindForm("#createDepartmentForm", function (form) { return validateDepartmentForm(form, false); });
    bindForm(".role-edit-form", function (form) { return validateRoleForm(form, true); });
    bindForm(".department-edit-form", function (form) { return validateDepartmentForm(form, true); });
})();
(function () {
    var button = document.getElementById("adminNotifBtn");
    var panel = document.getElementById("adminNotifPanel");
    var count = document.getElementById("adminNotifCount");
    var readMarked = false;
    if (!button || !panel) { return; }
    function openPanel() {
        panel.classList.remove("hidden");
        button.setAttribute("aria-expanded", "true");
        if (!readMarked && count && count.textContent !== "0") {
            readMarked = true;
            count.textContent = "0";
            count.classList.remove("has-count");
            var unreadItems = panel.querySelectorAll(".notif-item.unread");
            for (var i = 0; i < unreadItems.length; i++) {
                unreadItems[i].classList.remove("unread");
            }
            if (window.fetch) {
                fetch("<%=request.getContextPath()%>/admin?action=markNotificationsRead", { method: "GET", credentials: "same-origin" });
            }
        }
    }
    function closePanel() {
        panel.classList.add("hidden");
        button.setAttribute("aria-expanded", "false");
    }
    button.addEventListener("click", function (event) {
        event.stopPropagation();
        if (panel.classList.contains("hidden")) {
            openPanel();
            return;
        }
        closePanel();
    });
    document.addEventListener("click", function (event) {
        if (!panel.contains(event.target) && !button.contains(event.target)) {
            closePanel();
        }
    });
    panel.addEventListener("click", function (event) {
        event.stopPropagation();
    });
})();
</script>
</body>
</html>


