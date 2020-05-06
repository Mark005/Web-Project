package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.IUserStore;
import com.nncompany.api.model.entities.User;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserStore extends AbstractDao<User> implements IUserStore {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    public UserStore(){
        super(User.class);
    }

    @Override
    public List<User> search(String searchString) {
        org.apache.lucene.search.Query query = getQueryBuilder().simpleQueryString()
                                       .onFields("name", "surname")
                                       .matching(searchString + "*")
                                       .createQuery();
        return getJpaQuery(query).getResultList();
    }

    @Override
    public List<User> search(Integer searchString) {
        Query qry = sessionFactory.getCurrentSession().createQuery("from User u where str(u.certificateNumber)  like :num");
        qry.setParameter("num", "%" + searchString + "%");
        return qry.getResultList();
    }

    private QueryBuilder getQueryBuilder() {
        entityManager = sessionFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        return fullTextEntityManager.getSearchFactory()
                                    .buildQueryBuilder()
                                    .forEntity(User.class)
                                    .get();
    }

     private FullTextQuery getJpaQuery(org.apache.lucene.search.Query luceneQuery) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            return fullTextEntityManager.createFullTextQuery(luceneQuery, User.class);
    }
}
