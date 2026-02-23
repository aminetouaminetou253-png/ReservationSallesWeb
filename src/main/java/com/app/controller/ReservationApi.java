package com.app.controller;

import com.app.model.Reservation;
import com.app.model.User;
import com.app.security.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

@WebServlet("/api/reservations")
public class ReservationApi extends HttpServlet {

    private static final List<Reservation> reservations = new ArrayList<>();

    static {
        reservations.add(new Reservation(1, 1, "Ali",
                "2026-02-01", 2,
                "EN_ATTENTE", 200));

        reservations.add(new Reservation(2, 2, "Sara",
                "2026-02-05", 3,
                "VALIDEE", 300));
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Reservation r : reservations) {

            // CLIENT يرى فقط حجوزه
            if (user.getRole().equals("CLIENT") &&
                    !r.getNomClient().equals(user.getUsername())) {
                continue;
            }

            if (!first) json.append(",");
            first = false;

            json.append(String.format(
                    "{\"id\":%d,\"salleId\":%d,\"nomClient\":\"%s\",\"date\":\"%s\",\"statut\":\"%s\",\"cout\":%.2f}",
                    r.getId(), r.getSalleId(), r.getNomClient(),
                    r.getDate(), r.getStatut(), r.getCoutTotal()
            ));
        }

        json.append("]");
        response.getWriter().print(json.toString());
    }

    // ================= POST (CLIENT) =================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "CLIENT")) return;

        response.setContentType("application/json");

        try {
            JSONObject json = readJson(request);

            int id = json.getInt("id");
            int salleId = json.getInt("salleId");
            String nomClient = json.getString("nomClient");
            String date = json.getString("date");
            int duree = json.getInt("duree");

            // 🔴 Date future
            if (LocalDate.parse(date).isBefore(LocalDate.now())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Date must be in the future");
                return;
            }

            // 🔴 Durée max 8h
            if (duree > 8) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Reservation cannot exceed 8 hours");
                return;
            }

            // 🔴 Disponibilité
            for (Reservation r : reservations) {
                if (r.getSalleId() == salleId &&
                    r.getDate().equals(date)) {

                    response.sendError(HttpServletResponse.SC_CONFLICT,
                            "Salle already reserved");
                    return;
                }
            }

            double cout = duree * 100;

            reservations.add(new Reservation(
                    id, salleId, nomClient,
                    date, duree,
                    "EN_ATTENTE", cout
            ));

            response.getWriter().print(
                    "{\"success\":true,\"message\":\"Reservation created\",\"cout\":" + cout + "}"
            );

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid JSON body");
        }
    }

    // ================= PUT (GESTIONNAIRE) =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "GESTIONNAIRE")) return;

        response.setContentType("application/json");

        JSONObject json = readJson(request);

        int id = json.getInt("id");
        String statut = json.getString("statut");

        for (Reservation r : reservations) {
            if (r.getId() == id) {
                r.setStatut(statut);

                response.getWriter().print(
                        "{\"success\":true,\"message\":\"Reservation updated\"}"
                );
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    // ================= DELETE (CLIENT) =================
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        if (!SecurityUtil.checkRole(request, response, "CLIENT")) return;

        response.setContentType("application/json");

        JSONObject json = readJson(request);
        int id = json.getInt("id");

        for (Reservation r : reservations) {

            if (r.getId() == id) {

                LocalDate reservationDate = LocalDate.parse(r.getDate());

                // 🔴 règle 24h
                if (reservationDate.minusDays(1).isBefore(LocalDate.now())) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Cannot cancel less than 24h before");
                    return;
                }

                reservations.remove(r);

                response.getWriter().print(
                        "{\"success\":true,\"message\":\"Reservation cancelled\"}"
                );
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
