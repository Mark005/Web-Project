package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.IUserCredsService;
import com.nncompany.api.interfaces.IUserCredsStore;
import com.nncompany.api.model.UserCreds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCredsService implements IUserCredsService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private IUserCredsStore userCredsStore;

    @Override
    @Transactional
    public UserCreds get(int id) {
        return userCredsStore.get(id);
    }

    @Override
    @Transactional
    public UserCreds getUserCredsByLogin(String login, String pass){
        return userCredsStore.getByLogin(login, pass);
    }

    @Override
    @Transactional
    public List<UserCreds> getAll() {
        return userCredsStore.getAll();
    }

}
