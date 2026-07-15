<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Map user = (Map) session.getAttribute("user");
    List classes = (List) request.getAttribute("classes");
    List students = (List) request.getAttribute("students");
    List staffMembers = (List) request.getAttribute("staffMembers");
    List selectedClassStudents = (List) request.getAttribute("selectedClassStudents");
    List recentActivities = (List) request.getAttribute("recentActivities");
    Integer notificationCount = (Integer) request.getAttribute("notificationCount");
    Integer activeComplaintCount = (Integer) request.getAttribute("activeComplaintCount");
    Map complaintStatusCounts = (Map) request.getAttribute("complaintStatusCounts");
    String activeView = String.valueOf(request.getAttribute("activeView"));
    Object selectedClassId = request.getAttribute("selectedClassId");
    boolean hasDepartment = user.get("departmentId") != null && !"null".equals(String.valueOf(user.get("departmentId")));
    int pendingReviewCount = complaintStatusCounts == null || complaintStatusCounts.get("PENDING") == null ? 0 : Integer.parseInt(String.valueOf(complaintStatusCounts.get("PENDING")));
    int inProgressCount = complaintStatusCounts == null || complaintStatusCounts.get("ASSIGNED") == null ? 0 : Integer.parseInt(String.valueOf(complaintStatusCounts.get("ASSIGNED")));
    int rejectedCount = complaintStatusCounts == null || complaintStatusCounts.get("REJECTED") == null ? 0 : Integer.parseInt(String.valueOf(complaintStatusCounts.get("REJECTED")));
    int resolvedCount = complaintStatusCounts == null || complaintStatusCounts.get("SOLVED") == null ? 0 : Integer.parseInt(String.valueOf(complaintStatusCounts.get("SOLVED")));
    int statusTotal = pendingReviewCount + inProgressCount + rejectedCount + resolvedCount;
%>
<!DOCTYPE html>
<html>
<head>
    <title>HOD Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="page">
<div class="shell">
    <div class="topbar">
        <div><span class="badge">HOD PANEL</span><h2><%=user.get("departmentName")%> Department</h2></div>
        <div class="topbar-actions">
            <div class="notif-wrap">
                <button type="button" class="notif-button" id="hodNotifBtn" aria-expanded="false" aria-controls="hodNotifPanel">
                    <span>Notifications</span>
                    <span class="notif-count <%=notificationCount != null && notificationCount.intValue() > 0 ? "has-count" : ""%>" id="hodNotifCount"><%=notificationCount%></span>
                </button>
                <div class="notif-panel hidden" id="hodNotifPanel">
                    <div class="notif-panel-head">
                        <h3>HOD Updates</h3>
                        <span class="muted">Recent activity</span>
                    </div>
                    <% if (recentActivities == null || recentActivities.isEmpty()) { %>
                    <div class="notif-empty">No recent HOD updates available.</div>
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
    <div class="menu-bar">
        <a class="menu-link <%= "overview".equals(activeView) ? "active" : "" %>" href="<%=request.getContextPath()%>/hod?view=overview">Overview</a>
        <a class="menu-link <%= "classes".equals(activeView) ? "active" : "" %>" href="<%=request.getContextPath()%>/hod?view=classes">Classes</a>
        <a class="menu-link <%= "classStudents".equals(activeView) ? "active" : "" %>" href="<%=request.getContextPath()%>/hod?view=classStudents">Class Students</a>
        <a class="menu-link <%= "staff".equals(activeView) ? "active" : "" %>" href="<%=request.getContextPath()%>/hod?view=staff">Staff</a>
        <a class="menu-link <%= "cr".equals(activeView) ? "active" : "" %>" href="<%=request.getContextPath()%>/hod?view=cr">CR View</a>
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
    <% if (request.getAttribute("bulkCreatedUsers") != null) { List bulkCreatedUsers = (List) request.getAttribute("bulkCreatedUsers"); %>
    <div class="table-card">
        <h3>Bulk Student Credentials</h3>
        <table>
            <tr><th>Name</th><th>Email ID</th><th>Temporary Password</th></tr>
            <% for (int i = 0; i < bulkCreatedUsers.size(); i++) { Map row = (Map) bulkCreatedUsers.get(i); %>
            <tr>
                <td><%=row.get("fullName")%></td>
                <td><%=row.get("email")%></td>
                <td><strong><%=row.get("password")%></strong></td>
            </tr>
            <% } %>
        </table>
    </div>
    <% } %>
    <div class="dashboard-hero">
        <div class="dashboard-head">
            <div>
                <h1>HOD Dashboard</h1>
                <p class="dashboard-subtitle"><%=user.get("departmentName")%> Department</p>
            </div>
        </div>
        <div class="summary-grid four">
            <div class="summary-card">
                <div class="summary-icon campus">&#127979;</div>
                <strong><%=classes.size()%></strong>
                <span>Classes Created</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon student">&#127891;</div>
                <strong><%=students.size()%></strong>
                <span>Total Students</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon staff">&#128105;&#8205;&#127979;</div>
                <strong><%=staffMembers.size()%></strong>
                <span>Staff Members</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon complaint">&#128221;</div>
                <strong><%=activeComplaintCount == null ? 0 : activeComplaintCount.intValue()%></strong>
                <span>Active Complaints</span>
            </div>
        </div>
    </div>
    <div class="hero legacy-hero">
        <h1>Academic Role Management</h1>
        <p class="muted">Create classes, add students and staff, and assign eligible students as class representatives.</p>
        <div class="stats">
            <div class="stat"><span>Classes</span><strong><%=classes.size()%></strong></div>
            <div class="stat"><span>Students / CR</span><strong><%=students.size()%></strong></div>
            <div class="stat"><span>Staff</span><strong><%=staffMembers.size()%></strong></div>
        </div>
    </div>
    <% if (!hasDepartment) { %>
    <div class="message error">Department is not assigned to this HOD account. Admin must assign a department before you can create classes, students, or staff.</div>
    <% } %>
    <% if (hasDepartment) { %>
    <div class="grid three">
        <% if ("overview".equals(activeView) || "classes".equals(activeView)) { %>
        <div class="panel" id="createClassCard">
            <h3>Create Class</h3>
            <form method="post" action="<%=request.getContextPath()%>/hod">
                <input type="hidden" name="action" value="createClass">
                <div style="margin-bottom:10px;"><label>Class Name</label><input type="text" name="className" required></div>
                <div style="margin-bottom:12px;"><label>Expected Students</label><input type="number" name="expectedStrength" min="1" required></div>
                <button type="submit">Create Class</button>
            </form>
        </div>
        <% } %>
        <% if ("overview".equals(activeView) || "classStudents".equals(activeView)) { %>
        <div class="panel">
            <h3>Create Student</h3>
            <form id="createStudentForm" method="post" action="<%=request.getContextPath()%>/hod">
                <input type="hidden" name="action" value="createStudent">
                <div style="margin-bottom:10px;"><label>Full Name</label><input type="text" name="fullName" required></div>
                <div style="margin-bottom:10px;"><label>Email</label><input type="email" name="email" autocapitalize="none" spellcheck="false" required></div>
                <div style="margin-bottom:12px;"><label>Class</label><select name="classId" required><% for (int i = 0; i < classes.size(); i++) { Map row = (Map) classes.get(i); %><option value="<%=row.get("id")%>"><%=row.get("className")%></option><% } %></select></div>
                <button type="submit">Create Student</button>
            </form>
        </div>
        <div class="panel">
            <h3>Bulk Add Students</h3>
            <form id="bulkStudentsForm" method="post" action="<%=request.getContextPath()%>/hod">
                <input type="hidden" name="action" value="createBulkStudents">
                <div style="margin-bottom:10px;">
                    <label>Class</label>
                    <select name="classId" required>
                        <% for (int i = 0; i < classes.size(); i++) { Map row = (Map) classes.get(i); %>
                        <option value="<%=row.get("id")%>"><%=row.get("className")%></option>
                        <% } %>
                    </select>
                </div>
                <div style="margin-bottom:10px;">
                    <label>Students List</label>
                    <textarea name="studentEntries" placeholder="Example&#10;Rahul Patel,rahul@gmail.com&#10;Neha Shah,neha@gmail.com" required></textarea>
                </div>
                <button type="submit">Add Multiple Students</button>
            </form>
        </div>
        <% } %>
        <% if ("overview".equals(activeView) || "staff".equals(activeView)) { %>
        <div class="panel">
            <h3>Create Staff</h3>
            <form id="createStaffForm" method="post" action="<%=request.getContextPath()%>/hod">
                <input type="hidden" name="action" value="createStaff">
                <div style="margin-bottom:10px;"><label>Full Name</label><input type="text" name="fullName" required></div>
                <div style="margin-bottom:10px;"><label>Email</label><input type="email" name="email" autocapitalize="none" spellcheck="false" required></div>
                <button type="submit">Create Staff</button>
            </form>
        </div>
        <% } %>
    </div>
    <% if ("overview".equals(activeView) || "classes".equals(activeView) || "staff".equals(activeView)) { %>
    <div class="grid two">
        <% if ("overview".equals(activeView) || "classes".equals(activeView)) { %>
        <div class="table-card">
            <h3>Classes</h3>
            <table>
                <tr><th>Class</th><th>Expected</th><th>Current</th><th>Edit</th><th>Delete</th></tr>
                <% for (int i = 0; i < classes.size(); i++) { Map row = (Map) classes.get(i); %>
                <tr>
                    <td><a class="class-link" href="<%=request.getContextPath()%>/hod?view=classStudents&classId=<%=row.get("id")%>"><%=row.get("className")%></a></td>
                    <td><%=row.get("expectedStrength")%></td>
                    <td><%=row.get("studentCount")%></td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/hod" class="inline-form">
                            <input type="hidden" name="action" value="updateClass">
                            <input type="hidden" name="classId" value="<%=row.get("id")%>">
                            <input type="text" name="className" value="<%=row.get("className")%>" required>
                            <input type="number" name="expectedStrength" value="<%=row.get("expectedStrength")%>" min="1" required>
                            <button type="submit">Edit</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/hod">
                            <input type="hidden" name="action" value="deleteClass">
                            <input type="hidden" name="classId" value="<%=row.get("id")%>">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
        <% } %>
        <% if ("overview".equals(activeView) || "staff".equals(activeView)) { %>
        <div class="table-card">
            <h3>Staff Members</h3>
            <table>
                <tr><th>Name</th><th>Email</th><th>Role</th><th>Edit</th><th>Delete</th></tr>
                <% for (int i = 0; i < staffMembers.size(); i++) { Map row = (Map) staffMembers.get(i); %>
                <tr>
                    <td><%=row.get("fullName")%></td>
                    <td><%=row.get("email")%></td>
                    <td><%=row.get("role")%></td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/hod" class="inline-form email-edit-form" data-user-id="<%=row.get("id")%>">
                            <input type="hidden" name="action" value="updateStaff">
                            <input type="hidden" name="staffId" value="<%=row.get("id")%>">
                            <input type="text" name="fullName" value="<%=row.get("fullName")%>" required>
                            <input type="email" name="email" value="<%=row.get("email")%>" autocapitalize="none" spellcheck="false" required>
                            <button type="submit">Edit</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="<%=request.getContextPath()%>/hod">
                            <input type="hidden" name="action" value="deleteStaff">
                            <input type="hidden" name="staffId" value="<%=row.get("id")%>">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
        <% } %>
    </div>
    <% } %>
    <% if ("classStudents".equals(activeView)) { %>
    <div class="table-card">
        <h3>Selected Class Students</h3>
        <form method="get" action="<%=request.getContextPath()%>/hod" style="margin-bottom:16px;">
            <input type="hidden" name="view" value="classStudents">
            <div class="grid two">
                <div>
                    <label>Select Class</label>
                    <select name="classId" onchange="this.form.submit()">
                        <option value="">Choose Class</option>
                        <% for (int i = 0; i < classes.size(); i++) { Map row = (Map) classes.get(i); %>
                        <option value="<%=row.get("id")%>" <%=String.valueOf(row.get("id")).equals(String.valueOf(selectedClassId)) ? "selected" : ""%>><%=row.get("className")%></option>
                        <% } %>
                    </select>
                </div>
            </div>
        </form>
        <% if (selectedClassId == null) { %>
        <p class="muted">Select any class from the dropdown to see that class student details.</p>
        <% } else { %>
        <table>
            <tr><th>Name</th><th>Email</th><th>Role</th><th>Class</th></tr>
            <% for (int i = 0; i < selectedClassStudents.size(); i++) { Map row = (Map) selectedClassStudents.get(i); %>
            <tr>
                <td><%=row.get("fullName")%></td>
                <td><%=row.get("email")%></td>
                <td><%=row.get("role")%></td>
                <td><%=row.get("className")%></td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </div>
    <% } %>
    <% if ("staff".equals(activeView)) { %>
    <div class="table-card">
        <h3>All Staff Details</h3>
        <table>
            <tr><th>Name</th><th>Email</th><th>Role</th></tr>
            <% for (int i = 0; i < staffMembers.size(); i++) { Map row = (Map) staffMembers.get(i); %>
            <tr><td><%=row.get("fullName")%></td><td><%=row.get("email")%></td><td><%=row.get("role")%></td></tr>
            <% } %>
        </table>
    </div>
    <% } %>
    <% if ("cr".equals(activeView)) { %>
    <div class="table-card">
        <h3>CR Students</h3>
        <table>
            <tr><th>Name</th><th>Email</th><th>Class</th><th>Action</th></tr>
            <% for (int i = 0; i < students.size(); i++) { Map row = (Map) students.get(i); if (!"CR".equals(String.valueOf(row.get("role")))) { continue; } %>
            <tr>
                <td><%=row.get("fullName")%></td>
                <td><%=row.get("email")%></td>
                <td><%=row.get("className")%></td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/hod">
                        <input type="hidden" name="action" value="removeCr">
                        <input type="hidden" name="studentId" value="<%=row.get("id")%>">
                        <button type="submit">Remove CR</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
    <% } %>
    <% if ("overview".equals(activeView) || "cr".equals(activeView)) { %>
    <div class="table-card">
        <h3>Students And CR Assignment</h3>
        <p class="muted">Student once assigned to one class cannot be moved to another class.</p>
        <table>
            <tr><th>Name</th><th>Email</th><th>Class</th><th>Role</th><th>Edit</th><th>Assign CR</th><th>Delete</th></tr>
            <% for (int i = 0; i < students.size(); i++) { Map row = (Map) students.get(i); %>
            <tr>
                <td><%=row.get("fullName")%></td>
                <td><%=row.get("email")%></td>
                <td><%=row.get("className")%></td>
                <td><%=row.get("role")%></td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/hod" class="inline-form email-edit-form" data-user-id="<%=row.get("id")%>">
                        <input type="hidden" name="action" value="updateStudent">
                        <input type="hidden" name="studentId" value="<%=row.get("id")%>">
                        <input type="hidden" name="classId" value="<%
                            for (int j = 0; j < classes.size(); j++) {
                                Map classRow = (Map) classes.get(j);
                                if (String.valueOf(classRow.get("className")).equals(String.valueOf(row.get("className")))) {
                                    out.print(classRow.get("id"));
                                    break;
                                }
                            }
                        %>">
                        <input type="text" name="fullName" value="<%=row.get("fullName")%>" required>
                        <input type="email" name="email" value="<%=row.get("email")%>" autocapitalize="none" spellcheck="false" required>
                        <input type="text" value="<%=row.get("className")%>" disabled>
                        <button type="submit">Edit</button>
                    </form>
                </td>
                <td>
                    <% if ("CR".equals(String.valueOf(row.get("role")))) { %>
                    <form method="post" action="<%=request.getContextPath()%>/hod"><input type="hidden" name="action" value="removeCr"><input type="hidden" name="studentId" value="<%=row.get("id")%>"><button type="submit">Remove CR</button></form>
                    <% } else { %>
                    <form method="post" action="<%=request.getContextPath()%>/hod"><input type="hidden" name="action" value="assignCr"><input type="hidden" name="studentId" value="<%=row.get("id")%>"><button type="submit">Assign As CR</button></form>
                    <% } %>
                </td>
                <td><form method="post" action="<%=request.getContextPath()%>/hod"><input type="hidden" name="action" value="deleteStudent"><input type="hidden" name="studentId" value="<%=row.get("id")%>"><button type="submit">Delete</button></form></td>
            </tr>
            <% } %>
        </table>
    </div>
    <% } %>
    <% } %>
    <div class="status-overview-card">
        <h3>Complaint Status Overview</h3>
        <div class="status-overview-grid">
            <div class="status-metric">
                <span>Pending Review</span>
                <strong class="status-number pending"><%=pendingReviewCount%></strong>
                <div class="status-track"><div class="status-fill pending" style="width:<%=statusTotal == 0 ? 0 : Math.max(8, (pendingReviewCount * 100) / statusTotal)%>%"></div></div>
            </div>
            <div class="status-metric">
                <span>In Progress</span>
                <strong class="status-number progress"><%=inProgressCount%></strong>
                <div class="status-track"><div class="status-fill progress" style="width:<%=statusTotal == 0 ? 0 : Math.max(8, (inProgressCount * 100) / statusTotal)%>%"></div></div>
            </div>
            <div class="status-metric">
                <span>Rejected</span>
                <strong class="status-number rejected"><%=rejectedCount%></strong>
                <div class="status-track"><div class="status-fill rejected" style="width:<%=statusTotal == 0 ? 0 : Math.max(8, (rejectedCount * 100) / statusTotal)%>%"></div></div>
            </div>
            <div class="status-metric">
                <span>Resolved</span>
                <strong class="status-number solved"><%=resolvedCount%></strong>
                <div class="status-track"><div class="status-fill solved" style="width:<%=statusTotal == 0 ? 0 : Math.max(8, (resolvedCount * 100) / statusTotal)%>%"></div></div>
            </div>
        </div>
    </div>
</div>
<script>
(function () {
    var existingEmails = [
        <% for (int i = 0; i < students.size(); i++) { Map row = (Map) students.get(i); %>
        { id: "<%=row.get("id")%>", email: "<%=String.valueOf(row.get("email")).replace("\\", "\\\\").replace("\"", "\\\"")%>".toLowerCase() }<%= (i < students.size() - 1 || staffMembers.size() > 0) ? "," : ""%>
        <% } %>
        <% for (int i = 0; i < staffMembers.size(); i++) { Map row = (Map) staffMembers.get(i); %>
        { id: "<%=row.get("id")%>", email: "<%=String.valueOf(row.get("email")).replace("\\", "\\\\").replace("\"", "\\\"")%>".toLowerCase() }<%=i < staffMembers.size() - 1 ? "," : ""%>
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

    function emailExists(emailValue, excludeUserId) {
        return existingEmails.some(function (item) {
            return item.email === emailValue && (!excludeUserId || item.id !== excludeUserId);
        });
    }

    function validatePersonForm(form, excludeUserId) {
        clearErrors(form);
        var valid = true;
        var nameField = form.querySelector('input[name="fullName"]');
        var emailField = form.querySelector('input[name="email"]');
        if (!nameField || !emailField) {
            return true;
        }
        var nameValue = normalize(nameField.value);
        var emailValue = normalize(emailField.value);
        emailField.value = emailValue;

        if (nameValue.length < 3) {
            showError(nameField, "Name must be at least 3 characters.");
            valid = false;
        }
        if (!isValidEmail(emailValue)) {
            showError(emailField, "Enter a valid email address.");
            valid = false;
        } else if (emailExists(emailValue, excludeUserId)) {
            showError(emailField, "This email already exists in the system.");
            valid = false;
        }
        return valid;
    }

    function validateBulkForm(form) {
        clearErrors(form);
        var textarea = form.querySelector('textarea[name="studentEntries"]');
        if (!textarea) return true;

        var raw = textarea.value || "";
        var lines = raw.split(/\r?\n/);
        var seen = {};
        for (var i = 0; i < lines.length; i++) {
            var line = (lines[i] || "").trim();
            if (!line) continue;
            var parts = line.split(",");
            if (parts.length !== 2) {
                showError(textarea, "Invalid bulk format at line " + (i + 1) + ". Use: Name,email");
                return false;
            }
            var fullName = (parts[0] || "").trim();
            var emailValue = normalize(parts[1]);
            if (fullName.length < 3) {
                showError(textarea, "Student name too short at line " + (i + 1) + ".");
                return false;
            }
            if (!isValidEmail(emailValue)) {
                showError(textarea, "Invalid email at line " + (i + 1) + ".");
                return false;
            }
            if (seen[emailValue]) {
                showError(textarea, "Duplicate email found in bulk list at line " + (i + 1) + ".");
                return false;
            }
            if (emailExists(emailValue, null)) {
                showError(textarea, "This email already exists in the system (line " + (i + 1) + ").");
                return false;
            }
            seen[emailValue] = true;
        }
        return true;
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

    bindForm("#createStudentForm", function (form) { return validatePersonForm(form, null); });
    bindForm("#createStaffForm", function (form) { return validatePersonForm(form, null); });
    bindForm("#bulkStudentsForm", validateBulkForm);
    bindForm(".email-edit-form", function (form) { return validatePersonForm(form, form.getAttribute("data-user-id")); });
})();

(function () {
    var button = document.getElementById("hodNotifBtn");
    var panel = document.getElementById("hodNotifPanel");
    var count = document.getElementById("hodNotifCount");
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
                fetch("<%=request.getContextPath()%>/hod?action=markNotificationsRead", { method: "GET", credentials: "same-origin" });
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


