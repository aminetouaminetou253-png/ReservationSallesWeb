package com.app.controller;

import com.app.model.Salle;
import com.app.security.SecurityUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/salles")
public class SalleApi extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 🔴 stockage temporaire (à la place de la DB)
    private static final List<Salle> salles = new ArrayList<>();

    static {
        salles.add(new Salle(1, "Salle A", 30));
        salles.add(new Salle(2, "Salle B", 50));
        salles.add(new Salle(3, "Salle C", 100));
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < salles.size(); i++) {
            Salle s = salles.get(i);
            json.append(
                "{"
                + "\"id\":" + s.getId() + ","
                + "\"nom\":\"" + s.getNom() + "\","
                + "\"capacite\":" + s.getCapacite()
                + "}"
            );
            if (i < salles.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        response.getWriter().print(json.toString());
    }

    // ================= POST (ADD) =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkAdmin(request, response)) return;

        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("nom");
        int capacite = Integer.parseInt(request.getParameter("capacite"));

        salles.add(new Salle(id, nom, capacite));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print(
            "{"
            + "\"success\": true,"
            + "\"message\": \"Salle added successfully\""
            + "}"
        );
    }

    // ================= PUT (UPDATE) =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkAdmin(request, response)) return;

        String body = getBody(request);

        // مثال body: id=1&nom=Salle+A&capacite=40
        String[] params = body.split("&");

        int id = 0;
        String nom = "";
        int capacite = 0;

        for (String p : params) {
            String[] kv = p.split("=");
            if (kv[0].equals("id")) id = Integer.parseInt(kv[1]);
            if (kv[0].equals("nom")) { nom = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
            }

            if (kv[0].equals("capacite")) capacite = Integer.parseInt(kv[1]);
        }

        for (Salle s : salles) {
            if (s.getId() == id) {
                s.setNom(nom);
                s.setCapacite(capacite);

                response.setContentType("application/json");
                response.getWriter().print(
                    "{"
                    + "\"success\": true,"
                    + "\"message\": \"Salle updated successfully\""
                    + "}"
                );
                return;
            }
        }

        response.setStatus(404);
        response.getWriter().print(
            "{"
            + "\"success\": false,"
            + "\"message\": \"Salle not found\""
            + "}"
        );
    }

    // ================= DELETE =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkAdmin(request, response)) return;

        int id = Integer.parseInt(request.getParameter("id"));

        salles.removeIf(s -> s.getId() == id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print(
            "{"
            + "\"success\": true,"
            + "\"message\": \"Salle deleted successfully\""
            + "}"
        );
    }
    
    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (var reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

}
