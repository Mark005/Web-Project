package com.nncompany.impl.store;

import com.nncompany.api.interfaces.IUserCredsStore;
import com.nncompany.api.model.UserCreds;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserCredsStore extends AbstractDao<UserCreds> implements IUserCredsStore {

    @Autowired
    private SessionFactory sessionFactory;

    public UserCredsStore(){
        super(UserCreds.class);
    }

    @Override
    public UserCreds getByLogin(String login, String pass){
        Query query = sessionFactory.getCurrentSession().createQuery("from UserCreds u where u.login =:log and u.pass =:pas");
        query.setParameter("log", login);
        query.setParameter("pas", pass);
        query.setMaxResults(1);
        UserCreds product = (UserCreds) query.uniqueResult();
        return product;
    }
}
