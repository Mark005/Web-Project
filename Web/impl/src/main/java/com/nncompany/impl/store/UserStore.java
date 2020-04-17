package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.IUserStore;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserStore extends AbstractDao<User> implements IUserStore {

    public UserStore(){
        super(User.class);
    }

}
