package com.ferros.rest;


import com.ferros.model.File;
import com.ferros.model.User;
import com.ferros.service.FileService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/api/v1/files")
public class FileRestControllerV1 extends HttpServlet {
    private FileService fileService = new FileService();
    private Gson gson = new Gson();
    //TODO: connect GSON

    private File fileFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String json = stringBuilder.toString();


        return gson.fromJson(json, File.class);
    }

    private String gsonToJson(File file){
        return gson.toJson(file,File.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");
        File result = fileService.uploadFile(req);

        BufferedReader reader = req.getReader();
        File createdNewFile = new Gson().fromJson(reader, File.class);
        System.out.println(createdNewFile);
        fileService.saveFileToDB(createdNewFile);
//        String json = gsonToJson(result);
//        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileService.deleteFile(req);
    }
}
