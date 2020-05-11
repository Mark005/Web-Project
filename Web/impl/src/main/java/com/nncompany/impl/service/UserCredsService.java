package com.nncompany.impl.service;

import com.nncompany.api.interfaces.services.IUserCredsService;
import com.nncompany.api.interfaces.stors.IUserCredsStore;
import com.nncompany.api.model.entities.UserCreds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserCredsService implements IUserCredsService {

    @Autowired
    private IUserCredsStore userCredsStore;

    @Override
    public UserCreds get(int id) {
        return userCredsStore.get(id);
    }

    @Override
    public List<UserCreds> getAll() {
        return userCredsStore.getAll();
    }

    @Override
    public UserCreds getUserCredsByLoginAndPass(String login, String pass){
        return userCredsStore.getByLoginAndPass(login, pass);
    }

    @Override
    public Boolean checkLogin(String login){
        return userCredsStore.checkLogin(login);
    }

    @Override
    public void save(UserCreds userCreds) {
        userCredsStore.save(userCreds);
    }

    @Override
    public void update(UserCreds userCreds) {
        userCredsStore.update(userCreds);
    }
}
