package com.library.controller;

import com.library.dao.UserDAO;
import com.library.model.User;
import com.library.util.Validation;
import com.library.util.WebUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = session == null ? null : (User) session.getAttribute("user");
        if (sessionUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (!"changePassword".equals(action)) {
            response.sendRedirect("change_password.jsp");
            return;
        }

        String currentPass = request.getParameter("currentPass");
        String newPass = request.getParameter("newPass");
        String confirmPass = request.getParameter("confirmPass");

        if (Validation.isBlank(currentPass) || Validation.isBlank(newPass) || Validation.isBlank(confirmPass)) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("Missing required fields."));
            return;
        }

        newPass = newPass.trim();
        confirmPass = confirmPass.trim();

        if (!Validation.isStrongPassword(newPass, 8)) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc(Validation.passwordRuleMessage(8)));
            return;
        }

        if (!newPass.equals(confirmPass)) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("New password and confirm password do not match."));
            return;
        }

        if (newPass.equals(currentPass)) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("New password must be different from old password."));
            return;
        }

        UserDAO userDAO = new UserDAO();
        User dbUser = userDAO.getUserById(sessionUser.getId());
        if (dbUser == null) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("User not found. Please login again."));
            return;
        }

        if (!currentPass.equals(dbUser.getPassword())) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("Current password is incorrect."));
            return;
        }

        boolean updated = userDAO.updatePassword(sessionUser.getId(), newPass);
        if (!updated) {
            response.sendRedirect("change_password.jsp?error=" + WebUtil.enc("Failed to update password. Try again."));
            return;
        }

        sessionUser.setPassword(newPass);
        session.setAttribute("user", sessionUser);
        response.sendRedirect("change_password.jsp?msg=" + WebUtil.enc("Password updated successfully."));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("change_password.jsp");
    }


}
