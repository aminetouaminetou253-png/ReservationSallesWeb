package com.app.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/login")
public class LoginApi extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    String json =
	            "{"
	          + "\"status\":\"success\","
	          + "\"message\":\"GET login API works\""
	          + "}";

	    response.getWriter().print(json);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    String json =
	            "{"
	          + "\"status\":\"success\","
	          + "\"message\":\"POST login API works\""
	          + "}";

	    response.getWriter().print(json);
	}

}
