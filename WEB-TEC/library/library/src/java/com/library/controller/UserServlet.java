package com.library.controller;

import com.library.dao.RequestDAO;
import com.library.model.User;
import com.library.util.Validation;
import com.library.util.WebUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("requestBook".equals(action)) {
            Integer bookId = Validation.parsePositiveInt(request.getParameter("bookId"));
            if (bookId == null) {
                response.sendRedirect("user_dashboard.jsp?error=" + WebUtil.enc("Invalid book."));
                return;
            }
            
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if (user == null || !"user".equals(user.getRole())) {
                    response.sendRedirect("index.jsp?error=" + WebUtil.enc("Please login first"));
                    return;
                }
                
                RequestDAO dao = new RequestDAO();
                if (dao.createRequest(user.getId(), bookId.intValue())) {
                    response.sendRedirect("user_dashboard.jsp?msg=" + WebUtil.enc("Book Requested Successfully"));
                } else {
                    response.sendRedirect("user_dashboard.jsp?error=" + WebUtil.enc("Failed to request book (already requested / out of stock)."));
                }
            } else {
                response.sendRedirect("index.jsp?error=" + WebUtil.enc("Please login first"));
            }
        }
    }
}
