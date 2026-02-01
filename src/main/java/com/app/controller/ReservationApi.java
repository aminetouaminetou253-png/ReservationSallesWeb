package com.app.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/reservations")
public class ReservationApi extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json =
            "[" +
            "{\"id\":1,\"salle\":\"Salle A\",\"date\":\"2025-02-10\",\"heure\":\"10:00\"}," +
            "{\"id\":2,\"salle\":\"Salle B\",\"date\":\"2025-02-11\",\"heure\":\"14:00\"}" +
            "]";

        response.getWriter().print(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Simulation création réservation
        String json =
            "{" +
            "\"status\":\"success\"," +
            "\"message\":\"Reservation created successfully\"" +
            "}";

        response.getWriter().print(json);
    }
}
