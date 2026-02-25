package com.app.controller;

import com.app.model.Reservation;
import com.app.model.User;
import com.app.security.SecurityUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/reservations")
public class ReservationApi extends HttpServlet {

    public static final List<Reservation> reservations = new ArrayList<>();
    private static int idCounter = 1;

    private JSONObject readJson(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return new JSONObject(body.toString());
    }

    // ================= GET =================
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = (User) request.getSession().getAttribute("user");

        response.setContentType("application/json");
        StringBuilder json = new StringBuilder("[");

        boolean first = true;

        for (Reservation r : reservations) {

            if (user.getRole().equals("CLIENT")
                    && !r.getClient().equals(user.getUsername())) continue;

            if (!first) json.append(",");
            first = false;

            json.append("{")
                .append("\"id\":").append(r.getId()).append(",")
                .append("\"salleId\":").append(r.getSalleId()).append(",")
                .append("\"date\":\"").append(r.getDate()).append("\",")
                .append("\"statut\":\"").append(r.getStatut()).append("\",")
                .append("\"cout\":").append(r.getCout())
                .append("}");
        }

        json.append("]");
        response.getWriter().print(json.toString());
    }

    // ================= POST (CLIENT) =================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!SecurityUtil.checkRole(request, response, "CLIENT")) return;

        JSONObject json = readJson(request);

        int salleId = json.getInt("salleId");
        String date = json.getString("date");
        int duree = json.getInt("duree");
        int nb = json.getInt("nbParticipants");
        String services = json.getString("services");

        if (LocalDate.parse(date).isBefore(LocalDate.now())) {
            response.sendError(400, "Date invalide");
            return;
        }

        for (Reservation r : reservations) {
            if (r.getSalleId() == salleId && r.getDate().equals(date)) {
                response.sendError(409, "Salle déjà réservée");
                return;
            }
        }

        double cout = duree * 100; // prix simple

     // تكلفة الخدمات
     if (services.equalsIgnoreCase("cafe")) {
         cout += 20;
     }
     else if (services.equalsIgnoreCase("traiteur")) {
         cout += 50;
     }
     else if (services.equalsIgnoreCase("vip")) {
         cout += 100;
     }

        User user = (User) request.getSession().getAttribute("user");

        reservations.add(new Reservation(
                idCounter++, salleId, user.getUsername(),
                date, duree, nb, services,
                "EN_ATTENTE", cout
        ));

        response.getWriter().print("{\"success\":true,\"cout\":" + cout + "}");
    }

    // ================= PUT (MANAGER) =================
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!SecurityUtil.checkRole(request, response, "GESTIONNAIRE")) return;

        JSONObject json = readJson(request);
        int id = json.getInt("id");
        String statut = json.getString("statut");

        for (Reservation r : reservations) {
            if (r.getId() == id) {
                r.setStatut(statut);
                response.getWriter().print("{\"success\":true}");
                return;
            }
        }

        response.sendError(404, "Reservation non trouvée");
    }

    // ================= DELETE (CLIENT) =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!SecurityUtil.checkRole(request, response, "CLIENT")) return;

        JSONObject json = readJson(request);
        int id = json.getInt("id");

        User user = (User) request.getSession().getAttribute("user");

        for (Reservation r : reservations) {

            if (r.getId() == id && r.getClient().equals(user.getUsername())) {

                LocalDate reservationDate = LocalDate.parse(r.getDate());

                // ❌ منع الإلغاء قبل 24 ساعة
                if (!LocalDate.now().isBefore(reservationDate.minusDays(1))) {
                    response.sendError(400,
                            "Impossible d'annuler moins de 24h avant la réservation");
                    return;
                }

                reservations.remove(r);
                response.getWriter().print("{\"success\":true,\"message\":\"Reservation annulée\"}");
                return;
            }
        }

        response.sendError(404, "Reservation non trouvée");
    }
}