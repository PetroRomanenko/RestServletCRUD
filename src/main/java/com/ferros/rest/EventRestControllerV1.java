package com.ferros.rest;


import com.ferros.model.File;
import com.ferros.service.FileService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet("/app/v1/events")
public class EventRestControllerV1 extends HttpServlet {

    private FileService fileService = new FileService();
    private Gson gson = new Gson();    //TODO: connect GSON

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File result = fileService.uploadFile(req);
        //GSON.toJson(result);
        String json = "";
        resp.getWriter().write(json);
    }
}
