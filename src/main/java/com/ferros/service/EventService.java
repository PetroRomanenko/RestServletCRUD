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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventService {
    private  FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private  UserRepository userRepository = new HibernateUserRepositoryImpl();
    private  EventRepository eventRepository = new HibernateEventRepositoryImpl();

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

    public Event upateEvent(Integer eventId, Integer userId, Integer fileId) {
        User user = userRepository.getById(userId);
        File file = fileRepository.getById(fileId);
        Event event = new Event(eventId, user, file);
        return eventRepository.update(event);
    }

    public void deleteUser(Integer deletedEventId) {
        eventRepository.deleteById(deletedEventId);
    }
}
