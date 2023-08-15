package com.ferros.rest;


import com.ferros.model.File;
import com.ferros.service.FileService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/app/v1/files/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 5 * 5
)
public class FileRestControllerV1 extends HttpServlet {
    private FileService fileService = new FileService();
    private Gson gson = new Gson();




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")){
            List<File> fileList = fileService.getAllFiles();
            String eventsJson = gson.toJson(fileList);
            resp.getWriter().write(eventsJson);
        }else {
            //receive event id
            String FileIdString = pathInfo.substring(1);
            Integer fileId = Integer.parseInt(FileIdString);


            //Get file from bd using ID
            File file = fileService.getFile(fileId);

            if (file != null) {
                //Set Headers for response for downloading file
                resp.setContentType("application/octet-stream");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

                //Create input stream
                try (InputStream inputStream = new FileInputStream(file.getFilePath());
                     OutputStream outputStream = resp.getOutputStream()) {
                    //Copy file to output stream
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            }
        }

    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Upload file
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());


        String pathInfo = req.getPathInfo();
        //receive event id
        String FileIdString = pathInfo.substring(1);
        Integer userId = Integer.parseInt(FileIdString);

        //Get Part of file and uploading file
        Part filePart = req.getPart("file");
        String fileName = fileService.fileUploadService(filePart,userId).getName();




        resp.getWriter().println("File " + fileName + " has been uploaded successfully.");

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Update name of file
        String pathInfo = req.getPathInfo();
        String fileIdString = pathInfo.substring(1);
        Integer fileId = Integer.parseInt(fileIdString);

        String requestBody = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        File updatedFile= gson.fromJson(requestBody, File.class);
        updatedFile.setId(fileId);

        File savedFile = fileService.upateFile(updatedFile);
        if (savedFile != null) {
            String savedJsonString = gson.toJson(savedFile);
            resp.getWriter().write(savedJsonString);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileService.deleteFile(req);
    }
}
