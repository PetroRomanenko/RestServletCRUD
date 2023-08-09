package com.ferros.rest;


import com.ferros.model.User;
import com.ferros.service.EventService;
import com.ferros.service.FileService;
import com.ferros.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/app/v1/users")
public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService = new UserService();
    private final FileService fileService = new FileService();
    private final EventService eventService = new EventService();
    private  Gson gson=new Gson();
    //TODO: connect GSON

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");
        String idValue = req.getParameter("id");
        System.out.println(idValue);
        User requestedUser = userService.getById(Integer.valueOf(idValue));

        String userJsonString=gson.toJson(requestedUser);
        
        PrintWriter out = resp.getWriter();
        out.print(userJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        BufferedReader reader = req.getReader();
        User createdNewUser = new Gson().fromJson(reader, User.class);
        System.out.println(createdNewUser);
        System.out.println(createdNewUser.getEvents());
        User user = userService.createUser(req,createdNewUser);
//
//      resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
