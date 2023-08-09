package com.ferros.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
    private String message;

    public void init() throws ServletException {
        message = "This is simple servlet message";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        response.setContentType("text/html");

        PrintWriter messageWriter = response.getWriter();
        messageWriter.println("<h1>" + message + "<h1>");
    }

    public void destroy() {

    }
}
