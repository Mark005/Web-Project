package com.nncompany.api.interfaces;

import com.nncompany.api.model.User;
import com.nncompany.api.model.UserCreds;
import org.hibernate.Session;

import java.util.List;

public interface IUserCredsService {

    UserCreds get(int id);

    UserCreds getUserCredsByLogin(String login, String pass);

    List<UserCreds> getAll();
}
