package com.app.controller;

import com.app.data.UserData;
import org.json.JSONObject;

import com.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/login")
public class LoginApi extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = request.getReader().lines().reduce("", String::concat);
        JSONObject json = new JSONObject(body);

        String username = json.getString("username");
        String password = json.getString("password");


        User user = UserData.findByUsernameAndPassword(username, password);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(
                    "{\"success\":false,\"message\":\"Invalid credentials\"}"
            );
            return;
        }

        request.getSession().setAttribute("user", user);

        response.getWriter().print(
                "{"
                        + "\"success\":true,"
                        + "\"username\":\"" + user.getUsername() + "\","
                        + "\"role\":\"" + user.getRole() + "\""
                        + "}"
        );
    }
}
