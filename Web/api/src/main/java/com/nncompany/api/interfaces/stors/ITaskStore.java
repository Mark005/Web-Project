package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;

import java.util.List;

public interface ITaskStore extends IDao<Task> {

    List<Task> getUsersTasks(User user, TaskSatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetUsersTasks(User user, TaskSatus taskSatus, TaskType taskType);

    List<Task> getAll(Integer page, Integer pageSize,TaskSatus taskSatus, TaskType taskType);

    Integer getTotalCountForGetAll(TaskSatus taskSatus, TaskType taskType);
}
