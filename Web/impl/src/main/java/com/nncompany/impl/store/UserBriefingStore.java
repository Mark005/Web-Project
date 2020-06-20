package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.IUserBriefingStore;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserBriefingStore extends AbstractDao<UserBriefing> implements IUserBriefingStore {

    @Autowired
    private SessionFactory sessionFactory;

    public UserBriefingStore(){
        super(UserBriefing.class);
    }

    @Override
    public List<Briefing> getBriefingsByCurrentUser(User user) {
        Query query = sessionFactory.getCurrentSession().createQuery("from UserBriefing u where u.user =:user");
        query.setParameter("user", user);
        return query.list();
    }

    @Override
    public List<User> getUsersByCurrentBriefing(Briefing briefing) {
        Query query = sessionFactory.getCurrentSession().createQuery("from UserBriefing u where u.briefing =:briefing");
        query.setParameter("briefing", briefing);
        return query.list();
    }

    @Override
    public List<UserBriefing> getAll(Integer page, Integer pageSize, UserBriefingSort sort, Direction direction) {
        String hql = "FROM UserBriefing u ORDER BY u." + sort.getTitle() + " " + direction;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setFirstResult(page*pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }
}
