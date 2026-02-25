package com.app.controller;

import com.app.data.UserData;
import com.app.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/managers")
public class ManagerApi extends HttpServlet {

    // 🔹 قراءة JSON من الطلب
    private JSONObject readJson(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return new JSONObject(body.toString());
    }

    // =================================================
    // ✅ إضافة Manager (ADMIN فقط)
    // =================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            response.sendError(403, "Access denied");
            return;
        }

        JSONObject json = readJson(request);

        String username = json.getString("username");
        String password = json.getString("password");

        if (UserData.usernameExists(username)) {
            response.sendError(409, "Username already exists");
            return;
        }

        User manager = new User(
                username,
                password,
                "GESTIONNAIRE",
                json.getString("nom"),
                json.getString("prenom"),
                "-",
                json.getString("email"),
                json.getString("telephone")
        );

        UserData.addUser(manager);

        response.setContentType("application/json");
        response.getWriter().print("{\"success\":true,\"message\":\"Manager added\"}");
    }

    // =================================================
    // ✅ عرض جميع Managers
    // =================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            response.sendError(403, "Access denied");
            return;
        }

        response.setContentType("application/json");

        StringBuilder json = new StringBuilder("[");

        for (User u : UserData.users) {
            if (u.getRole().equals("GESTIONNAIRE")) {
                json.append("{")
                        .append("\"username\":\"").append(u.getUsername()).append("\",")
                        .append("\"nom\":\"").append(u.getNom()).append("\",")
                        .append("\"prenom\":\"").append(u.getPrenom()).append("\",")
                        .append("\"email\":\"").append(u.getEmail()).append("\",")
                        .append("\"telephone\":\"").append(u.getTelephone()).append("\"")
                        .append("},");
            }
        }

        if (json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }

        json.append("]");

        response.getWriter().print(json.toString());
    }

    // =================================================
    // ✅ تعديل Manager
    // =================================================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            response.sendError(403, "Access denied");
            return;
        }

        JSONObject json = readJson(request);
        String username = json.getString("username");

        for (User u : UserData.users) {

            if (u.getUsername().equals(username)
                    && u.getRole().equals("GESTIONNAIRE")) {

                u.setNom(json.getString("nom"));
                u.setPrenom(json.getString("prenom"));
                u.setEmail(json.getString("email"));
                u.setTelephone(json.getString("telephone"));

                response.setContentType("application/json");
                response.getWriter().print("{\"success\":true,\"message\":\"Manager updated\"}");
                return;
            }
        }

        response.sendError(404, "Manager not found");
    }

    // =================================================
    // ✅ حذف Manager
    // =================================================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            response.sendError(403, "Access denied");
            return;
        }

        JSONObject json = readJson(request);
        String username = json.getString("username");

        boolean removed = UserData.removeManager(username);

        response.setContentType("application/json");

        if (removed) {
            response.getWriter().print("{\"success\":true,\"message\":\"Manager removed\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print("{\"success\":false,\"message\":\"Manager not found\"}");
        }
    }
}