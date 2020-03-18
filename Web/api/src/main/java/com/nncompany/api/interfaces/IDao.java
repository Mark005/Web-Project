package com.nncompany.api.interfaces;

import com.nncompany.api.exception.DBException;
import org.hibernate.Session;

import java.util.List;

public interface IDao<T> {

    T get(Session session, int id) throws DBException;

    List<T> getAll(Session session) throws DBException;

    void save(Session session, T t) throws DBException;

    void update(Session session, T t) throws DBException;

    void delete(Session session, T t) throws DBException;
}
