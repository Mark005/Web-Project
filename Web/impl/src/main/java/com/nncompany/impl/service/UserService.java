package com.nncompany.impl.service;

import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.interfaces.stors.IUserStore;
import com.nncompany.api.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserStore userStore;

    @Override
    public User get(int id) {
        return userStore.get(id);
    }

    @Override
    public List<User> getAll() {
        return userStore.getAll();
    }

    @Override
    public List<User> getWithPagination(Integer page, Integer pageSize) {
        return userStore.getWithPagination(page, pageSize);
    }

    @Override
    public Integer getTotalCount() {
        return userStore.getTotalCount();
    }

    @Override
    public void save(User user) {
        userStore.save(user);
    }

    @Override
    public void update(User user) {
        userStore.update(user);
    }

    @Override
    public void delete(User user) {
        userStore.delete(user);
    }
}
