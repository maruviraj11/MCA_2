package com.library.controller;

import com.library.dao.UserDAO;
import com.library.model.User;
import com.library.util.Validation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("index.jsp");
            return;
        }

        String email = Validation.normalizeEmail(request.getParameter("email"));
        String pass = request.getParameter("pass");

        if (!Validation.isValidEmail(email) || Validation.isBlank(pass)) {
            response.sendRedirect("index.jsp?error=invalid");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmailAndPassword(email, pass);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            switch (user.getRole()) {
                case "admin":
                    response.sendRedirect("admin_dashboard.jsp");
                    break;
                case "librarian":
                    response.sendRedirect("librarian_dashboard.jsp");
                    break;
                case "user":
                    response.sendRedirect("user_dashboard.jsp");
                    break;
            }
        } else {
            response.sendRedirect("index.jsp?error=invalid");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        response.sendRedirect("index.jsp");
    }
}
