package com.library.controller;

import com.library.dao.UserDAO;
import com.library.model.User;
import com.library.util.Validation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = Validation.normalizeEmail(request.getParameter("email"));
        String pass = request.getParameter("pass");
        String cpass = request.getParameter("cpass");

        if (!Validation.isValidEmail(email)) {
            response.sendRedirect("register.jsp?error=failed");
            return;
        }

        if (Validation.isBlank(pass) || Validation.isBlank(cpass)) {
            response.sendRedirect("register.jsp?error=failed");
            return;
        }

        if (!Validation.isStrongPassword(pass, 8)) {
            response.sendRedirect("register.jsp?error=failed");
            return;
        }

        if (!pass.equals(cpass)) {
            response.sendRedirect("register.jsp?error=mismatch");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.checkEmailExists(email)) {
            response.sendRedirect("register.jsp?error=exists");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);
        user.setRole("user");

        if (userDAO.addUser(user)) {
            response.sendRedirect("index.jsp?success=registered");
        } else {
            response.sendRedirect("register.jsp?error=failed");
        }
    }
}
