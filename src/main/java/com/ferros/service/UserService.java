package com.ferros.service;

import com.ferros.model.User;
import com.ferros.repository.EventRepository;
import com.ferros.repository.FileRepository;
import com.ferros.repository.UserRepository;
import com.ferros.repository.hibernate.HibernateEventRepositoryImpl;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import com.ferros.repository.hibernate.HibernateUserRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

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

    public User createUser(HttpServletRequest req, User user) {
        user.setEvents(new ArrayList<>());
        User newSavedUser = saveUser(user);

        return newSavedUser;
    }
}
