package sevice;

import com.nncompany.api.interfaces.IUserService;
import com.nncompany.api.interfaces.IUserStore;
import com.nncompany.api.model.User;
import com.nncompany.di.Di;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import util.HibernateManager;

import java.util.List;

public class UserService implements IUserService {

    private final Logger log = Logger.getLogger(this.getClass());
    private IUserStore userStore = Di.getInstance().load(IUserStore.class);

    @Override
    public User get(int id) {
        User user = null;
        try (Session session = HibernateManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            user = userStore.get(session, id);
            session.getTransaction().commit();
            return user;
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
