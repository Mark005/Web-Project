package com.nncompany.impl.service;

import com.nncompany.api.interfaces.services.ITaskService;
import com.nncompany.api.interfaces.stors.ITaskStore;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskStatus;
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
    public List<Task> getUsersTasks(User user, TaskStatus taskStatus, TaskType taskType) {
        return taskStore.getUsersTasks(user, taskStatus, taskType);
    }

    @Override
    public Integer getTotalCountForGetUsersTasks(User user, TaskStatus taskStatus, TaskType taskType) {
        return taskStore.getTotalCountForGetUsersTasks(user, taskStatus, taskType);
    }

    @Override
    public List<Task> getAll(Integer page, Integer pageSize,TaskStatus taskStatus, TaskType taskType) {
        return taskStore.getAll(page, pageSize, taskStatus, taskType);
    }

    @Override
    public Integer getTotalCountForGetAll(TaskStatus taskStatus, TaskType taskType) {
        return taskStore.getTotalCountForGetAll(taskStatus, taskType);
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
