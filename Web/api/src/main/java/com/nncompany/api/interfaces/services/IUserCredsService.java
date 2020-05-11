package com.nncompany.api.interfaces.services;

import com.nncompany.api.model.entities.UserCreds;

import java.util.List;

public interface IUserCredsService {

    UserCreds get(int id);

    List<UserCreds> getAll();

    void save(UserCreds userCreds);

    UserCreds getUserCredsByLoginAndPass(String login, String pass);

    Boolean checkLogin(String login);

    void update(UserCreds userCreds);
}
