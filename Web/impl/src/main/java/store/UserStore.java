package store;

import com.nncompany.api.model.User;
import org.hibernate.Session;

public class UserStore extends AbstractDao{
    private UserStore(){
        super(User.class);
    }
}
