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

import javax.imageio.ImageTranscoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet("/app/v1/users")
public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService = new UserService();
    private final FileService fileService = new FileService();
    private final EventService eventService = new EventService();
    private  Gson gson=new Gson();
    //TODO: connect GSON



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get pathInfo from site to decide to show all or specific user
        String pathInfo = req.getPathInfo();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        if(pathInfo!=null || pathInfo.equals("/")){
            List<User> userList = userService.getAllUsers();
            String userJson = gson.toJson(userList);
            resp.getWriter().write(userJson);
        }else {
            //receive user id
            String userIdString = pathInfo.substring(1); //remove first symbol "/"
            Integer userId = Integer.parseInt(userIdString);

            User user = userService.getById(userId);
            if (user!=null) {
                String userJsonString = gson.toJson(user);
                resp.getWriter().write(userJsonString);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }


        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        BufferedReader reader = req.getReader();
        User createdNewUser = new Gson().fromJson(reader, User.class);
        System.out.println(createdNewUser);
        System.out.println(createdNewUser.getEvents());
        userService.createUser(req,createdNewUser);
//
      resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get id and new data of user we want to change
        Integer userId = Integer.parseInt(req.getParameter("id"));
        String newUserName = req.getParameter("name");
        if (userId!=null && newUserName!=null) {

            userService.updateUser(userId, newUserName);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("User upate succesfuly");
        }else {
            resp.getWriter().write("NO Such User");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get id of user we want to delete
        Integer deletedUserId = Integer.parseInt(req.getParameter("id"));
    }
}
