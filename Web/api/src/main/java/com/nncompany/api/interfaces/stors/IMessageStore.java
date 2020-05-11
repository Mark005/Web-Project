package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;

import java.util.List;

public interface IMessageStore extends IDao<Message> {

    Integer getTotalCountMessages(User userOne, User userTwo);

    List<Message> getDialogWithPagination(User userOne, User userTwo, Integer page, Integer pageSize);

    List<Message> getChatWithPagination(Integer page, Integer pageSize);
}
