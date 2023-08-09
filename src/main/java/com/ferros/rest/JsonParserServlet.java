package com.ferros.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/json-parser")
public class JsonParserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var printWriter = resp.getWriter()) {
            printWriter.write("Get fdom json parser");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Allow specific HTTP methods (e.g., POST, GET, OPTIONS, etc.)
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        // Allow specific HTTP headers (if needed)
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // Set response content type to JSON
        response.setContentType("application/json");

        // Get the JSON data from the request
        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);

            // You can now access the JSON properties using jsonObject.get("property_name") method

            // Example: Getting a property named "name"
            String name = jsonObject.get("name").getAsString();

            // Prepare the response JSON
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Hello, " + name + "! Your JSON was parsed successfully.");

            // Send the response JSON
            PrintWriter out = response.getWriter();
            out.print(responseJson);
            out.flush();
        } catch (Exception e) {
            // If there is an error while parsing JSON or handling the request, return an error message
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Failed to parse JSON or process the request.");
            PrintWriter out = response.getWriter();
            out.print(errorJson);
            out.flush();
            e.printStackTrace();
        }
    }
}

