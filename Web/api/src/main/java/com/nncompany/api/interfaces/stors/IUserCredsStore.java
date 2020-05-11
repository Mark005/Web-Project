package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.UserCreds;


public interface IUserCredsStore extends IDao<UserCreds> {

    UserCreds getByLoginAndPass(String login, String pass);

    Boolean checkLogin(String login);
}
