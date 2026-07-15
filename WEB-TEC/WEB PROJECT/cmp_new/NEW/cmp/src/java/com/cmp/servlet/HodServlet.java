package com.cmp.servlet;

import com.cmp.dao.HodDao;
import com.cmp.util.SessionUtil;
import com.cmp.util.ValidationUtil;
import com.cmp.util.ViewUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HodServlet extends HttpServlet {

    private final HodDao hodDao = new HodDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "HOD")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if ("markNotificationsRead".equals(request.getParameter("action"))) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        Integer departmentId = getDepartmentId(request);
        if (departmentId == null) {
            prepareEmptyDashboard(request);
            request.setAttribute("error", "Your HOD account is not assigned to any department yet. Ask admin to assign a department first.");
            ViewUtil.forward("hod.jsp", request, response);
            return;
        }
        loadDashboard(request);
        ViewUtil.forward("hod.jsp", request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "HOD")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Integer departmentId = getDepartmentId(request);
        if (departmentId == null) {
            prepareEmptyDashboard(request);
            request.setAttribute("error", "Department not assigned. Admin must assign your HOD account before using this panel.");
            ViewUtil.forward("hod.jsp", request, response);
            return;
        }
        try {
            String action = request.getParameter("action");
            if ("createClass".equals(action)) {
                hodDao.saveClass(departmentId.intValue(), request.getParameter("className"),
                        Integer.parseInt(request.getParameter("expectedStrength")));
                request.setAttribute("success", "Class created successfully.");
            } else if ("updateClass".equals(action)) {
                hodDao.updateClass(Integer.parseInt(request.getParameter("classId")), departmentId.intValue(),
                        request.getParameter("className"), Integer.parseInt(request.getParameter("expectedStrength")));
                request.setAttribute("success", "Class updated successfully.");
            } else if ("deleteClass".equals(action)) {
                hodDao.deleteClass(Integer.parseInt(request.getParameter("classId")), departmentId.intValue());
                request.setAttribute("success", "Class deleted successfully.");
            } else if ("createStudent".equals(action)) {
                String email = ValidationUtil.normalizeEmailOrThrow(request.getParameter("email"));
                String password = hodDao.saveStudent(request.getParameter("fullName"), email,
                        departmentId.intValue(), Integer.parseInt(request.getParameter("classId")));
                request.setAttribute("success", "Student created successfully.");
                request.setAttribute("credentialEmail", email);
                request.setAttribute("credentialPassword", password);
                request.setAttribute("credentialRole", "STUDENT");
            } else if ("createBulkStudents".equals(action)) {
                List studentRows = parseBulkStudents(request.getParameter("studentEntries"));
                List createdUsers = hodDao.saveStudentsBulk(studentRows, departmentId.intValue(),
                        Integer.parseInt(request.getParameter("classId")));
                request.setAttribute("bulkCreatedUsers", createdUsers);
                request.setAttribute("success", createdUsers.size() + " students created successfully.");
            } else if ("createStaff".equals(action)) {
                String email = ValidationUtil.normalizeEmailOrThrow(request.getParameter("email"));
                String password = hodDao.saveStaff(request.getParameter("fullName"), email, departmentId.intValue());
                request.setAttribute("success", "Staff created successfully.");
                request.setAttribute("credentialEmail", email);
                request.setAttribute("credentialPassword", password);
                request.setAttribute("credentialRole", "STAFF");
            } else if ("updateStudent".equals(action)) {
                hodDao.updateStudent(Integer.parseInt(request.getParameter("studentId")), departmentId.intValue(),
                        request.getParameter("fullName"), ValidationUtil.normalizeEmailOrThrow(request.getParameter("email")),
                        Integer.parseInt(request.getParameter("classId")));
                request.setAttribute("success", "Student updated successfully.");
            } else if ("deleteStudent".equals(action)) {
                hodDao.deleteDepartmentUser(Integer.parseInt(request.getParameter("studentId")), departmentId.intValue(), "STUDENT_GROUP");
                request.setAttribute("success", "Student deleted successfully.");
            } else if ("updateStaff".equals(action)) {
                hodDao.updateStaff(Integer.parseInt(request.getParameter("staffId")), departmentId.intValue(),
                        request.getParameter("fullName"), ValidationUtil.normalizeEmailOrThrow(request.getParameter("email")));
                request.setAttribute("success", "Staff updated successfully.");
            } else if ("deleteStaff".equals(action)) {
                hodDao.deleteDepartmentUser(Integer.parseInt(request.getParameter("staffId")), departmentId.intValue(), "STAFF");
                request.setAttribute("success", "Staff deleted successfully.");
            } else if ("assignCr".equals(action)) {
                hodDao.assignCr(Integer.parseInt(request.getParameter("studentId")));
                request.setAttribute("success", "CR assigned successfully.");
            } else if ("removeCr".equals(action)) {
                hodDao.removeCr(Integer.parseInt(request.getParameter("studentId")));
                request.setAttribute("success", "CR removed successfully.");
            } else if ("clearClasses".equals(action)) {
                hodDao.clearAllClasses(departmentId.intValue());
                request.setAttribute("success", "All classes cleared successfully.");
            } else if ("clearStudents".equals(action)) {
                hodDao.clearAllStudents(departmentId.intValue());
                request.setAttribute("success", "All students and CR records cleared successfully.");
            } else if ("clearStaff".equals(action)) {
                hodDao.clearAllStaff(departmentId.intValue());
                request.setAttribute("success", "All staff members cleared successfully.");
            }
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }
        loadDashboard(request);
        ViewUtil.forward("hod.jsp", request, response);
    }

    private void loadDashboard(HttpServletRequest request) {
        Integer departmentId = getDepartmentId(request);
        if (departmentId == null) {
            prepareEmptyDashboard(request);
            return;
        }
        List classes = hodDao.getClasses(departmentId);
        request.setAttribute("classes", classes);
        request.setAttribute("students", hodDao.getStudents(departmentId));
        request.setAttribute("staffMembers", hodDao.getStaff(departmentId));
        request.setAttribute("activeComplaintCount", Integer.valueOf(hodDao.getActiveComplaintCount(departmentId.intValue())));
        request.setAttribute("complaintStatusCounts", hodDao.getComplaintStatusCounts(departmentId.intValue()));
        List recentActivities = Collections.emptyList();
        try {
            recentActivities = hodDao.getRecentHodActivities(departmentId.intValue());
        } catch (NoSuchMethodError ex) {
            recentActivities = Collections.emptyList();
        }
        request.setAttribute("recentActivities", recentActivities);
        request.setAttribute("notificationCount", Integer.valueOf(recentActivities.size()));
        String view = request.getParameter("view");
        if (view == null || view.trim().length() == 0) {
            view = "overview";
        }
        request.setAttribute("activeView", view);
        String classIdParam = request.getParameter("classId");
        if (classIdParam != null && classIdParam.trim().length() > 0) {
            int classId = Integer.parseInt(classIdParam);
            request.setAttribute("selectedClassId", Integer.valueOf(classId));
            request.setAttribute("selectedClassStudents", hodDao.getStudentsByClass(departmentId.intValue(), classId));
        } else {
            request.setAttribute("selectedClassStudents", Collections.emptyList());
        }
    }

    private Integer getDepartmentId(HttpServletRequest request) {
        Map user = SessionUtil.getUser(request);
        Object value = user == null ? null : user.get("departmentId");
        if (value == null || "null".equals(String.valueOf(value))) {
            return null;
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private void prepareEmptyDashboard(HttpServletRequest request) {
        request.setAttribute("classes", Collections.emptyList());
        request.setAttribute("students", Collections.emptyList());
        request.setAttribute("staffMembers", Collections.emptyList());
        request.setAttribute("activeComplaintCount", Integer.valueOf(0));
        request.setAttribute("complaintStatusCounts", Collections.emptyMap());
        request.setAttribute("recentActivities", Collections.emptyList());
        request.setAttribute("notificationCount", Integer.valueOf(0));
        request.setAttribute("selectedClassStudents", Collections.emptyList());
        request.setAttribute("activeView", "overview");
    }

    private List parseBulkStudents(String studentEntries) {
        List rows = new ArrayList();
        Set seenEmails = new HashSet();
        if (studentEntries == null || studentEntries.trim().length() == 0) {
            throw new RuntimeException("Please enter at least one student row.");
        }
        String[] lines = studentEntries.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i] == null ? "" : lines[i].trim();
            if (line.length() == 0) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid bulk format at line " + (i + 1) + ". Use: Name,email");
            }
            String fullName = parts[0].trim();
            String email;
            try {
                email = ValidationUtil.normalizeEmailOrThrow(parts[1]);
            } catch (RuntimeException ex) {
                throw new RuntimeException("Invalid email at line " + (i + 1) + ".");
            }
            if (fullName.length() < 3) {
                throw new RuntimeException("Student name too short at line " + (i + 1) + ".");
            }
            if (seenEmails.contains(email)) {
                throw new RuntimeException("Duplicate email found in bulk list at line " + (i + 1) + ".");
            }
            seenEmails.add(email);
            Map row = new HashMap();
            row.put("fullName", fullName);
            row.put("email", email);
            rows.add(row);
        }
        if (rows.isEmpty()) {
            throw new RuntimeException("Please enter at least one valid student row.");
        }
        return rows;
    }
}
