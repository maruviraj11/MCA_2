package com.cmp.servlet;

import com.cmp.dao.AuthDao;
import com.cmp.util.SessionUtil;
import com.cmp.util.ValidationUtil;
import com.cmp.util.ViewUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    private final AuthDao authDao = new AuthDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ViewUtil.forward("login.jsp", request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Enter a valid email address.");
            ViewUtil.forward("login.jsp", request, response);
            return;
        }
        Map user = authDao.authenticate(email, request.getParameter("password"));
        if (user == null) {
            request.setAttribute("error", "Invalid email or password.");
            ViewUtil.forward("login.jsp", request, response);
            return;
        }
        SessionUtil.login(request, user);
        if (Boolean.TRUE.equals(user.get("mustChangePassword"))) {
            response.sendRedirect(request.getContextPath() + "/user?action=changePasswordPage");
            return;
        }
        response.sendRedirect(request.getContextPath() + routeForRole(String.valueOf(user.get("role"))));
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
}
