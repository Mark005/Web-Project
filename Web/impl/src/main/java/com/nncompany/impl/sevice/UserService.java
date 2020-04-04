package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.IUserService;
import com.nncompany.api.interfaces.IUserStore;
import com.nncompany.api.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private IUserStore userStore;

    @Override
    @Transactional
    public User get(int id) {
        return userStore.get(id);
    }

    @Override
    @Transactional
    public List<User> getAll() {
        return null;
    }

    @Override
    @Transactional
    public void save(User user) {

    }

    @Override
    @Transactional
    public void update(User user) {

    }

    @Override
    @Transactional
    public void delete(User user) {

    }
}
