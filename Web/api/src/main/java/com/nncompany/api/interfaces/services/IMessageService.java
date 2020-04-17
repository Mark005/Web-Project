package com.nncompany.api.interfaces.services;

import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;

import java.util.List;

public interface IMessageService {

    Message get(int id);

    List<Message> getAll();

    List<Message> getWithPagination(Integer offset, Integer limit);

    List<Message> getDialogWithPagination(User userOne, User userTwo, Integer offset, Integer limit);

    List<Message> getChatWithPagination(Integer offset, Integer limit);

    void save(Message message);

    void update(Message message);

    void delete(Message message);
}
