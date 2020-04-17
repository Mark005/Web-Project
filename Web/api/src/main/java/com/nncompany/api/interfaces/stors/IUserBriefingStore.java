package com.nncompany.api.interfaces.stors;

import com.nncompany.api.interfaces.IDao;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;

import java.util.List;

public interface IUserBriefingStore extends IDao<UserBriefing> {

    List<Briefing> getBriefingsByCurrentUser(User user);

    List<User> getUsersByCurrentBriefing(Briefing briefing);

    List<UserBriefing> getAll(UserBriefingSort sort, Direction direction);
}
