package com.cmp.servlet;

import com.cmp.dao.ClerkDao;
import com.cmp.util.SessionUtil;
import com.cmp.util.ViewUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClerkServlet extends HttpServlet {

    private final ClerkDao clerkDao = new ClerkDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "CLERK")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if ("markNotificationsRead".equals(request.getParameter("action"))) {
            Map currentUser = SessionUtil.getUser(request);
            int currentUserId = Integer.parseInt(String.valueOf(currentUser.get("id")));
            clerkDao.markClerkSeen(currentUserId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        loadDashboard(request);
        ViewUtil.forward("clerk.jsp", request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "CLERK")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            String action = request.getParameter("action");
            Map user = SessionUtil.getUser(request);
            int clerkId = Integer.parseInt(String.valueOf(user.get("id")));
            if ("clearActive".equals(action)) {
                clerkDao.clearActiveComplaints(clerkId);
                request.setAttribute("success", "All active complaints cleared.");
            } else if ("clearHistory".equals(action)) {
                clerkDao.clearResolvedHistory(clerkId);
                request.setAttribute("success", "Resolved history cleared.");
            } else {
                clerkDao.updateStatus(Integer.parseInt(request.getParameter("complaintId")),
                        request.getParameter("status"), request.getParameter("remarks"));
                request.setAttribute("success", "Complaint status updated.");
            }
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }
        loadDashboard(request);
        ViewUtil.forward("clerk.jsp", request, response);
    }

    private void loadDashboard(HttpServletRequest request) {
        Map user = SessionUtil.getUser(request);
        int clerkId = Integer.parseInt(String.valueOf(user.get("id")));
        request.setAttribute("complaints", clerkDao.getComplaints(clerkId));
        request.setAttribute("resolvedComplaints", clerkDao.getResolvedComplaints(clerkId));
        request.setAttribute("notificationCount", Integer.valueOf(clerkDao.getUnreadNotificationCount(clerkId)));
        request.setAttribute("recentAlerts", clerkDao.getRecentComplaintAlerts(clerkId));
    }
}
