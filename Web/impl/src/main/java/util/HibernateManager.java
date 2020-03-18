package util;

import com.nncompany.api.model.User;
import com.nncompany.api.model.UserCreds;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateManager  {

    private static SessionFactory sessionFactory;

    private HibernateManager() {}

    public static SessionFactory getSessionFactory() throws Exception {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                //configuration.addAnnotatedClass(User.class);
                //configuration.addAnnotatedClass(UserCreds.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                builder.build();
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
throw new Exception(e.getMessage());
                //System.out.println("SessionFactory ERROR: " + e.getMessage());
            }
        }
        return sessionFactory;
    }
}
