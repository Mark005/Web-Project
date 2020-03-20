package util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.logging.ErrorManager;

public class HibernateManager {

    private static SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(SessionFactory.class);

    private HibernateManager() {
    }

    public static SessionFactory getSessionFactory() throws Exception {
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
