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
import java.util.stream.Collectors;

@WebServlet("/app/v1/files/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 5 * 5
)
public class FileRestControllerV1 extends HttpServlet {
    private FileService fileService = new FileService();
    private final UtilsServlet utilsServlet = new UtilsServlet();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (pathInfo == null || pathInfo.equals("/")){
            List<File> fileList = fileService.getAllFiles();
            String eventsJson = gson.toJson(fileList);
            resp.getWriter().write(eventsJson);
        }else {
            Integer fileId = utilsServlet.getInteger(req);
            File file = fileService.getFile(fileId);

            if (file != null) {
                resp.setContentType("application/octet-stream");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

                try (InputStream inputStream = new FileInputStream(file.getFilePath());
                     OutputStream outputStream = resp.getOutputStream()) {

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
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Integer userId = utilsServlet.getInteger(req);

        Part filePart = req.getPart("file");
        String fileName = fileService.fileUploadService(filePart,userId).getName();

        resp.getWriter().println("File " + fileName + " has been uploaded successfully.");

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer fileId = utilsServlet.getInteger(req);

        File updatedFile = utilsServlet.deserialize(req, File.class);


        File savedFile = fileService.upateFile(fileId,updatedFile.getName());
        if (savedFile != null) {
            String savedJsonString = gson.toJson(savedFile);
            resp.getWriter().write(savedJsonString);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer deletedFileId = utilsServlet.getInteger(req);

        fileService.deleteFile(deletedFileId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
