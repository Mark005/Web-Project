package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.User;

import java.util.List;


public interface IUserStore extends IDao<User> {

    List<User> search(String searchString);

    List<User> search(Integer searchString);
}
