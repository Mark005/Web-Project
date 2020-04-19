package com.nncompany.impl.store;

import com.nncompany.api.interfaces.stors.ITaskStore;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskStore extends AbstractDao<Task> implements ITaskStore {

    @Autowired
    private SessionFactory sessionFactory;

    public TaskStore() {
        super (Task.class);
    }

    @Override
    public List<Task> getUsersTasks(User user, TaskSatus taskSatus, TaskType taskType) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Task t " +
                                                                        "where t.executor =:user " +
                                                                        "and t.status =: status " +
                                                                        "and t.type =: type");
        query.setParameter("user", user);
        query.setParameter("status", taskSatus);
        query.setParameter("type", taskType);
        return query.list();
    }

    @Override
    public Integer getTotalCountForGetUsersTasks(User user, TaskSatus taskSatus, TaskType taskType){
        Query query = sessionFactory.getCurrentSession().createQuery("Select count (t.id)from Task t " +
                                                                                            "where t.executor =:user " +
                                                                                            "and t.status =: status " +
                                                                                            "and t.type =: type");
        query.setParameter("user", user);
        query.setParameter("status", taskSatus);
        query.setParameter("type", taskType);
        return ((Long) query.uniqueResult()).intValue();
    }

    @Override
    public List<Task> getAll(Integer page, Integer pageSize, TaskSatus taskSatus, TaskType taskType) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Task t " +
                                                                        "where  t.status =: status " +
                                                                        "and t.type =: type");
        query.setParameter("status", taskSatus);
        query.setParameter("type", taskType);
        query.setFirstResult(page*pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Override
    public Integer getTotalCountForGetAll(TaskSatus taskSatus, TaskType taskType) {
        Query query = sessionFactory.getCurrentSession().createQuery("Select count (t.id) from Task t " +
                                                                                            "where  t.status =: status " +
                                                                                            "and t.type =: type");
        query.setParameter("status", taskSatus);
        query.setParameter("type", taskType);
        return ((Long) query.uniqueResult()).intValue();
    }
}
