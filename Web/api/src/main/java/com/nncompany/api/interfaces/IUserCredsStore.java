package com.nncompany.api.interfaces;

import com.nncompany.api.model.UserCreds;
import org.hibernate.Session;

public interface IUserCredsStore extends IDao<UserCreds>{
    public UserCreds getByLogin(Session session, String login, String pass);
}
