package com.ferros.rest;


import com.ferros.model.Event;
import com.ferros.model.File;
import com.ferros.service.EventService;
import com.ferros.service.FileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/app/v1/events/*")
public class EventRestControllerV1 extends HttpServlet {

    private FileService fileService = new FileService();
    private final EventService eventService = new EventService();
    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get pathInfo from site to decide to show all or specific events
        String pathInfo = req.getPathInfo();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")){
            List<Event> eventList = eventService.getAllEvents();
            String eventsJson = gson.toJson(eventList);
            resp.getWriter().write(eventsJson);
        }else {
            //receive event id
            String eventIdString = pathInfo.substring(1);
            Integer eventId = Integer.parseInt(eventIdString);

            Event event = eventService.getById(eventId);
            if (event!= null){
                String eventJsonString = gson.toJson(event);
                resp.getWriter().write(eventJsonString);
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        resp.setContentType("application/json");
//
//        File result = fileService.uploadFile(req);
//        //GSON.toJson(result);
//        String json = "";
//        resp.getWriter().write(json);
//    }
}
