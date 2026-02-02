package com.app.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.app.data.UserData;
import com.app.model.User;

@WebServlet("/api/login")
public class LoginApi extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 1️⃣ Vérification des champs
        if (username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{"
              + "\"success\": false,"
              + "\"message\": \"username and password are required\""
              + "}"
            );
            return; // ⭐ IMPORTANT
        }

        User user = UserData.findByUsernameAndPassword(username, password);

        // 2️⃣ Mauvais login
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(
                "{"
              + "\"success\": false,"
              + "\"message\": \"Invalid credentials\""
              + "}"
            );
        }
        // 3️⃣ Login OK
        else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(
                "{"
              + "\"success\": true,"
              + "\"username\": \"" + user.getUsername() + "\","
              + "\"role\": \"" + user.getRole() + "\""
              + "}"
            );
        }
    }
}
