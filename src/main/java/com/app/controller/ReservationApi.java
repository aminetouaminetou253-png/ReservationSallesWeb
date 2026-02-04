package com.app.controller;

import com.app.model.Reservation;
import com.app.security.SecurityUtil;

import java.io.BufferedReader;
import org.json.JSONObject;
import org.json.JSONException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/reservations")
public class ReservationApi extends HttpServlet {

    // 🔴 stockage temporaire
    private static final List<Reservation> reservations = new ArrayList<>();

    static {
        reservations.add(new Reservation(1, 1, "Ali", "2026-02-01"));
        reservations.add(new Reservation(2, 2, "Sara", "2026-02-05"));
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            json.append(String.format(
                "{\"id\":%d,\"salleId\":%d,\"nomClient\":\"%s\",\"date\":\"%s\"}",
                r.getId(), r.getSalleId(), r.getNomClient(), r.getDate()
            ));
            if (i < reservations.size() - 1) json.append(",");
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
        int salleId = Integer.parseInt(request.getParameter("salleId"));
        String nomClient = request.getParameter("nomClient");
        String date = request.getParameter("date");

        reservations.add(new Reservation(id, salleId, nomClient, date));

        response.setContentType("application/json");
        response.getWriter().print(
            "{\"success\":true,\"message\":\"Reservation added successfully\"}"
        );
    }

    // ================= PUT (UPDATE) =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkAdmin(request, response)) return;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder body = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        try {
            JSONObject json = new JSONObject(body.toString());

            int id = json.getInt("id");
            int salleId = json.getInt("salleId");   // ✅ نفس الاسم
            String nomClient = json.getString("nomClient");
            String date = json.getString("date");

            for (Reservation r : reservations) {
                if (r.getId() == id) {
                    r.setSalleId(salleId);
                    r.setNomClient(nomClient);
                    r.setDate(date);

                    response.getWriter().print(
                        "{\"success\":true,\"message\":\"Reservation updated successfully\"}"
                    );
                    return;
                }
            }

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Reservation not found\"}"
            );

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Invalid JSON body\"}"
            );
        }
    }


    // ================= DELETE =================
 // ================= DELETE =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkAdmin(request, response)) return;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // قراءة JSON من body
        StringBuilder body = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        try {
            JSONObject json = new JSONObject(body.toString());
            int id = json.getInt("id");

            boolean removed = reservations.removeIf(r -> r.getId() == id);

            if (removed) {
                response.getWriter().print(
                    "{\"success\":true,\"message\":\"Reservation deleted successfully\"}"
                );
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().print(
                    "{\"success\":false,\"message\":\"Reservation not found\"}"
                );
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"success\":false,\"message\":\"Invalid JSON body\"}"
            );
        }
    }

}
