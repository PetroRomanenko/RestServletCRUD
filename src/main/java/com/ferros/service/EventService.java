package com.ferros.service;

import com.ferros.model.Event;
import com.ferros.model.File;
import com.ferros.model.User;
import com.ferros.repository.EventRepository;
import com.ferros.repository.FileRepository;
import com.ferros.repository.UserRepository;
import com.ferros.repository.hibernate.HibernateEventRepositoryImpl;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import com.ferros.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.List;

public class EventService {
    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserRepository userRepository = new HibernateUserRepositoryImpl();
    private final EventRepository eventRepository = new HibernateEventRepositoryImpl();

    public Event save(Event eventToCreate) {

        eventRepository.save(eventToCreate);
        return eventToCreate;
    }

    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    public Event getById(Integer eventId) {
        return eventRepository.getById(eventId);
    }

    public Event createUploadEvent(User user, File fileToSave) {
        Event newUploadEvent = new Event();
        newUploadEvent.setUser(user);
        newUploadEvent.setFile(fileToSave);

        eventRepository.save(newUploadEvent);
        return newUploadEvent;
    }
}
