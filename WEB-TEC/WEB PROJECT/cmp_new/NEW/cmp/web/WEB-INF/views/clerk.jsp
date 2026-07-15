<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Map user = (Map) session.getAttribute("user");
    List complaints = (List) request.getAttribute("complaints");
    List resolvedComplaints = (List) request.getAttribute("resolvedComplaints");
    Integer notificationCount = (Integer) request.getAttribute("notificationCount");
    List recentAlerts = (List) request.getAttribute("recentAlerts");
    int activeComplaints = complaints == null ? 0 : complaints.size();
    int pendingReviewCount = 0;
    int inProgressCount = 0;
    int rejectedCount = 0;
    int resolvedCount = resolvedComplaints == null ? 0 : resolvedComplaints.size();
    if (complaints != null) {
        for (int i = 0; i < complaints.size(); i++) {
            Map row = (Map) complaints.get(i);
            String status = String.valueOf(row.get("status"));
            if ("PENDING".equals(status)) {
                pendingReviewCount++;
            } else if ("ASSIGNED".equals(status)) {
                inProgressCount++;
            } else if ("REJECTED".equals(status)) {
                rejectedCount++;
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Clerk Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="page">
<div class="shell">
    <div class="topbar">
        <div><span class="badge">CLERK PANEL</span><h2><%=user.get("departmentName")%> Complaint Desk</h2></div>
        <div class="topbar-actions">
            <div class="notif-wrap">
                <button type="button" class="notif-button" id="clerkNotifBtn" aria-expanded="false" aria-controls="clerkNotifPanel">
                    <span>Notifications</span>
                    <span class="notif-count <%=notificationCount != null && notificationCount.intValue() > 0 ? "has-count" : ""%>" id="clerkNotifCount"><%=notificationCount%></span>
                </button>
                <div class="notif-panel hidden" id="clerkNotifPanel">
                    <div class="notif-panel-head">
                        <h3>Complaint Alerts</h3>
                        <span class="muted">Latest 6 complaints</span>
                    </div>
                    <% if (recentAlerts == null || recentAlerts.isEmpty()) { %>
                    <div class="notif-empty">No complaint alerts available.</div>
                    <% } else { for (int i = 0; i < recentAlerts.size(); i++) { Map row = (Map) recentAlerts.get(i); %>
                    <div class="notif-item unread">
                        <strong><%=row.get("title")%></strong>
                        <div class="muted">Status: <%=row.get("status")%></div>
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
    <div class="table-card">
        <h3>Clerk Department</h3>
        <p class="muted" style="margin:0;">You are logged in as clerk of <strong><%=user.get("departmentName") == null ? "Department not assigned" : user.get("departmentName")%></strong>.</p>
    </div>
    <div class="dashboard-hero">
        <div class="dashboard-head">
            <div>
                <h1>Clerk Dashboard</h1>
                <p class="dashboard-subtitle"><%=user.get("departmentName") == null ? "Department not assigned" : user.get("departmentName")%> Complaint Desk</p>
            </div>
        </div>
        <div class="summary-grid four">
            <div class="summary-card">
                <div class="summary-icon complaint">&#128221;</div>
                <strong><%=activeComplaints%></strong>
                <span>Active Complaints</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon pending">&#9203;</div>
                <strong><%=pendingReviewCount%></strong>
                <span>Pending Review</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon staff">&#128295;</div>
                <strong><%=inProgressCount%></strong>
                <span>In Progress</span>
            </div>
            <div class="summary-card">
                <div class="summary-icon solved">&#9989;</div>
                <strong><%=resolvedCount%></strong>
                <span>Resolved</span>
            </div>
        </div>
    </div>
    <div class="hero legacy-hero">
        <h1>Complaint Validation And Resolution</h1>
        <p class="muted">Department complaints from students, CR, and staff appear here for review, action, and resolution.</p>
    </div>
    <div class="table-card">
        <div class="section-head">
            <h3>Active Complaints</h3>
            <form method="post" action="<%=request.getContextPath()%>/clerk" class="inline-action-form">
                <input type="hidden" name="action" value="clearActive">
                <button type="submit" class="danger-button compact-button">Clear All</button>
            </form>
        </div>
        <table>
            <tr><th>Complaint</th><th>Raised By</th><th>Scope</th><th>Status</th><th>Update</th></tr>
            <% for (int i = 0; i < complaints.size(); i++) { Map row = (Map) complaints.get(i); %>
            <tr>
                <td><strong><%=row.get("title")%></strong><br><span class="muted"><%=row.get("description")%></span><br><span class="muted">Created: <%=row.get("createdAt")%></span></td>
                <td><%=row.get("raisedBy")%><br><span class="badge"><%=row.get("raisedRole")%></span><% if ("CLASS".equals(String.valueOf(row.get("complaintScope")))) { %><div class="muted" style="margin-top:8px;">Class: <%=row.get("className")%></div><% } %></td>
                <td><%=row.get("complaintScope")%></td>
                <td><span class="badge warn"><%=row.get("status")%></span></td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/clerk">
                        <input type="hidden" name="complaintId" value="<%=row.get("id")%>">
                        <div style="margin-bottom:8px;"><select name="status"><option value="REJECTED">Reject</option><option value="ASSIGNED">Assign</option><option value="SOLVED">Solved</option><option value="PENDING">Pending</option></select></div>
                        <div style="margin-bottom:8px;"><textarea name="remarks" placeholder="Remarks"></textarea></div>
                        <button type="submit">Save Status</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
    <div class="table-card">
        <div class="section-head">
            <h3>Resolved History</h3>
            <form method="post" action="<%=request.getContextPath()%>/clerk" class="inline-action-form">
                <input type="hidden" name="action" value="clearHistory">
                <button type="submit" class="danger-button compact-button">Clear All</button>
            </form>
        </div>
        <table>
            <tr><th>Complaint</th><th>Raised By</th><th>Scope</th><th>Status</th></tr>
            <% if (resolvedComplaints == null || resolvedComplaints.isEmpty()) { %>
            <tr><td colspan="4" class="muted">No resolved complaint history available.</td></tr>
            <% } else { for (int i = 0; i < resolvedComplaints.size(); i++) { Map row = (Map) resolvedComplaints.get(i); %>
            <tr>
                <td><strong><%=row.get("title")%></strong><br><span class="muted"><%=row.get("description")%></span><br><span class="muted">Created: <%=row.get("createdAt")%></span></td>
                <td><%=row.get("raisedBy")%><br><span class="badge"><%=row.get("raisedRole")%></span><% if ("CLASS".equals(String.valueOf(row.get("complaintScope")))) { %><div class="muted" style="margin-top:8px;">Class: <%=row.get("className")%></div><% } %></td>
                <td><%=row.get("complaintScope")%></td>
                <td><span class="badge"><%=row.get("status")%></span></td>
            </tr>
            <% }} %>
        </table>
    </div>
</div>
<script>
(function () {
    var button = document.getElementById("clerkNotifBtn");
    var panel = document.getElementById("clerkNotifPanel");
    var count = document.getElementById("clerkNotifCount");
    var markReadUrl = "<%=request.getContextPath()%>/clerk?action=markNotificationsRead";
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


