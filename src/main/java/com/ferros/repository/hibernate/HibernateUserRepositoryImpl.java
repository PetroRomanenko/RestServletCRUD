package com.ferros.repository.hibernate;

import com.ferros.model.User;
import com.ferros.repository.UserRepository;
import com.ferros.utils.HibernateUtil;
import jakarta.persistence.NoResultException;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {

    @Override
    public User getById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.events WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.events", User.class)
                    .getResultList();
        }
    }

    @Override
    public User save(User user) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User update(User user) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.remove(session.get(User.class, id));
            session.getTransaction().commit();
        }
    }
}