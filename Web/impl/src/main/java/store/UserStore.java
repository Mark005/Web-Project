package store;

import com.nncompany.api.interfaces.IUserStore;
import com.nncompany.api.model.User;

public class UserStore extends AbstractDao<User> implements IUserStore {
    public UserStore(){
        super(User.class);
    }
}
