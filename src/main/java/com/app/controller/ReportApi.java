package com.app.controller;

import com.app.model.Reservation;
import com.app.security.SecurityUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/reports")
public class ReportApi extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!SecurityUtil.checkRole(request, response, "ADMIN")) return;

        double totalRevenue = 0;
        Map<String, Integer> clientCount = new HashMap<>();

        for (Reservation r : ReservationApi.reservations) {

            if (r.getStatut().equals("VALIDEE")) {
                totalRevenue += r.getCout();
            }

            clientCount.put(
                    r.getClient(),
                    clientCount.getOrDefault(r.getClient(), 0) + 1
            );
        }

        StringBuilder json = new StringBuilder("{");

        json.append("\"revenuTotal\":").append(totalRevenue).append(",");
        json.append("\"clientsActifs\":").append(clientCount.size());

        json.append("}");

        response.setContentType("application/json");
        response.getWriter().print(json.toString());
    }
}