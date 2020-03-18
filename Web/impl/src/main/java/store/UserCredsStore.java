package store;

import com.nncompany.api.interfaces.IUserCredsStore;
import com.nncompany.api.model.UserCreds;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class UserCredsStore extends AbstractDao<UserCreds> implements IUserCredsStore {

    public UserCredsStore(){
        super(UserCreds.class);
    }

    @Override
    public UserCreds getByLogin(Session session, String login, String pass){
        Query query = session.createQuery("from UserCreds u where u.login =:log and u.pass =:pas");
        query.setParameter("log", login);
        query.setParameter("pas", pass);
        query.setMaxResults(1);
        UserCreds product = (UserCreds) query.uniqueResult();
        return product;
    }
}
