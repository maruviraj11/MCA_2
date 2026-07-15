package com.cmp.servlet;

import com.cmp.dao.AuthDao;
import com.cmp.dao.UserDao;
import com.cmp.util.SessionUtil;
import com.cmp.util.ViewUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 5242880L, maxRequestSize = 6291456L)
public class UserServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final AuthDao authDao = new AuthDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        if ("markNotificationsRead".equals(action)) {
            Map currentUser = SessionUtil.getUser(request);
            int currentUserId = Integer.parseInt(String.valueOf(currentUser.get("id")));
            userDao.markUserNotificationsRead(currentUserId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        if ("downloadAttachment".equals(action) || "viewAttachment".equals(action)) {
            streamAttachment(request, response, "downloadAttachment".equals(action));
            return;
        }
        if ("changePasswordPage".equals(action)) {
            ViewUtil.forward("change-password.jsp", request, response);
            return;
        }
        Map user = SessionUtil.getUser(request);
        if ("ADMIN".equals(user.get("role"))) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        if ("HOD".equals(user.get("role"))) {
            response.sendRedirect(request.getContextPath() + "/hod");
            return;
        }
        if ("CLERK".equals(user.get("role"))) {
            response.sendRedirect(request.getContextPath() + "/clerk");
            return;
        }
        int userId = Integer.parseInt(String.valueOf(user.get("id")));
        request.setAttribute("complaints", userDao.getComplaintHistory(userId));
        request.setAttribute("notificationCount", Integer.valueOf(userDao.getUserNotificationCount(userId)));
        try {
            request.setAttribute("recentNotifications", userDao.getRecentNotifications(userId));
        } catch (NoSuchMethodError ex) {
            request.setAttribute("recentNotifications", Collections.emptyList());
        }
        ViewUtil.forward("user.jsp", request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Map user = SessionUtil.getUser(request);
        try {
            String action = request.getParameter("action");
            if ("changePassword".equals(action)) {
                authDao.changePassword(Integer.parseInt(String.valueOf(user.get("id"))), request.getParameter("newPassword"));
                user.put("mustChangePassword", Boolean.FALSE);
                response.sendRedirect(request.getContextPath() + routeForRole(String.valueOf(user.get("role"))));
                return;
            } else if ("createComplaint".equals(action)) {
                Integer classId = null;
                if ("CLASS".equals(request.getParameter("scope")) && user.get("classId") != null) {
                    classId = Integer.valueOf(String.valueOf(user.get("classId")));
                }
                Map attachment = saveAttachment(request);
                userDao.createComplaint(Integer.parseInt(String.valueOf(user.get("id"))),
                        classId, request.getParameter("scope"), request.getParameter("title"), request.getParameter("description"),
                        attachment.get("name") == null ? null : String.valueOf(attachment.get("name")),
                        attachment.get("path") == null ? null : String.valueOf(attachment.get("path")),
                        attachment.get("type") == null ? null : String.valueOf(attachment.get("type")));
                request.setAttribute("success", "Complaint submitted successfully.");
            } else if ("editComplaint".equals(action)) {
                userDao.updateComplaint(Integer.parseInt(request.getParameter("complaintId")),
                        Integer.parseInt(String.valueOf(user.get("id"))),
                        request.getParameter("title"), request.getParameter("description"));
                request.setAttribute("success", "Complaint updated successfully.");
            } else if ("clearHistory".equals(action)) {
                try {
                    userDao.clearComplaintHistory(Integer.parseInt(String.valueOf(user.get("id"))));
                    request.setAttribute("success", "Complaint history cleared successfully.");
                } catch (NoSuchMethodError ex) {
                    request.setAttribute("error", "Clear history is not available in the deployed build yet. Please clean, rebuild, and redeploy the project.");
                }
            }
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }
        int userId = Integer.parseInt(String.valueOf(user.get("id")));
        request.setAttribute("complaints", userDao.getComplaintHistory(userId));
        request.setAttribute("notificationCount", Integer.valueOf(userDao.getUserNotificationCount(userId)));
        try {
            request.setAttribute("recentNotifications", userDao.getRecentNotifications(userId));
        } catch (NoSuchMethodError ex) {
            request.setAttribute("recentNotifications", Collections.emptyList());
        }
        ViewUtil.forward("user.jsp", request, response);
    }

    private String routeForRole(String role) {
        if ("ADMIN".equals(role)) {
            return "/admin";
        }
        if ("HOD".equals(role)) {
            return "/hod";
        }
        if ("CLERK".equals(role)) {
            return "/clerk";
        }
        return "/user";
    }

    private Map saveAttachment(HttpServletRequest request) throws Exception {
        java.util.HashMap attachment = new java.util.HashMap();
        attachment.put("name", null);
        attachment.put("path", null);
        attachment.put("type", null);
        Part part = null;
        try {
            part = request.getPart("attachment");
        } catch (Exception ex) {
            return attachment;
        }
        if (part == null || part.getSize() <= 0) {
            return attachment;
        }
        String submittedFileName = extractFileName(part);
        if (submittedFileName == null || submittedFileName.trim().length() == 0) {
            return attachment;
        }
        String uploadsPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadsPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String cleanName = submittedFileName.replace("\\", "_").replace("/", "_").replace(" ", "_");
        String storedName = System.currentTimeMillis() + "_" + cleanName;
        File target = new File(uploadDir, storedName);
        part.write(target.getAbsolutePath());
        attachment.put("name", submittedFileName);
        attachment.put("path", storedName);
        attachment.put("type", part.getContentType());
        return attachment;
    }

    private String extractFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) {
            return null;
        }
        String[] pieces = header.split(";");
        for (int i = 0; i < pieces.length; i++) {
            String piece = pieces[i].trim();
            if (piece.startsWith("filename=")) {
                return piece.substring(9).replace("\"", "");
            }
        }
        return null;
    }

    private void streamAttachment(HttpServletRequest request, HttpServletResponse response, boolean forceDownload) throws IOException {
        String attachmentPath = request.getParameter("file");
        String attachmentName = request.getParameter("name");
        if (attachmentPath == null || attachmentPath.trim().length() == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File file = new File(getServletContext().getRealPath("/uploads"), attachmentPath);
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(getServletContext().getMimeType(file.getName()) == null
                ? "application/octet-stream" : getServletContext().getMimeType(file.getName()));
        if (forceDownload) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + (attachmentName == null ? file.getName() : attachmentName) + "\"");
        } else {
            response.setHeader("Content-Disposition", "inline; filename=\"" + (attachmentName == null ? file.getName() : attachmentName) + "\"");
        }
        FileInputStream input = new FileInputStream(file);
        OutputStream output = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        input.close();
        output.flush();
    }
}
