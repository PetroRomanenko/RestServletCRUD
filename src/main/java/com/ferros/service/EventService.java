package com.ferros.service;

import com.ferros.model.Event;
import com.ferros.repository.EventRepository;
import com.ferros.repository.FileRepository;
import com.ferros.repository.UserRepository;
import com.ferros.repository.hibernate.HibernateEventRepositoryImpl;
import com.ferros.repository.hibernate.HibernateFileRepositoryImpl;
import com.ferros.repository.hibernate.HibernateUserRepositoryImpl;

public class EventService {
    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserRepository userRepository = new HibernateUserRepositoryImpl();
    private final EventRepository eventRepository = new HibernateEventRepositoryImpl();

    public void save(Event eventToCreate) {

        eventRepository.save(eventToCreate);
    }
}
