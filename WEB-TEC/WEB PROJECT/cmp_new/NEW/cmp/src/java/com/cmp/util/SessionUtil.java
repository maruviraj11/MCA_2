package com.cmp.util;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtil {

    private SessionUtil() {
    }

    public static void login(HttpServletRequest request, Map user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("role", user.get("role"));
    }

    public static Map getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Map) session.getAttribute("user");
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getUser(request) != null;
    }

    public static boolean hasRole(HttpServletRequest request, String role) {
        Map user = getUser(request);
        return user != null && role.equals(String.valueOf(user.get("role")));
    }
}
