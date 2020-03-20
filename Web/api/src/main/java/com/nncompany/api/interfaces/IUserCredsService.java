package com.nncompany.api.interfaces;

import com.nncompany.api.model.UserCreds;

import java.util.List;

public interface IUserCredsService {

    UserCreds get(int id);

    UserCreds getUserCredsByLogin(String login, String pass);

    List<UserCreds> getAll();
}
