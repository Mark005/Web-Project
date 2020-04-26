package com.nncompany.impl.service;

import com.nncompany.api.interfaces.services.IMessageService;
import com.nncompany.api.interfaces.stors.IMessageStore;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService implements IMessageService {

    @Autowired
    IMessageStore messageStore;

    @Override
    public Message get(int id) {
        return messageStore.get(id);
    }

    @Override
    public List<Message> getAll() {
        return messageStore.getAll();
    }

    @Override
    public Integer getTotalCountMessages(User userOne, User userTwo){
        return messageStore.getTotalCountMessages(userOne, userTwo);
    }

    @Override
    public List<Message> getWithPagination(Integer page, Integer pageSize) {
        return messageStore.getWithPagination(page, pageSize);
    }

    @Override
    public List<Message> getDialogWithPagination(User userOne, User userTwo, Integer page, Integer pageSize) {
        return messageStore.getDialogWithPagination(userOne, userTwo, page, pageSize);
    }

    @Override
    public List<Message> getChatWithPagination(Integer offset, Integer limit) {
        return messageStore.getChatWithPagination(offset, limit);
    }

    @Override
    public void save(Message message) {
        messageStore.save(message);
    }

    @Override
    public void update(Message message) {
        messageStore.update(message);
    }

    @Override
    public void delete(Message message) {
        messageStore.delete(message);
    }
}
