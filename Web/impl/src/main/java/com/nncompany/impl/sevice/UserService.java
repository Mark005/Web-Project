package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.IUserService;
import com.nncompany.api.interfaces.IUserStore;
import com.nncompany.api.model.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import com.nncompany.impl.util.HibernateManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService implements IUserService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    HibernateManager hibernateManager;

    @Autowired
    private IUserStore userStore;

    @Override
    public User get(int id) {
        User user = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            user = userStore.get(session, id);
            session.getTransaction().commit();
            return user;
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
