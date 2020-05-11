package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskStatus;
import com.nncompany.api.model.enums.TaskType;

import java.util.List;

public interface ITaskStore extends IDao<Task> {

    List<Task> getUsersTasks(User user, TaskStatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetUsersTasks(User user, TaskStatus taskSatus, TaskType taskType);

    List<Task> getAll(Integer page, Integer pageSize,TaskStatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetAll(TaskStatus taskSatus, TaskType taskType);
}
