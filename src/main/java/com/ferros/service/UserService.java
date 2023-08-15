package com.ferros.service;

import com.ferros.model.User;
import com.ferros.repository.EventRepository;
import com.ferros.repository.FileRepository;
import com.ferros.repository.UserRepository;
import com.ferros.repository.hibernate.HibernateEventRepositoryImpl;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import com.ferros.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserRepository userRepository = new HibernateUserRepositoryImpl();
    private final EventRepository eventRepository = new HibernateEventRepositoryImpl();
    public User getById(Integer userId) {
        return userRepository.getById(userId);
    }

    public User saveUser(User user){
        if (user!=null){
            return userRepository.save(user);
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public User createUser( User user) {
        user.setEvents(new ArrayList<>());

        return saveUser(user);
    }

    public User updateUser(Integer userId, User upatedUser) {
        return userRepository.update(upatedUser);
    }

    public void deleteUser(Integer userId){
        User deletedUser = getById(userId);
        if (deletedUser!= null){
            userRepository.deleteById(userId);
        }
    }
}
