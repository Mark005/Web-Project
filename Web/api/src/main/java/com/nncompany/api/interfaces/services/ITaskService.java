package com.nncompany.api.interfaces.services;

import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;

import java.util.List;

public interface ITaskService {

    Task get(int id);

    List<Task> getAll();

    List<Task> getWithPagination(Integer offset, Integer limit);

    List<Task> getUsersTasks(User user, TaskSatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetUsersTasks(User user, TaskSatus taskSatus, TaskType taskType);

    List<Task> getAll(Integer page, Integer pageSize, TaskSatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetAll(TaskSatus taskSatus, TaskType taskType);

    void save(Task task);

    void update(Task task);

    void delete(Task task);
}
