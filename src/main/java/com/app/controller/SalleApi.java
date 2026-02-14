package com.app.controller;

import com.app.model.Salle;
import com.app.security.SecurityUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/salles")
public class SalleApi extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 🔴 stockage temporaire
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
            json.append(String.format(
                "{\"id\":%d,\"nom\":\"%s\",\"capacite\":%d}",
                s.getId(), s.getNom(), s.getCapacite()
            ));
            if (i < salles.size() - 1) json.append(",");
        }
        json.append("]");

        response.getWriter().print(json.toString());
    }

    // ================= POST =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JSONObject json = readJson(request);

            int id = json.getInt("id");
            String nom = json.getString("nom");
            int capacite = json.getInt("capacite");

            salles.add(new Salle(id, nom, capacite));

            response.getWriter().print(
                "{\"success\":true,\"message\":\"Salle added successfully\"}"
            );

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Invalid JSON body\"}"
            );
        }
    }

    // ================= PUT =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JSONObject json = readJson(request);

            int id = json.getInt("id");
            String nom = json.getString("nom");
            int capacite = json.getInt("capacite");

            for (Salle s : salles) {
                if (s.getId() == id) {
                    s.setNom(nom);
                    s.setCapacite(capacite);

                    response.getWriter().print(
                        "{\"success\":true,\"message\":\"Salle updated successfully\"}"
                    );
                    return;
                }
            }

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Salle not found\"}"
            );

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Invalid JSON body\"}"
            );
        }
    }

    // ================= DELETE =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            JSONObject json = readJson(request);
            int id = json.getInt("id");

            boolean removed = salles.removeIf(s -> s.getId() == id);

            if (removed) {
                response.getWriter().print(
                    "{\"success\":true,\"message\":\"Salle deleted successfully\"}"
                );
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().print(
                    "{\"success\":false,\"message\":\"Salle not found\"}"
                );
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Invalid JSON body\"}"
            );
        }
    }

    // ================= UTILITY =================
    private JSONObject readJson(HttpServletRequest request) throws Exception {
        StringBuilder body = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return new JSONObject(body.toString());
    }
}
