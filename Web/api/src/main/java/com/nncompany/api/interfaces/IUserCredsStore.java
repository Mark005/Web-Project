package com.nncompany.api.interfaces;

import com.nncompany.api.model.UserCreds;


public interface IUserCredsStore extends IDao<UserCreds>{
    public UserCreds getByLogin(String login, String pass);
}
