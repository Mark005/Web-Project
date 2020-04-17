package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.IMessageStore;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageStore extends AbstractDao<Message> implements IMessageStore {

    @Autowired
    private SessionFactory sessionFactory;

    public MessageStore() {
        super(Message.class);
    }

    @Override
    public List<Message> getDialogWithPagination(User userOne, User userTwo, Integer offset, Integer limit) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Message m " +
                                                                        "where (m.userFrom =:userOne and m.userTo =: userTwo) " +
                                                                        "or (m.userFrom =:userTwo and m.userTo =: userOne) " +
                                                                        "ORDER BY m.date");
        query.setParameter("userOne", userOne);
        query.setParameter("userTwo", userTwo);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public List<Message> getChatWithPagination(Integer offset, Integer limit) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Message m " +
                "where m.userTo is null " +
                "ORDER BY m.date");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }
}
