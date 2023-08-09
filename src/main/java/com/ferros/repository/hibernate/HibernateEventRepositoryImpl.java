package com.ferros.repository.hibernate;

import com.ferros.model.Event;
import com.ferros.repository.EventRepository;
import com.ferros.utils.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.Hibernate;

import java.util.List;

public class HibernateEventRepositoryImpl implements EventRepository {

    @Override
    public Event getById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT e FROM Event e LEFT JOIN FETCH e.file WHERE e.id = :id", Event.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Event> getAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT e FROM Event e LEFT JOIN FETCH e.file ", Event.class)
                    .getResultList();
        }
    }

    @Override
    public Event save(Event event) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.save(event);
            session.getTransaction().commit();
            return event;
        }
    }

    @Override
    public Event update(Event event) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            Hibernate.initialize(event.getFile());
            session.merge(event);
            session.getTransaction().commit();
            return event;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.remove(session.get(Event.class, id));
            session.getTransaction().commit();
        }
    }
}
