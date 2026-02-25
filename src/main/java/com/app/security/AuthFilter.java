package com.app.security;

import com.app.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

     // السماح بدون تسجيل الدخول
     if (path.contains("/api/login") || path.contains("/api/register")) {
         chain.doFilter(request, response);
         return;
     }

     

        // السماح بتسجيل الدخول
        if (path.contains("/login")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().print("{\"error\":\"Please login first\"}");
            return;
        }

        // المستخدم مسجل → يسمح بالمرور
        chain.doFilter(request, response);
    }
}
