package com.ferros.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ferros.model.File;
import com.ferros.model.User;
import com.ferros.repository.FileRepository;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileServiceTest {

    private FileService fileService;
    private FileRepository fileRepository;
    private UserService userService;
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        fileService = new FileService();
        fileRepository = mock(FileRepository.class);
        userService = mock(UserService.class);
        eventService = mock(EventService.class);
        fileService.setFileRepository(fileRepository);
        fileService.setUserService(userService);
        fileService.setEventService(eventService);
    }

//    @Test

    public void testFileUploadService() throws ServletException, IOException {
        Part filePart = mock(Part.class);
        Integer userId = 1;

        String fileName = "testFile.txt";
        String filePath = "src/main/resources/files/" + fileName;

        User user = new User();
        when(userService.getById(userId)).thenReturn(user);

        File savedFile = new File();
        savedFile.setId(1);
        when(fileRepository.save(any())).thenReturn(savedFile);

        fileService.fileUploadService(filePart, userId);

        verify(filePart, times(1)).getInputStream();
        verify(fileRepository, times(1)).save(any());
        verify(eventService, times(1)).createUploadEvent(user, savedFile);
    }

    @Test
    public void testGetAllFiles() {
        List<File> fileList = new ArrayList<>();
        when(fileRepository.getAll()).thenReturn(fileList);

        List<File> resultFileList = fileService.getAllFiles();

        verify(fileRepository, times(1)).getAll();
        assertEquals(fileList, resultFileList);
    }

    @Test
    public void testDeleteFile() {
        Integer deletedFileId = 1;
        File fileToDelete = new File();
        fileToDelete.setFilePath("src/main/resources/files/testFile.txt");
        when(fileRepository.getById(deletedFileId)).thenReturn(fileToDelete);

        fileService.deleteFile(deletedFileId);

        verify(fileRepository, times(1)).deleteById(deletedFileId);
    }

    @Test
    public void testGetFile() {
        Integer fileId = 1;
        File expectedFile = new File();
        when(fileRepository.getById(fileId)).thenReturn(expectedFile);

        File resultFile = fileService.getFile(fileId);

        verify(fileRepository, times(1)).getById(fileId);
        assertEquals(expectedFile, resultFile);
    }

//    @Test

    public void testUpdateFile() {
        Integer fileId = 1;
        String newFileName = "newTestFile.txt";

        File fileToUpdate = new File();
        when(fileRepository.getById(fileId)).thenReturn(fileToUpdate);

        File updatedFile = new File();
        when(fileRepository.update(any())).thenReturn(updatedFile);

        File resultFile = fileService.upateFile(fileId, newFileName);

        verify(fileRepository, times(1)).getById(fileId);
        verify(fileRepository, times(1)).update(any());
        assertEquals(updatedFile, resultFile);
    }

}