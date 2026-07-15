package com.cmp.servlet;

import com.cmp.dao.AdminDao;
import com.cmp.util.SessionUtil;
import com.cmp.util.ValidationUtil;
import com.cmp.util.ViewUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

    private final AdminDao adminDao = new AdminDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if ("markNotificationsRead".equals(request.getParameter("action"))) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        loadDashboard(request);
        ViewUtil.forward("admin.jsp", request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            String action = request.getParameter("action");
            if ("createDepartment".equals(action)) {
                adminDao.saveDepartment(request.getParameter("name"), request.getParameter("description"),
                        request.getParameter("hodUserId"), request.getParameter("clerkUserId"));
                request.setAttribute("success", "Department created successfully.");
            } else if ("updateDepartment".equals(action)) {
                adminDao.updateDepartment(Integer.parseInt(request.getParameter("departmentId")),
                        request.getParameter("name"), request.getParameter("description"),
                        request.getParameter("hodUserId"), request.getParameter("clerkUserId"));
                request.setAttribute("success", "Department updated successfully.");
            } else if ("createRoleUser".equals(action)) {
                String email = ValidationUtil.normalizeEmailOrThrow(request.getParameter("email"));
                String role = request.getParameter("role");
                String plainPassword = adminDao.saveRoleUser(request.getParameter("fullName"),
                        email, role, request.getParameter("departmentId"));
                request.setAttribute("success", role + " created successfully.");
                request.setAttribute("credentialEmail", email);
                request.setAttribute("credentialPassword", plainPassword);
                request.setAttribute("credentialRole", role);
            } else if ("assignDepartment".equals(action)) {
                adminDao.assignDepartment(Integer.parseInt(request.getParameter("departmentId")),
                        request.getParameter("hodUserId"), request.getParameter("clerkUserId"));
                request.setAttribute("success", "Department assignment updated.");
            } else if ("deleteDepartment".equals(action)) {
                adminDao.deleteDepartment(Integer.parseInt(request.getParameter("departmentId")));
                request.setAttribute("success", "Department deleted successfully.");
            } else if ("deleteRoleUser".equals(action)) {
                adminDao.deleteRoleUser(Integer.parseInt(request.getParameter("userId")), request.getParameter("role"));
                request.setAttribute("success", request.getParameter("role") + " deleted successfully.");
            } else if ("updateRoleUser".equals(action)) {
                adminDao.updateRoleUser(Integer.parseInt(request.getParameter("userId")),
                        request.getParameter("role"),
                        request.getParameter("fullName"),
                        ValidationUtil.normalizeEmailOrThrow(request.getParameter("email")));
                request.setAttribute("success", request.getParameter("role") + " updated successfully.");
            } else if ("clearDepartments".equals(action)) {
                adminDao.clearAllDepartments();
                request.setAttribute("success", "All departments cleared successfully.");
            } else if ("clearHods".equals(action)) {
                adminDao.clearRoleUsers("HOD");
                request.setAttribute("success", "All HOD records cleared successfully.");
            } else if ("clearClerks".equals(action)) {
                adminDao.clearRoleUsers("CLERK");
                request.setAttribute("success", "All clerk records cleared successfully.");
            }
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }
        loadDashboard(request);
        ViewUtil.forward("admin.jsp", request, response);
    }

    private void loadDashboard(HttpServletRequest request) {
        request.setAttribute("departments", adminDao.getDepartments());
        request.setAttribute("hods", adminDao.getUsersByRole("HOD"));
        request.setAttribute("clerks", adminDao.getUsersByRole("CLERK"));
        request.setAttribute("totalComplaintCount", Integer.valueOf(adminDao.getTotalComplaintCount()));
        request.setAttribute("pendingComplaintCount", Integer.valueOf(adminDao.getPendingComplaintCount()));
        List recentActivities = Collections.emptyList();
        try {
            recentActivities = adminDao.getRecentAdminActivities();
        } catch (NoSuchMethodError ex) {
            recentActivities = Collections.emptyList();
        }
        request.setAttribute("recentActivities", recentActivities);
        request.setAttribute("notificationCount", Integer.valueOf(recentActivities.size()));
    }
}
