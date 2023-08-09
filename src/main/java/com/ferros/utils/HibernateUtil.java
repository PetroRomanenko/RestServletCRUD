package com.ferros.utils;

import com.ferros.model.Event;
import com.ferros.model.File;
import com.ferros.model.User;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


@UtilityClass
public class HibernateUtil {
    private Session session;

    private Transaction transaction;

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            Configuration configuration = buildConfiguration();
            configuration.configure();

            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    private static Configuration buildConfiguration() {
        Configuration configuration =new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Event.class);
        configuration.addAnnotatedClass(File.class);
        return configuration;
    }

    public static Session openSession(){
        return getSessionFactory().openSession();
    }

    public Session getSession(){
        return session;
    }

    public Transaction getTransaction(){
        return transaction;
    }

    public Session openTransactionSession(){
        session=openSession();
        transaction=session.getTransaction();
        return session;
    }

    private void closeSession() {
        session.close();
    }

    public void closeTransactionSession() {
        transaction.commit();
        closeSession();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }


}
