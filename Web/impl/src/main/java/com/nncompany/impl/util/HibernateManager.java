package com.nncompany.impl.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HibernateManager {

    private SessionFactory sessionFactory;
    private final Logger log = Logger.getLogger(this.getClass());

    private HibernateManager() {
    }

    public SessionFactory getSessionFactory() throws Exception {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                builder.build();
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
        return sessionFactory;
    }
}
