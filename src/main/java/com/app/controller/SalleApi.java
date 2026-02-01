package com.app.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/salles")
public class SalleApi extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    String json =
	        "[" +
	        "{\"id\":1,\"nom\":\"Salle A\",\"capacite\":30}," +
	        "{\"id\":2,\"nom\":\"Salle B\",\"capacite\":50}," +
	        "{\"id\":3,\"nom\":\"Salle C\",\"capacite\":100}" +
	        "]";

	    response.getWriter().print(json);
	}
}
