package com.nncompany.impl.sevice;

import com.nncompany.api.interfaces.services.IUserBriefingService;
import com.nncompany.api.interfaces.stors.IUserBriefingStore;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserBriefingService implements IUserBriefingService {

    @Autowired
    IUserBriefingStore userBriefingStore;

    @Override
    public UserBriefing get(int id) {
        return userBriefingStore.get(id);
    }

    @Override
    public List<Briefing> getBriefingsByCurrentUser(User user) {
        return userBriefingStore.getBriefingsByCurrentUser(user);
    }


    @Override
    public List<User> getUsersByCurrentBriefing(Briefing briefing) {
        return userBriefingStore.getUsersByCurrentBriefing(briefing);
    }

    @Override
    public List<UserBriefing> getAll() {
        return userBriefingStore.getAll();
    }

    @Override
    public Integer getTotalCount() {
        return userBriefingStore.getTotalCount();
    }

    @Override
    public List<UserBriefing> getAll(Integer page, Integer pageSize, UserBriefingSort sort, Direction direction) {
        return userBriefingStore.getAll(page, pageSize, sort, direction);
    }

    @Override
    public List<UserBriefing> getWithPagination(Integer offset, Integer limit) {
        return userBriefingStore.getWithPagination(offset, limit);
    }

    @Override
    public void save(UserBriefing userBriefing) {
        userBriefingStore.save(userBriefing);
    }

    @Override
    public void update(UserBriefing userBriefing) {
        userBriefingStore.update(userBriefing);
    }

    @Override
    public void delete(UserBriefing userBriefing) {
        userBriefingStore.delete(userBriefing);
    }
}
