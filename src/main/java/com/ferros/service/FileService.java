package com.ferros.service;

import com.ferros.model.File;
import com.ferros.model.Event;
import com.ferros.model.User;
import com.ferros.repository.FileRepository;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import lombok.Data;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Data
public class FileService {

    private  FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private  UserService userService = new UserService();
    private  EventService eventService = new EventService();
    private final String UPLOAD_DIRECTORY = "src/main/resources/files";


    public File getFile(Integer fileId) {
        return fileRepository.getById(fileId);
    }

    public File upateFile(Integer fileId, String newFileName) {
        File fileToUpdate = fileRepository.getById(fileId);

        File upatedFile = changeFileName(fileToUpdate, newFileName);
        upatedFile.setId(fileId);

        return fileRepository.update(upatedFile);
    }

    private File changeFileName(File fileToUpdate, String newFileName) {


        // Создание объекта File для старого файла
        java.io.File oldFile = new java.io.File(fileToUpdate.getFilePath());

        // Получение пути к директории файла
        String parentDirectory = oldFile.getParent();

        // Создание объекта File для нового файла
        java.io.File newFile = new java.io.File(parentDirectory, newFileName);

        // Попробуйте переименовать файл
        if (oldFile.exists()) {
            boolean success = oldFile.renameTo(newFile);
            if (success) {
                System.out.println("Файл успешно переименован");
            } else {
                System.out.println("Не удалось переименовать файл");
            }
        } else {
            System.out.println("Старый файл не существует");
        }
        return new File(null, newFileName, newFile.getPath());
    }


    public File saveFileToDB(File file) {
        if (file != null) {
            return fileRepository.save(file);
        } else return null;
    }


    public File fileUploadService(Part filePart, Integer userId) throws ServletException, IOException {

        String fileName = getSubmittedFileName(filePart);
        String filePath = UPLOAD_DIRECTORY + java.io.File.separator + fileName;

        java.io.File uploadDir = new java.io.File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try (var fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, new java.io.File(filePath).toPath());
        }

        //get user from DB
        User user = userService.getById(userId);

        //Create new File
        File fileToSave = new File();
        fileToSave.setName(fileName);
        fileToSave.setFilePath(filePath);

        //Save file to DB
        File createdFile = saveFileToDB(fileToSave);
        //Save Event to DB
        eventService.createUploadEvent(user, fileToSave);

        return createdFile;
    }

    private String getSubmittedFileName(Part part) {
        if (part != null) {
            String header = part.getHeader("content-disposition");

            String[] parts = header.split(";");
            for (String cd : parts) {
                if (cd.trim().startsWith("filename")) {
                    return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        }
        return null;
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


    public void deleteFile(Integer deletedFileId) {
        java.io.File fileToDelete = new java.io.File(fileRepository.getById(deletedFileId).getFilePath());

        fileToDelete.delete();

        fileRepository.deleteById(deletedFileId);
    }
}
