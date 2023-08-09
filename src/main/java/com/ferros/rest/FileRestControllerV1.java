package com.ferros.rest;


import com.ferros.model.File;
import com.ferros.service.FileService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Receive File id from user from request
        Integer fileId = Integer.parseInt(req.getParameter("fileId"));

        //Get file from bd using ID
        File file = fileService.getFile(fileId);

        if(file!=null){
            //Set Headers for response for downloading file
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition","attachment; filename=\""+file.getName()+"\"");

            //Create input stream
            try(InputStream inputStream = new FileInputStream(file.getFilePath());
                OutputStream outputStream = resp.getOutputStream()) {
                //Copy file to output stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead =inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,bytesRead);
                }
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        }

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
