package com.ferros.service;

import com.ferros.model.File;
import com.ferros.model.Event;
import com.ferros.model.User;
import com.ferros.repository.FileRepository;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.io.*;
import java.util.List;

public class FileService {

    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();
    private static final String USER = "user";

    private final String UPLOAD_DIRECTORY = "src/main/resources/files";
    private final String DEFAULT_FILENAME = "DefaultFile";
    public File getFile(Integer fileId){
//        Integer fileId = request.getIntHeader("file_id");
        return fileRepository.getById(fileId);
    }

    public File upateFile(File file){
        return fileRepository.update(file);
    }
    public void deleteFile(HttpServletRequest request){
        Integer fileId = request.getIntHeader("file_id");
//        userService.deleteById(fileId);

    }
    public File saveFileToDB(File file){
        if (file!=null) {
            return fileRepository.save(file);
        }
        else return null;
    }
    public Event createDownloadEvent(HttpServletRequest req, File file) {
        //Receive session and get current user
        var session = req.getSession();
        User currentUser = (User)session.getAttribute(USER);
        //creating new event
        Event newDownloadEvent = new Event();
        newDownloadEvent.setFile(file);
        newDownloadEvent.setUser(currentUser);
        //save event to db
        return eventService.save(newDownloadEvent);
    }
    public File uploadFile(HttpServletRequest request) throws ServletException, IOException {

        //TODO: upload physical file
//        String fileName = physicalUploadFile(request);

        String uploadPath = request.getServletContext().getRealPath("") + java.io.File.separator + UPLOAD_DIRECTORY;
        java.io.File uploadDir = new java.io.File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        for (Part part : request.getParts()) {
            String fileName = getFileName(part);
            part.write(uploadPath + java.io.File.separator + fileName);
        }


//        File fileToSave = new File();
//        fileToSave.setName(fileName);
//        fileToSave.setFilePath(UPLOAD_DIRECTORY+fileName);

//        File createdFile =saveFileToDB(fileToSave);
//
//        Event UploadEvent = createDownloadEvent(request, fileToSave);
//
        return null;
    }
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return DEFAULT_FILENAME;
    }

    private String physicalUploadFile(HttpServletRequest request) throws IOException, ServletException {

        Part filePart = request.getPart("file");
        String fileName = null;
        for (String content : filePart.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                break;
            }
        }


        OutputStream outputStream = null;
        InputStream fileContent = null;
        try {
            java.io.File file = new java.io.File(UPLOAD_DIRECTORY + java.io.File.separator + fileName);
            outputStream = new FileOutputStream(file);
            fileContent = filePart.getInputStream();

            int read;
            byte[] buffer = new byte[1024];
            while ((read = fileContent.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (fileContent != null) {
                fileContent.close();
            }
        }
        return fileName;
    }


    public List<File> getAllFiles() {
       return fileRepository.getAll();
    }
}
