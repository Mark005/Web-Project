package com.nncompany.impl.store;

import com.nncompany.api.interfaces.IDao;
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
    public Integer getTotalCount() {
        String countQ = "Select count (f.id) from " + entityClass.getName() + " f";
        Query countQuery = sessionFactory.getCurrentSession().createQuery(countQ);
        return ((Long) countQuery.uniqueResult()).intValue();
    }

    @Override
    public List<T> getAll(){
        return sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName()).list();
    }

    @Override
    public List<T> getWithPagination(Integer page, Integer pageSize){
        Query query = sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName());
        query.setFirstResult(page*pageSize);
        query.setMaxResults(pageSize);
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
