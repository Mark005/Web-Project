package com.nncompany.impl.sevice;


import com.nncompany.api.interfaces.IUserCredsService;
import com.nncompany.api.interfaces.IUserCredsStore;
import com.nncompany.api.model.UserCreds;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nncompany.impl.util.HibernateManager;

import java.util.List;

@Component
public class UserCredsService implements IUserCredsService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    HibernateManager hibernateManager;

    @Autowired
    private IUserCredsStore userCredsStore;

    @Override
    public UserCreds get(int id) {
        UserCreds userCreds = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            userCreds = userCredsStore.get(session, id);
            session.getTransaction().commit();
            return userCreds;
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return userCreds;
    }

    @Override
    public UserCreds getUserCredsByLogin(String login, String pass){
        UserCreds userCreds = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            userCreds = userCredsStore.getByLogin(session, login, pass);
            session.getTransaction().commit();
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return userCreds;
    }

    @Override
    public List<UserCreds> getAll() {
        List<UserCreds> userCreds = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            userCreds = userCredsStore.getAll(session);
            session.getTransaction().commit();
            return userCreds;
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return userCreds;
    }

}
