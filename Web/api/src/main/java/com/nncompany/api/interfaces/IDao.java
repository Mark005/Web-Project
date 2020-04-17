package com.nncompany.api.interfaces;

import java.util.List;

public interface IDao<T> {

    T get(int id);

    List<T> getAll();

    List<T> getWithPagination(Integer offset, Integer limit);

    void save(T t);

    void update(T t);

    void delete(T t);


}
