package com.nncompany.api.interfaces.services;

import com.nncompany.api.model.entities.User;

import java.util.List;

public interface IUserService {

    User get(int id);

    List<User> getAll();

    List<User> getWithPagination(Integer page, Integer pageSize);

    Integer getTotalCount();

    void save(User user);

    void update(User user);

    void delete(User user);
}
