package com.ferros.service;

import com.ferros.model.File;
import com.ferros.model.Event;
import com.ferros.model.User;
import com.ferros.repository.FileRepository;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.*;

public class FileService {

    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();
    //TODO Спросить у Жени какой тут путь прописывать для загрузки с интернета
    private final String UPLOAD_DIRECTORY = "src/main/resources/files";

    public com.ferros.model.File getFile(Integer fileId){
//        Integer fileId = request.getIntHeader("file_id");
        return fileRepository.getById(fileId);
    }
    public void deleteFile(HttpServletRequest request){
        Integer fileId = request.getIntHeader("file_id");
//        userService.deleteById(fileId);

    }
    public void saveFileToDB(File file){
        if (file!=null)
        fileRepository.save(file);
    }
    public File uploadFile(HttpServletRequest request) throws ServletException, IOException {

        //TODO: upload physical file
        physicalUploadFile(request);

        Integer userId = request.getIntHeader("user_id");
        User user = userService.getById(userId);

        String fileName =request.getHeader("file_name");

        com.ferros.model.File fileToSave = new File();
        fileToSave.setName(fileName);
        fileToSave.setFilePath(UPLOAD_DIRECTORY+fileName);

        File createdFile = fileRepository.save(fileToSave);


        Event eventToCreate = new Event();
        eventToCreate.setUser(user);
        eventToCreate.setFile(createdFile);
        eventService.save(eventToCreate);
        return createdFile;
    }

    private void physicalUploadFile(HttpServletRequest request) throws IOException, ServletException {
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
    }


}
