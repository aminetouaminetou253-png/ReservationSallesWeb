package com.app.security;

import com.app.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/api/*")
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String method = req.getMethod();

        // ✅ السماح بدون تسجيل دخول
        if (path.contains("/login") || path.contains("/register")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // ✅ التحقق من session
        if (session == null || session.getAttribute("user") == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        // ===== SALLES =====
        if (path.contains("/salle")) {

            if (method.equals("GET")) {
                chain.doFilter(request, response);
                return;
            }

            if (!role.equals("ADMIN")) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Only ADMIN can manage salles");
                return;
            }
        }

        // ===== RESERVATIONS =====
        if (path.contains("/reservations")) {

            // CLIENT + GESTIONNAIRE يمكنهم GET
            if (method.equals("GET")) {
                chain.doFilter(request, response);
                return;
            }

            // CLIENT ينشئ حجز
            if (method.equals("POST") && role.equals("CLIENT")) {
                chain.doFilter(request, response);
                return;
            }

            // GESTIONNAIRE يصادق أو يرفض
            if (method.equals("PUT") && role.equals("GESTIONNAIRE")) {
                chain.doFilter(request, response);
                return;
            }

            // CLIENT يحذف حجزه
            if (method.equals("DELETE") && role.equals("CLIENT")) {
                chain.doFilter(request, response);
                return;
            }

            res.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Access denied for your role");
            return;
        }

        chain.doFilter(request, response);
    }
}