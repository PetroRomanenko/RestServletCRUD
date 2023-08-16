package com.ferros.rest;


import com.ferros.model.User;
import com.ferros.service.EventService;
import com.ferros.service.FileService;
import com.ferros.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/app/v1/users/*")
public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService = new UserService();
    private static final String USER = "user";
    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get pathInfo from site to decide to show all or specific user
        String pathInfo = req.getPathInfo();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> userList = userService.getAllUsers();
            System.out.println(userList);
            String usersJson = gson.toJson(userList);
            resp.getWriter().write(usersJson);
        } else {
            //Open session to store user data
            var session = req.getSession();

            //receive user id
            String userIdString = pathInfo.substring(1); //remove first symbol "/"
            Integer userId = Integer.parseInt(userIdString);

            User user = userService.getById(userId);
            System.out.println(user);
            if (user != null) {
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

        String requestBody = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        User newUser = gson.fromJson(requestBody, User.class);
        User createdNewUser = userService.createUser(newUser);

        String createdUserJson = new Gson().toJson(createdNewUser);
        resp.getWriter().write(createdUserJson);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get id and new data of user we want to change (UPDATE)
        //{
        //  "name": "New One1"
        //}
        String pathInfo = req.getPathInfo();
        String userIdString = pathInfo.substring(1);
        Integer userId = Integer.parseInt(userIdString);

        String requestBody = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        User updatedUser = gson.fromJson(requestBody, User.class);
        updatedUser.setId(userId);

        User savedUser = userService.updateUser(userId, updatedUser);
        if (savedUser != null) {
            String savedJsonString = gson.toJson(savedUser);
            resp.getWriter().write(savedJsonString);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get id of user we want to delete
        String pathInfo = req.getPathInfo();
        String userIdString = pathInfo.substring(1);
        Integer deletedUserId = Integer.parseInt(userIdString);

        userService.deleteUser(deletedUserId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
