package com.nncompany.api.interfaces.services;

import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;

import java.util.List;

public interface IMessageService {

    Message get(int id);

    List<Message> getAll();

    Integer getTotalCountMessagesInDialog(User userOne, User userTwo);

    List<Message> getWithPagination(Integer page, Integer pageSize);

    List<Message> getDialogWithPagination(User userOne, User userTwo, Integer page, Integer pageSize);

    List<Message> getChatWithPagination(Integer page, Integer pageSize);

    void save(Message message);

    void update(Message message);

    void delete(Message message);
}
