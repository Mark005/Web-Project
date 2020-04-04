package com.nncompany.impl.store;

import com.nncompany.api.interfaces.IUserStore;
import com.nncompany.api.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserStore extends AbstractDao<User> implements IUserStore {
    public UserStore(){
        super(User.class);
    }
}
