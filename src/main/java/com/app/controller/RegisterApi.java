package com.app.controller;

import com.app.data.UserData;
import com.app.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

@WebServlet("/api/register")
public class RegisterApi extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        JSONObject json = readJson(request);

        String username = json.getString("username");
        String password = json.getString("password");

        if (UserData.usernameExists(username)) {
            response.sendError(409, "Username already exists");
            return;
        }

        User user = new User(
                username,
                password,
                "CLIENT",
                json.getString("nom"),
                json.getString("prenom"),
                json.getString("entreprise"),
                json.getString("email"),
                json.getString("telephone")
        );

        UserData.addUser(user);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print(
            "{\"success\":true,\"message\":\"Account created\"}"
        );
    }

    private JSONObject readJson(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            body.append(line);
        }
        return new JSONObject(body.toString());
    }
}