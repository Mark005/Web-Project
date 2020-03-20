package com.nncompany.api.interfaces;

import com.nncompany.api.model.User;
import org.hibernate.Session;

import java.util.List;

public interface IUserService {

    User get(int id);

    List<User> getAll();

    void save(User user);

    void update(User user);

    void delete(User user);
}
