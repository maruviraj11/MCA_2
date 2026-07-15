<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Map user = (Map) session.getAttribute("user");
    List complaints = (List) request.getAttribute("complaints");
    Integer notificationCount = (Integer) request.getAttribute("notificationCount");
    List recentNotifications = (List) request.getAttribute("recentNotifications");
    boolean isCr = "CR".equals(String.valueOf(user.get("role")));
    int totalComplaints = complaints == null ? 0 : complaints.size();
    int pendingComplaints = 0;
    int resolvedComplaints = 0;
    if (complaints != null) {
        for (int i = 0; i < complaints.size(); i++) {
            Map row = (Map) complaints.get(i);
            String status = String.valueOf(row.get("status"));
            if ("SOLVED".equals(status)) {
                resolvedComplaints++;
            } else if ("PENDING".equals(status) || "ASSIGNED".equals(status)) {
                pendingComplaints++;
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="page">
<div class="shell">
    <div class="topbar">
        <div><span class="badge"><%=user.get("role")%> PANEL</span><h2>Welcome, <%=user.get("fullName")%></h2></div>
        <div class="topbar-actions">
            <div class="notif-wrap">
                <button type="button" class="notif-button" id="userNotifBtn" aria-expanded="false" aria-controls="userNotifPanel">
                    <span>Notifications</span>
                    <span class="notif-count <%=notificationCount != null && notificationCount.intValue() > 0 ? "has-count" : ""%>" id="userNotifCount"><%=notificationCount%></span>
                </button>
                <div class="notif-panel hidden" id="userNotifPanel">
                    <div class="notif-panel-head">
                        <h3>Recent Notifications</h3>
                        <span class="muted">Latest 6 updates</span>
                    </div>
                    <% if (recentNotifications == null || recentNotifications.isEmpty()) { %>
                    <div class="notif-empty">No notifications available.</div>
                    <% } else { for (int i = 0; i < recentNotifications.size(); i++) { Map row = (Map) recentNotifications.get(i); %>
                    <div class="notif-item <%=Boolean.TRUE.equals(row.get("isRead")) ? "" : "unread"%>">
                        <strong><%=row.get("message")%></strong>
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
    <div class="dashboard-hero">
        <div class="dashboard-head">
            <div>
                <h1>My Dashboard</h1>
                <p class="dashboard-subtitle"><%=user.get("fullName")%> - <%=user.get("className") == null ? "-" : user.get("className")%>, <%=user.get("departmentName")%></p>
            </div>
            <a class="dashboard-action" href="#newComplaintCard">+ New Complaint</a>
        </div>
        <div class="summary-grid three">
            <div class="summary-card">
                <div class="summary-icon complaint">&#128221;</div>
                <strong><%=totalComplaints%></strong>
                <span>My Complaints</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon pending">&#9203;</div>
                <strong><%=pendingComplaints%></strong>
                <span>Pending</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon solved">&#9989;</div>
                <strong><%=resolvedComplaints%></strong>
                <span>Resolved</span>
            </div>
        </div>
    </div>
    <div class="hero legacy-hero">
        <h1>Complaint Submission</h1>
        <p class="muted">Your complaint is sent directly to the department clerk, and every status update appears in your history.</p>
        <div class="stats">
            <div class="stat"><span>Department</span><strong><%=user.get("departmentName")%></strong></div>
            <div class="stat"><span>Role</span><strong><%=user.get("role")%></strong></div>
            <div class="stat"><span>Class</span><strong><%=user.get("className") == null ? "-" : user.get("className")%></strong></div>
        </div>
    </div>
    <div class="grid two">
        <div class="panel" id="newComplaintCard">
            <h3>New Complaint</h3>
            <form method="post" action="<%=request.getContextPath()%>/user">
                <input type="hidden" name="action" value="createComplaint">
                <div style="margin-bottom:10px;"><label>Complaint Scope</label><select name="scope"><option value="SELF">Self Complaint</option><% if (isCr) { %><option value="CLASS">Complaint For Class</option><% } %></select></div>
                <div style="margin-bottom:10px;"><label>Title</label><input type="text" name="title" required></div>
                <div style="margin-bottom:12px;"><label>Description</label><textarea name="description" required></textarea></div>
                <button type="submit">Submit Complaint</button>
            </form>
        </div>
        <div class="panel">
            <h3>Rules</h3>
            <p class="muted">A complaint can be edited for up to 30 minutes after submission.</p>
            <p class="muted">Class complaint option is available only for CR users.</p>
            <p class="muted">Clerk statuses: REJECTED, ASSIGNED, SOLVED, PENDING.</p>
            <p class="muted">If clerk performs ASSIGNED, SOLVED, or PENDING, update option is removed. Only REJECTED complaints stay editable within 30 minutes.</p>
        </div>
    </div>
    <div class="table-card">
        <h3>Complaint History</h3>
        <table>
            <tr><th>Title</th><th>Scope</th><th>Status</th><th>Remarks</th><th>Edit</th></tr>
            <% for (int i = 0; i < complaints.size(); i++) { Map row = (Map) complaints.get(i); %>
            <tr>
                <td><strong><%=row.get("title")%></strong><br><span class="muted"><%=row.get("description")%></span><br><span class="muted">Created: <%=row.get("createdAt")%></span></td>
                <td><%=row.get("complaintScope")%></td>
                <td><span class="badge warn"><%=row.get("status")%></span></td>
                <td><%=row.get("clerkRemarks")%></td>
                <td>
                    <% if (Boolean.TRUE.equals(row.get("canEdit"))) { %>
                    <form method="post" action="<%=request.getContextPath()%>/user">
                        <input type="hidden" name="action" value="editComplaint">
                        <input type="hidden" name="complaintId" value="<%=row.get("id")%>">
                        <div style="margin-bottom:8px;"><input type="text" name="title" value="<%=row.get("title")%>" required></div>
                        <div style="margin-bottom:8px;"><textarea name="description" required><%=row.get("description")%></textarea></div>
                        <button type="submit">Update</button>
                    </form>
                    <% } else { %><span class="badge danger">Locked</span><div class="muted" style="margin-top:6px;">Locked after clerk action. Rejected complaints only stay editable.</div><% } %>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
<script>
(function () {
    var button = document.getElementById("userNotifBtn");
    var panel = document.getElementById("userNotifPanel");
    var count = document.getElementById("userNotifCount");
    var markReadUrl = "<%=request.getContextPath()%>/user?action=markNotificationsRead";
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
                fetch(markReadUrl, { method: "GET", credentials: "same-origin" });
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



