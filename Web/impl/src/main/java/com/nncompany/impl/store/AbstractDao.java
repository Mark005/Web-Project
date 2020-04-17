package com.nncompany.impl.store;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract class AbstractDao<T> implements IDao<T>{

    private Class<T> entityClass;

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
        return sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName()).list();
    }

    @Override
    public List<T> getWithPagination(Integer offset, Integer limit){
        Query query = sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName());
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
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
