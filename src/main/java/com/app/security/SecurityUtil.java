package com.app.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityUtil {

    public static boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String role = request.getHeader("X-ROLE");

        if (role == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(
                "{"
                 + "\"success\": false,"
                 + "\"message\": \"Missing role header\""
              + "}"
            );
            return false;
        }

        if (!role.equalsIgnoreCase("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(
                "{"
                  + "\"success\": false,"
                  + "\"message\": \"Access denied\""
                 
                +"}"
            );
            return false;
        }

        return true;
    }
}
