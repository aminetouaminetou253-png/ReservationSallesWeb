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
import jakarta.servlet.http.*;

@WebServlet("/api/salles")
public class SalleApi extends HttpServlet {

    private static final List<Salle> salles = new ArrayList<>();

    static {
        salles.add(new Salle(1, "Salle A", 30,
                "Réunion", 50,
                "Projecteur, Wifi",
                "Bâtiment A - 1er étage"));

        salles.add(new Salle(2, "Salle B", 50,
                "Conférence", 80,
                "Écran, Son",
                "Bâtiment B - RDC"));

        salles.add(new Salle(3, "Salle C", 100,
                "Formation", 120,
                "Ordinateurs, Tableau",
                "Bâtiment C - 2e étage"));
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Salle s : salles) {
            if (!first) json.append(",");
            first = false;

            json.append(String.format(
                    "{\"id\":%d,\"nom\":\"%s\",\"capacite\":%d," +
                    "\"type\":\"%s\",\"prixHeure\":%.2f," +
                    "\"equipements\":\"%s\",\"localisation\":\"%s\"}",
                    s.getId(),
                    s.getNom(),
                    s.getCapacite(),
                    s.getType(),
                    s.getPrixHeure(),
                    s.getEquipements(),
                    s.getLocalisation()
            ));
        }

        json.append("]");
        response.getWriter().print(json.toString());
    }

    // ================= POST (ADMIN) =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;

        response.setContentType("application/json");

        try {
            JSONObject json = readJson(request);

            int id = json.getInt("id");
            String nom = json.getString("nom");
            int capacite = json.getInt("capacite");

            String type = json.getString("type");
            double prixHeure = json.getDouble("prixHeure");
            String equipements = json.getString("equipements");
            String localisation = json.getString("localisation");

            salles.add(new Salle(id, nom, capacite,
                    type, prixHeure, equipements, localisation));

            response.getWriter().print(
                    "{\"success\":true,\"message\":\"Salle ajoutée avec succès\"}"
            );

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "JSON invalide");
        }
    }

    // ================= PUT (ADMIN) =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;

        response.setContentType("application/json");

        try {
            JSONObject json = readJson(request);

            int id = json.getInt("id");
            String nom = json.getString("nom");
            int capacite = json.getInt("capacite");

            String type = json.getString("type");
            double prixHeure = json.getDouble("prixHeure");
            String equipements = json.getString("equipements");
            String localisation = json.getString("localisation");

            for (Salle s : salles) {
                if (s.getId() == id) {

                    s.setNom(nom);
                    s.setCapacite(capacite);
                    s.setType(type);
                    s.setPrixHeure(prixHeure);
                    s.setEquipements(equipements);
                    s.setLocalisation(localisation);

                    response.getWriter().print(
                            "{\"success\":true,\"message\":\"Salle mise à jour\"}"
                    );
                    return;
                }
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Salle non trouvée");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "JSON invalide");
        }
    }

    // ================= DELETE (ADMIN) =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;

        response.setContentType("application/json");

        try {
            JSONObject json = readJson(request);
            int id = json.getInt("id");

            boolean removed = salles.removeIf(s -> s.getId() == id);

            if (removed) {
                response.getWriter().print(
                        "{\"success\":true,\"message\":\"Salle supprimée\"}"
                );
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Salle non trouvée");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "JSON invalide");
        }
    }

    // ================= UTIL =================
    private JSONObject readJson(HttpServletRequest request) throws IOException {

        StringBuilder body = new StringBuilder();
        String line;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return new JSONObject(body.toString());
    }
}