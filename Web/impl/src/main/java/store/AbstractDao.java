package store;

import com.nncompany.api.exception.DBException;
import com.nncompany.api.interfaces.IDao;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

abstract class AbstractDao<T> implements IDao<T>{
    Class<T> entityClass;

    protected AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T get(Session session, int id) {
        return (T) session.get(entityClass, id);
    }

    @Override
    public List<T> getAll(Session session){
        List<T> autorepairs;
        autorepairs = session.createQuery("from" + entityClass.getCanonicalName(), entityClass).list();
        return autorepairs;
    }

    @Override
    public void save(Session session, T t){
        session.save(t);
    }

    @Override
    public void update(Session session, T t){
        session.update(t);
    }

    @Override
    public void delete(Session session, T t){
        session.delete(t);
    }
}
