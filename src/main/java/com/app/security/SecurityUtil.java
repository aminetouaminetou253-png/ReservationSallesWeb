package com.app.security;

import com.app.model.User;
import jakarta.servlet.http.*;
import java.io.IOException;

public class SecurityUtil {

    public static boolean checkRole(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String requiredRole)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals(requiredRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
