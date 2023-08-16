package com.ferros.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class UtilsServlet extends HttpServlet {
    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    public  Integer getInteger(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String eventIdString = pathInfo.substring(1);
        Integer eventId = Integer.parseInt(eventIdString);
        return eventId;
    }

    public  <T> T deserialize(HttpServletRequest req,Class<T> clazz) throws IOException {
        String requestBody = req.getReader().lines().collect(Collectors.joining());

        return  gson.fromJson(requestBody,clazz);
    }

}
