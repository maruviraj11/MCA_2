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

public class AdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("addLibrarian".equals(action)) {
            String email = Validation.normalizeEmail(request.getParameter("email"));
            String pass = request.getParameter("pass");

            if (!Validation.isValidEmail(email) || !Validation.isStrongPassword(pass, 8)) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Invalid email or weak password."));
                return;
            }
            
            User librarian = new User();
            librarian.setEmail(email);
            librarian.setPassword(pass);
            librarian.setRole("librarian");
            
            UserDAO dao = new UserDAO();
            if (dao.checkEmailExists(email)) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Email already exists"));
                return;
            }
            boolean success = dao.addUser(librarian);
            
            if (success) {
                response.sendRedirect("admin_dashboard.jsp?msg=" + WebUtil.enc("Librarian Added Successfully"));
            } else {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Failed to add Librarian"));
            }
        } else if ("removeLibrarian".equals(action)) {
            Integer id = Validation.parsePositiveInt(request.getParameter("librarianId"));
            if (id == null) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Invalid Librarian ID"));
                return;
            }
            UserDAO dao = new UserDAO();
            boolean success = dao.deleteLibrarian(id.intValue());
            
            if (success) {
                response.sendRedirect("admin_dashboard.jsp?msg=" + WebUtil.enc("Librarian Removed Successfully"));
            } else {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Failed to remove Librarian. Ensure ID is correct."));
            }
        } else if ("editLibrarian".equals(action)) {
            Integer id = Validation.parsePositiveInt(request.getParameter("librarianId"));
            if (id == null) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Invalid Librarian ID"));
                return;
            }
            String email = Validation.normalizeEmail(request.getParameter("email"));
            String pass = request.getParameter("pass");

            if (Validation.isBlank(email)) email = null;
            if (pass != null && pass.isEmpty()) pass = null;

            if (email == null && pass == null) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Provide Email or Password to update"));
                return;
            }

            UserDAO dao = new UserDAO();
            String currentEmail = dao.getLibrarianEmailById(id.intValue());
            if (currentEmail == null) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Librarian not found. Ensure ID is correct."));
                return;
            }

            if (email != null && !Validation.isValidEmail(email)) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Invalid email format"));
                return;
            }
            if (pass != null && !Validation.isStrongPassword(pass, 8)) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc(Validation.passwordRuleMessage(8)));
                return;
            }

            if (email != null && !email.equalsIgnoreCase(currentEmail) && dao.checkEmailExists(email)) {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Email already exists"));
                return;
            }

            boolean success = dao.updateLibrarian(id.intValue(), email, pass);
            if (success) {
                response.sendRedirect("admin_dashboard.jsp?msg=" + WebUtil.enc("Librarian Updated Successfully"));
            } else {
                response.sendRedirect("admin_dashboard.jsp?error=" + WebUtil.enc("Failed to update Librarian"));
            }
        } else if ("addUser".equals(action)) {
            String email = Validation.normalizeEmail(request.getParameter("email"));
            String pass = request.getParameter("pass");

            if (!Validation.isValidEmail(email) || !Validation.isStrongPassword(pass, 8)) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Invalid email or weak password."));
                return;
            }

            UserDAO dao = new UserDAO();
            if (dao.checkEmailExists(email)) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Email already exists"));
                return;
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(pass);
            user.setRole("user");

            boolean success = dao.addUser(user);
            if (success) {
                response.sendRedirect("admin_users.jsp?msg=" + WebUtil.enc("User Added Successfully"));
            } else {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Failed to add User"));
            }
        } else if ("removeUser".equals(action)) {
            Integer id = Validation.parsePositiveInt(request.getParameter("userId"));
            if (id == null) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Invalid User ID"));
                return;
            }
            UserDAO dao = new UserDAO();
            boolean success = dao.deleteUser(id.intValue());
            if (success) {
                response.sendRedirect("admin_users.jsp?msg=" + WebUtil.enc("User Removed Successfully"));
            } else {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Failed to remove User. Ensure ID is correct."));
            }
        } else if ("editUser".equals(action)) {
            Integer id = Validation.parsePositiveInt(request.getParameter("userId"));
            if (id == null) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Invalid User ID"));
                return;
            }
            String email = Validation.normalizeEmail(request.getParameter("email"));
            String pass = request.getParameter("pass");

            if (Validation.isBlank(email)) email = null;
            if (pass != null && pass.isEmpty()) pass = null;

            if (email == null && pass == null) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Provide Email or Password to update"));
                return;
            }

            UserDAO dao = new UserDAO();
            String currentEmail = dao.getUserEmailById(id.intValue());
            if (currentEmail == null) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("User not found. Ensure ID is correct."));
                return;
            }

            if (email != null && !Validation.isValidEmail(email)) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Invalid email format"));
                return;
            }
            if (pass != null && !Validation.isStrongPassword(pass, 8)) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc(Validation.passwordRuleMessage(8)));
                return;
            }

            if (email != null && !email.equalsIgnoreCase(currentEmail) && dao.checkEmailExists(email)) {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Email already exists"));
                return;
            }

            boolean success = dao.updateUser(id.intValue(), email, pass);
            if (success) {
                response.sendRedirect("admin_users.jsp?msg=" + WebUtil.enc("User Updated Successfully"));
            } else {
                response.sendRedirect("admin_users.jsp?error=" + WebUtil.enc("Failed to update User"));
            }
        }
    }
}
