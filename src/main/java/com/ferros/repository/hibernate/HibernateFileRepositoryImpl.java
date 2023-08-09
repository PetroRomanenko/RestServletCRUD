package com.ferros.repository.hibernate;

import com.ferros.model.File;

import com.ferros.repository.FileRepository;
import com.ferros.utils.HibernateUtil;
import jakarta.persistence.NoResultException;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {

    @Override
    public File getById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT f FROM File f WHERE f.id = :id", File.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<File> getAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("SELECT f FROM File f ", File.class)
                    .getResultList();
        }
    }

    @Override
    public File save(File file) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.save(file);
            session.getTransaction().commit();
            return file;
        }
    }

    @Override
    public File update(File file) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.merge(file);
            session.getTransaction().commit();
            return file;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (var session = HibernateUtil.openSession()) {
            session.beginTransaction();
            session.remove(session.get(File.class, id));
            session.getTransaction().commit();
        }

    }
}
