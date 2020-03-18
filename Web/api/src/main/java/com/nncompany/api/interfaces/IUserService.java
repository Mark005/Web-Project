package com.nncompany.api.interfaces;

import com.nncompany.api.model.User;
import org.hibernate.Session;

import java.util.List;

public interface IUserService {

    User get(Session session, int id);

    List<User> getAll(Session session);

    void save(Session session, User user);

    void update(Session session, User user);

    void delete(Session session, User user);
}
