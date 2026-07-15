package com.library.controller;

import com.library.dao.RequestDAO;
import com.library.model.User;
import com.library.util.Validation;
import com.library.util.WebUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
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
        } else if ("returnBook".equals(action)) {
            Integer requestId = Validation.parsePositiveInt(request.getParameter("requestId"));
            String returnAtStr = request.getParameter("returnAt");
            if (requestId == null || Validation.isBlank(returnAtStr)) {
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Invalid return date/time."));
                return;
            }

            HttpSession session = request.getSession(false);
            User user = session == null ? null : (User) session.getAttribute("user");
            if (user == null || !"user".equals(user.getRole())) {
                response.sendRedirect("index.jsp?error=" + WebUtil.enc("Please login first"));
                return;
            }

            Timestamp returnAt;
            try {
                // HTML datetime-local format: yyyy-MM-ddTHH:mm
                LocalDateTime ldt = LocalDateTime.parse(returnAtStr.trim());
                LocalDateTime now = LocalDateTime.now();
                // Disallow selecting a past date/time (allow small clock skew)
                if (ldt.isBefore(now.minus(1, ChronoUnit.MINUTES))) {
                    response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Return date/time cannot be in the past."));
                    return;
                }
                returnAt = Timestamp.valueOf(ldt);
            } catch (DateTimeParseException ex) {
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Invalid return date/time format."));
                return;
            }

            RequestDAO dao = new RequestDAO();
            boolean ok = dao.returnBookByUser(requestId.intValue(), user.getId(), returnAt);
            if (ok) {
                response.sendRedirect("user_requests.jsp?msg=" + WebUtil.enc("Book returned. Fine updated (if any)."));
            } else {
                response.sendRedirect("user_requests.jsp?error=" + WebUtil.enc("Failed to return book. Ensure it is currently issued."));
            }
        }
    }
}
