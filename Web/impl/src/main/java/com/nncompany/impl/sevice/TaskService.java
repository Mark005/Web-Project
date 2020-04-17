package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.services.ITaskService;
import com.nncompany.api.interfaces.stors.ITaskStore;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService implements ITaskService {

    @Autowired
    ITaskStore taskStore;

    @Override
    public Task get(int id) {
        return taskStore.get(id);
    }

    @Override
    public List<Task> getAll() {
        return taskStore.getAll();
    }

    @Override
    public List<Task> getWithPagination(Integer offset, Integer limit) {
        return taskStore.getWithPagination(offset, limit);
    }

    @Override
    public List<Task> getUsersTasks(User user, TaskSatus taskSatus, TaskType taskType) {
        return taskStore.getUsersTasks(user, taskSatus, taskType);
    }

    @Override
    public List<Task> getTasks(TaskSatus taskSatus, TaskType taskType) {
        return taskStore.getTasks(taskSatus, taskType);
    }

    @Override
    public void save(Task task) {
        taskStore.save(task);
    }

    @Override
    public void update(Task task) {
        taskStore.update(task);
    }

    @Override
    public void delete(Task task) {
        taskStore.delete(task);
    }
}
