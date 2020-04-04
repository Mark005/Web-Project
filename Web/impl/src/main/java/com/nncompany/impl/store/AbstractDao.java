package com.nncompany.impl.store;

import com.nncompany.api.interfaces.IDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract class AbstractDao<T> implements IDao<T>{
    Class<T> entityClass;

    @Autowired
    private SessionFactory sessionFactory;

    protected AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T get(int id) {
        return (T) sessionFactory.getCurrentSession().get(entityClass, id);
    }

    @Override
    public List<T> getAll(){
        List<T> autorepairs;
        autorepairs = sessionFactory.getCurrentSession().createQuery("from" + entityClass.getCanonicalName(), entityClass).list();
        return autorepairs;
    }

    @Override
    public void save(T t){
        sessionFactory.getCurrentSession().save(t);
    }

    @Override
    public void update(T t){
        sessionFactory.getCurrentSession().update(t);
    }

    @Override
    public void delete(T t){
        sessionFactory.getCurrentSession().delete(t);
    }
}
