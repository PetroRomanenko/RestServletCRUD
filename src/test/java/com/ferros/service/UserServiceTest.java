package com.ferros.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ferros.model.User;
import com.ferros.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
        userRepository = mock(UserRepository.class);
        userService.setUserRepository(userRepository);
    }

    @Test
    public void testGetUserById() {
        Integer userId = 1;
        User expectedUser = new User();
        when(userRepository.getById(userId)).thenReturn(expectedUser);

        User resultUser = userService.getById(userId);

        verify(userRepository, times(1)).getById(userId);
        assertEquals(expectedUser, resultUser);
    }

    @Test
    public void testSaveUser() {
        User userToSave = new User();
        User savedUser = new User();
        when(userRepository.save(userToSave)).thenReturn(savedUser);

        User resultUser = userService.saveUser(userToSave);

        verify(userRepository, times(1)).save(userToSave);
        assertEquals(savedUser, resultUser);
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        when(userRepository.getAll()).thenReturn(userList);

        List<User> resultUserList = userService.getAllUsers();

        verify(userRepository, times(1)).getAll();
        assertEquals(userList, resultUserList);
    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setName("TestUser");

        when(userRepository.save(any())).thenReturn(newUser);

        User resultUser = userService.createUser(newUser);

        verify(userRepository, times(1)).save(newUser);
        assertNotNull(resultUser.getEvents());
    }

    @Test
    public void testUpdateUser() {
        Integer userId = 1;
        User updatedUser = new User();
        when(userRepository.update(any())).thenReturn(updatedUser);

        User resultUser = userService.updateUser(userId, updatedUser);

        verify(userRepository, times(1)).update(updatedUser);
        assertEquals(updatedUser, resultUser);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 1;
        User deletedUser = new User();
        when(userService.getById(userId)).thenReturn(deletedUser);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
